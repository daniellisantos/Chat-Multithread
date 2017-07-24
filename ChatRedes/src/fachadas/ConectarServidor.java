package fachadas;

import chatredes.Usuario;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConectarServidor {
    
    public InetAddress enderecoIP = InetAddress.getByName("localhost");
    private int portaTCP = 6790;
    private int portaTCPreceber = 6791;
    private int portaUDP = 6750;
    private int portaUDPreceber = 6751;
    private int portaMulticast = 6868;
    //variaveis tcp
    private Socket socketClienteTCP;
    private DataOutputStream paraServidorTCP;
    private BufferedReader doServidorTCP;
    private String listaSalas;
    private String dados;
    //variaveis udp
    private DatagramSocket socketClienteUDP;
    private DatagramPacket pacoteUDP;
    private byte[] dadosEnviarUDP;
    DatagramSocket socketServidorUDP;
    //variaveis multicast
    private InetAddress ipMulticast = null;
    private MulticastSocket socketMulticast = null;
    
    
    
    //SOBRE CONEXAO TCP:
    //conexao inicial TCP estabelecida na abertura da janelaLogin
    public ConectarServidor() throws IOException{
        socketClienteTCP = new Socket(enderecoIP, portaTCP); // cria um novo socket TCP
        doServidorTCP = new BufferedReader(new InputStreamReader(socketClienteTCP.getInputStream()));//cria uma variavel para receber os dados do servidor
        paraServidorTCP = new DataOutputStream(socketClienteTCP.getOutputStream());//cria uma variavel para enviar os dados para o servidor e ja conecta com ele
        listaSalas = doServidorTCP.readLine();//recebe as salas do servidor
    } 
    public String getListaSalas(){
        return listaSalas;
    }
    public void setDadosLogin(String login, String sala, String arquivosPasta, String caminhoPasta) throws IOException{
        dados = login+";"+sala+";"+arquivosPasta+";"+caminhoPasta+"\n";
    }
    public void enviarDadosLogin() throws IOException{
        paraServidorTCP.writeBytes(dados);
    }
    
    //SOBRE CONEXAO UDP:
    public void conectarUDP() throws SocketException, UnknownHostException{
        socketClienteUDP = new DatagramSocket();
        byte[] byteVazio = new byte[0];
        pacoteUDP = new DatagramPacket(byteVazio,0,enderecoIP,portaUDP);
        dadosEnviarUDP = new byte[1000]; //tamanho do pacote que vai ser enviado
    }
    public void receberUDP() throws IOException{ //parte 'servidor' do cliente, para receber mensagens
        socketServidorUDP = new DatagramSocket(portaUDPreceber);
        byte[] dadosReceber = new byte[1000];
        while(true){
            DatagramPacket pacoteRecebido = new DatagramPacket(dadosReceber, dadosReceber.length); //cria pacote UDP
            socketServidorUDP.receive(pacoteRecebido); //recebe o pacote UDP enviado pelo cliente
            String temp = new String(pacoteRecebido.getData()); //converte os dados enviados em string
            String mensagem = temp.substring(0, temp.indexOf("¨"));//pra nao aparecer lixo usei esse limite de caracter que é adicionado na captura da escrita em JanelaChat
        }
    }
    public void mandarMensagemUDP(String mensagem, Usuario usuarioDestino) throws IOException{
        usuarioDestino.getNomeUsuario();
        dadosEnviarUDP = mensagem.getBytes(); // tranforma a mensagem em bytes
        pacoteUDP.setData(dadosEnviarUDP); //coloca a mensagem no pacote
        pacoteUDP.setLength(mensagem.getBytes().length); //define o tamanho da mensagem
//        pacoteUDP.setAddress(enderecoIP); //define o IP destino
//        pacoteUDP.setPort(porta); //define a porta destino
//        pacoteUDP = new DatagramPacket(dadosEnviar, dadosEnviar.length, enderecoIP, porta);
        socketClienteUDP.send(pacoteUDP); //envia
        System.out.println("Mensagem para o servidor: "+mensagem);
    }
    
    //SOBRE CONEXAO MULTICAST:
    public void conectarMulticast() throws IOException{
        ipMulticast = InetAddress.getByName("224.225.226.227");
        socketMulticast = new MulticastSocket(portaMulticast);
        socketMulticast.joinGroup(ipMulticast);
    }
    public void enviarMensagemMulticast(String mensagem){
        try {
            DatagramPacket pacote = new DatagramPacket(mensagem.getBytes(), mensagem.length(), ipMulticast, portaMulticast);
            socketMulticast.send(pacote);
        } catch (IOException ex) {
            Logger.getLogger(ConectarServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void receberMensagemMulticast() throws IOException{
        byte[] buffer = new byte[2000];
        while(true){
            DatagramPacket recebido = new DatagramPacket(buffer, buffer.length);
            socketMulticast.setSoTimeout(120000);
            socketMulticast.receive(recebido);
            String temp = new String(recebido.getData()); //converte os dados enviados em string
            String mensagem = temp.substring(0, temp.indexOf("¨"));//pra nao aparecer lixo usei esse limite de caracter que é adicionado na captura da escrita em JanelaChat
            System.out.println("Mensagem multicast recebida: "+mensagem);
        }
    }
    
    
    
    
    
    //PARA DESCONECTAR DE TUDO:
    public void desconetar() throws IOException{
        socketMulticast.leaveGroup(ipMulticast);//sai primeiro do grupo multicast
        socketClienteUDP.close(); //encerra conexao UDP com servidor
        socketClienteTCP.close(); //encerra conexao TCP com servidor
        
    }
}
