package fachadas;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConectarServidor implements Runnable {
    
    public InetAddress enderecoIP = InetAddress.getByName("localhost"); //do servidor
    private final int portaTCP = 6790; //do servidor
    private final int portaTCPreceber = 6791;
    private final int portaUDP = 6750;
    private final int portaUDPreceber = 6751;
    private final int portaMulticast = 6868;
    //variaveis tcp
    private final Socket socketClienteTCP;
    private final DataOutputStream paraServidorTCP;
    private final BufferedReader doServidorTCP;
    private final String listaSalas;
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
    
    
    //SOBRE CONEXAO MULTICAST:
    public void conectarMulticast() throws IOException{
        ipMulticast = InetAddress.getByName("224.225.226.227");
        socketMulticast = new MulticastSocket(portaMulticast);
        socketMulticast.joinGroup(ipMulticast);
    }
    public void enviarMensagemMulticast(String mensagem) throws IOException{
        conectarMulticast();
        try {
            DatagramPacket pacote = new DatagramPacket(mensagem.getBytes(), mensagem.length(), ipMulticast, portaMulticast);
            socketMulticast.send(pacote);
            System.out.println("Mensagem multicast enviada: "+mensagem);
        } catch (IOException ex) {
            Logger.getLogger(ConectarServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        //socketMulticast.leaveGroup(ipMulticast);
    }
    public void receberMensagemMulticast() throws IOException{
        byte[] buffer = new byte[2000];
        while(true){
            DatagramPacket recebido = new DatagramPacket(buffer, buffer.length);
            socketMulticast.setSoTimeout(120000);
            socketMulticast.receive(recebido);
            String temp = new String(recebido.getData()); //converte os dados enviados em string
            String mensagem = temp.substring(0, temp.indexOf("|"));//pra nao aparecer lixo usei esse limite de caracter que é adicionado na captura da escrita em JanelaChat
            System.out.println("Mensagem multicast recebida: "+mensagem);
        }
    }
    

    //PARA DESCONECTAR DE TUDO:
    public void desconetar() throws IOException{
        socketMulticast.leaveGroup(ipMulticast);//sai primeiro do grupo multicast
        socketClienteUDP.close(); //encerra conexao UDP com servidor
        socketClienteTCP.close(); //encerra conexao TCP com servidor 
    }

    @Override
    public void run() {
        try {
            conectarMulticast();
            receberMensagemMulticast();
        } catch (IOException ex) {
            Logger.getLogger(ConectarServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public ListModel<String> atualizarLista() throws IOException 
        {  
         
       String receber;
       DefaultListModel usuariosOnline  = new DefaultListModel();// tem q ser do tipo model pra ser compativel no Jlist
       BufferedReader usuarioOnline = new BufferedReader(new InputStreamReader(socketClienteTCP.getInputStream()));// variavel q recebe os nomes dos usuarrios  vindos do servidor
       receber =usuarioOnline.readLine(); //recebe
        for( int i =0; i< 2; i++)// coloquei até dois pra testar
            {
            usuariosOnline.add(i,receber);// passa para variavel do tipo model para enviar para Jlist               
         }
        return usuariosOnline;
    }
}
