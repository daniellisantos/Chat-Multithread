package chatredes;

import chatredes.Usuario;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public final class Servidor implements Runnable{
    
    //private static String servidor = "localhost"; //endereco do servidor
    private static int portaTCPservidor = 6790; // porta do servidor
    private static int portaUDPservidor = 6750;
    private static int portaMulticast = 6868;
    private String salas = "Redes de Computadores II;Programacao Movel;Banco de Dados;Administracao";
    private Usuario usuario;
    private Usuario usuarioThread;
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private ServerSocket socketInicial; //socket TCP que vai ficar recebendo todas as conexoes na thread principal
    private Socket socketThread;
    
    
    public Servidor() throws IOException{
        socketInicial = new ServerSocket(portaTCPservidor);
        conectar();
    }
    
    public Servidor(Usuario usuarioThread, Socket socketCon){
        this.usuario = usuarioThread;
        socketThread = socketCon;
    }
    
    public void conectar() throws IOException{
        //metodo para receber as conexoes iniciais.
        String resposta="";
        System.out.println("Servidor iniciado, porta TCP: "+portaTCPservidor);
        while (true) {
            System.out.println("Aguardando conexao...\n");
            Socket socketConexao = socketInicial.accept();//aceita a conexao
            System.out.println("Conexao aceita.");
            usuario = new Usuario(socketConexao);//inicializa um usuario com o seu socket
            usuarios.add(usuario);
            
            Thread thread = new Thread(new Servidor(usuario, socketConexao));
            thread.start();
            System.out.println("Usuario encaminhado para thread.");
        }
    }
    
    @Override
    public void run() {
        InetAddress ipGrupo;
        MulticastSocket socketMulticast;
        
        System.out.println("Aguardando dados do usuario...");
        try {
            DataOutputStream paraCliente = new DataOutputStream(socketThread.getOutputStream());//inicializa uma varivel para enviar dados
            paraCliente.writeBytes(salas+'\n');//envia as salas disponiveis para o cliente
            
            //RECEBENDO O RESTANTE DOS DADOS DO USUARIO:
            BufferedReader doUsuario = new BufferedReader(new InputStreamReader(socketThread.getInputStream()));//inicializa uma varivel com os dados vindos do cliente        
            String dadosBrutoUsuario = doUsuario.readLine();//pega os bytes enviados do cliente e salva como String
            String[] dadosUsuario = dadosBrutoUsuario.split(Pattern.quote(";"));
            usuario.setNomeUsuario(dadosUsuario[0]);//pega o nome de usuario
            usuario.setSala(dadosUsuario[1]);//pega a sala (o nome como String)
            String[] temp = dadosUsuario[2].split(Pattern.quote(":"));//separar a lista de arquivos
            ArrayList<String> temp2 = new ArrayList<>();//converter de String[] para ArrayList
            for(int i=0; i<temp.length; i++)
                temp2.add(temp[i]);
            usuario.setListaArq(temp2);//passa o ArrayList para o objeto usuario
            usuario.setCaminhoPasta(dadosUsuario[3]);
            
            System.out.println(usuario.getNomeUsuario()+" entrou no chat. Sala "+usuario.getSala());

            
/*DAQUI PRA BAIXO ACHO QUE TENHA QUE SER MUDADO. Creio que tera que encontrar um modo de gerar uma thread para 
cada tipo de conexao. Uma para manter a conexao TCP, uma para UDP e uma para Multicast, porque todas devem ficar
escutando ao mesmo tempo. Do mesmo modo será feito do lado do usuario (serao iniciadas na classe janelaChat, provavelmente)*/
            
            //DIRECIONANDO PARA A SALA DE CHAT CORRETA:
            switch(usuario.getSala()){
                case "Redes de Computadores II":
                    //insere o usuario no grupo do multithread certo e chama a thead
                    ipGrupo = InetAddress.getByName("224.225.226.227");
                    socketMulticast = new MulticastSocket(portaMulticast);
                    socketMulticast.joinGroup(ipGrupo);
                    usuario.setSalaChat(ipGrupo);
                    System.out.println("Adicionado a sala Redes II\n");
                    break;
                case "Programacao Movel":
                    //insere o usuario no grupo do multithread certo e chama a thead
                    ipGrupo = InetAddress.getByName("224.225.226.228");
                    socketMulticast = new MulticastSocket(portaMulticast+1);
                    socketMulticast.joinGroup(ipGrupo);
                    usuario.setSalaChat(ipGrupo);
                    break;
                case "Banco de Dados":
                    //insere o usuario no grupo do multithread certo e chama a thead
                    ipGrupo = InetAddress.getByName("224.225.226.229");
                    socketMulticast = new MulticastSocket(portaMulticast+2);
                    socketMulticast.joinGroup(ipGrupo);
                    usuario.setSalaChat(ipGrupo);
                    break;
                case "Administracao":
                    ///insere o usuario no grupo do multithread certo e chama a thead
                    ipGrupo = InetAddress.getByName("224.225.226.230");
                    socketMulticast = new MulticastSocket(portaMulticast+3);
                    socketMulticast.joinGroup(ipGrupo);
                    usuario.setSalaChat(ipGrupo);
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            receberMensagem(); //chama o metodo para receber as mensagens UDP
        } catch (SocketException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void receberMensagem() throws SocketException, IOException{
        //RECEBER MENSAGENS
        DatagramSocket socketUDP = new DatagramSocket(portaUDPservidor);
        byte[] dadosRecebidos = new byte[1000]; //1000 = 1kb
        
        while(true){
            DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos, dadosRecebidos.length); //cria pacote UDP
            socketUDP.receive(pacoteRecebido); //recebe o pacote UDP enviado pelo cliente
            String temp = new String(pacoteRecebido.getData()); //converte os dados enviados em string
            String mensagem = temp.substring(0, temp.indexOf("¨"));//pra nao aparecer lixo usei esse limite de caracter que é adicionado na captura da escrita em JanelaChat
                        
            System.out.println(usuario.getNomeUsuario()+" diz: "+mensagem);
        }
    }
    
    
    public static void main(String argv[]) throws Exception { 
         Servidor servidor = new Servidor();
         //servidor.run();
    }
}
