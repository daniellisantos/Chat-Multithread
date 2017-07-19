package chatredes;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    
    private static String servidor = "localhost"; //endereco do servidor
    private int porta = 6789; // porta do servidor
    private String username;
    private Socket clientSocket;
    private ArrayList<String> listaArq;

    public Usuario(String username, List<String> listaArq) throws IOException {
        this.username=username;
        this.listaArq=new ArrayList<String>(listaArq);
        this.clientSocket = new Socket(servidor, porta);
    }

    //GETTERS:
    public String getUsername() {
        return username;
    }
    public Socket getClientSocket() {
        return clientSocket;
    }
    public ArrayList<String> getListaArq() {
        return listaArq;
    }
    
}
