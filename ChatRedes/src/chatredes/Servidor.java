package chatredes;

import threads.ConexaoTCP;
import chatredes.Usuario;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public final class Servidor {

    //private static String servidor = "localhost"; //endereco do servidor
    private static final int portaTCPservidor = 6790; // porta do servidor
    private static final int portaUDPservidor = 6750;
    private static final int portaMulticast = 6868;
    private final String salas;
    private Usuario usuario;
    public static ArrayList<Usuario> usuarios = new ArrayList<>();
    private final ServerSocket socketInicial; //socket TCP que vai ficar recebendo todas as conexoes na thread principal
    public DatagramSocket socketUDP;

    public Servidor() throws IOException {
        this.salas = "Redes de Computadores II;Programacao Movel;Banco de Dados;Administracao";
        socketInicial = new ServerSocket(portaTCPservidor);
        conectar();
    }

    public void conectar() throws IOException {
        //metodo para receber as conexoes TCP iniciais.
        System.out.println("Servidor iniciado, porta TCP: " + portaTCPservidor);
        while (true) {
            System.out.println("Aguardando conexao...");
            Socket socketConexaoTCP = socketInicial.accept();//aceita a conexao
            System.out.println("Conexao aceita.");
            usuario = new Usuario(socketConexaoTCP);//inicializa um usuario com o seu socket
            usuarios.add(usuario);

            Thread thread = new Thread(new ConexaoTCP(usuario, socketConexaoTCP));
            thread.start();
        }
    }


    public static void main(String argv[]) throws Exception {
        Servidor servidor = new Servidor();
//        ArrayBlockingQueue pilha = new ArrayBlockingQueue(5);
    }
}
