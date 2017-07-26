package threads;

import chatredes.Usuario;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ConexaoTCP implements Runnable {

    private static final int portaMulticast = 6868;
    private final String salas;
    private final Usuario usuario;
    public static ArrayList<Usuario> usuarios = new ArrayList<>();
    private final Socket socketThread;
    private InetAddress ipGrupo;
    private MulticastSocket socketMulticast;
    
    
    public ConexaoTCP(Usuario usuarioThread, Socket socketCon){
        this.salas = "Redes de Computadores II;Programacao Movel;Banco de Dados;Administracao";
        this.usuario = usuarioThread;
        socketThread = socketCon;
    }

    @Override
    public void run() {
        System.out.println("Aguardando dados do usuario...");
        try {
            DataOutputStream paraCliente = new DataOutputStream(socketThread.getOutputStream());//inicializa uma varivel para enviar dados
            paraCliente.writeBytes(salas + '\n');//envia as salas disponiveis para o cliente
            
            //RECEBENDO O RESTANTE DOS DADOS DO USUARIO:
            BufferedReader doUsuario = new BufferedReader(new InputStreamReader(socketThread.getInputStream()));//inicializa uma varivel com os dados vindos do cliente
            String dadosBrutoUsuario = doUsuario.readLine();//pega os bytes enviados do cliente e salva como String
            String[] dadosUsuario = dadosBrutoUsuario.split(Pattern.quote(";"));
            usuario.setNomeUsuario(dadosUsuario[0]);//pega o nome de usuario
            usuario.setSala(dadosUsuario[1]);//pega a sala (o nome como String)
            String[] temp = dadosUsuario[2].split(Pattern.quote(":"));//separar a lista de arquivos
            ArrayList<String> temp2 = new ArrayList<>();//converter de String[] para ArrayList
            for (int i = 0; i < temp.length; i++) {
                temp2.add(temp[i]);
            }
            usuario.setListaArq(temp2);//passa o ArrayList para o objeto usuario
            usuario.setCaminhoPasta(dadosUsuario[3]);//pega o caminho da pasta compartilhada
            
            System.out.println(usuario.getNomeUsuario() + " entrou no chat.");
            
        } catch (IOException ex) {
            Logger.getLogger(ConexaoTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            definirSala();
        } catch (IOException ex) {
            Logger.getLogger(ConexaoTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void definirSala() throws IOException{
        //DIRECIONANDO PARA A SALA DE CHAT CORRETA:
            switch (usuario.getSala()) {
                case "Redes de Computadores II":
                    //insere o usuario no grupo do multithread certo e chama a thead
                    ipGrupo = InetAddress.getByName("224.225.226.227");
                    socketMulticast = new MulticastSocket(portaMulticast);
                    socketMulticast.joinGroup(ipGrupo);
                    usuario.setSocketMulticast(socketMulticast);
//                    ArrayList<Usuario> usuariosS1 = new ArrayList<>();
//                    usuariosS1.add(usuario);
//                    usuariosS1.getBytes();
//                    DatagramPacket dtgrm = new DatagramPacket(msg.getBytes(), msg.length(), ipGrupo, porta);
                    System.out.println("Adicionado a sala Redes II\n");
                    break;
                case "Programacao Movel":
                    //insere o usuario no grupo do multithread certo e chama a thead
                    ipGrupo = InetAddress.getByName("224.225.226.228");
                    socketMulticast = new MulticastSocket(portaMulticast);
                    socketMulticast.joinGroup(ipGrupo);
                    usuario.setSocketMulticast(socketMulticast);
                    break;
                case "Banco de Dados":
                    //insere o usuario no grupo do multithread certo e chama a thead
                    ipGrupo = InetAddress.getByName("224.225.226.229");
                    socketMulticast = new MulticastSocket(portaMulticast);
                    socketMulticast.joinGroup(ipGrupo);
                    usuario.setSocketMulticast(socketMulticast);
                    break;
                case "Administracao":
                    ///insere o usuario no grupo do multithread certo e chama a thead
                    ipGrupo = InetAddress.getByName("224.225.226.230");
                    socketMulticast = new MulticastSocket(portaMulticast);
                    socketMulticast.joinGroup(ipGrupo);
                    usuario.setSocketMulticast(socketMulticast);
                    break;
            }
    }
    
}
