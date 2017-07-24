package chatredes;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    
    //private int porta = 6790; // porta do servidor
    private String nomeUsuario;
    private Socket socketCliente;
    private ArrayList<String> listaArq;
    private String caminhoPasta;
    private String sala;
    private InetAddress salaChat = null;

    public Usuario(Socket socketCliente){
        this.socketCliente=socketCliente;
    }

    
    // GETTERS E SETTERS
    public String getNomeUsuario() {
        return nomeUsuario;
    }
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
    
    public Socket getSocketCliente() {
        return socketCliente;
    }

    public ArrayList<String> getListaArq() {
        return listaArq;
    }
    public void setListaArq(ArrayList<String> listaArq) {
        this.listaArq = listaArq;
    }
    public String getCaminhoPasta() {
        return caminhoPasta;
    }
    public void setCaminhoPasta(String caminhoPasta) {
        this.caminhoPasta = caminhoPasta;
    }
    public String getSala() {
        return sala;
    }
    public void setSala(String sala) {
        this.sala = sala;
    }
    public InetAddress getSalaChat() {
        return salaChat;
    }
    public void setSalaChat(InetAddress salaChat) {
        this.salaChat = salaChat;
    }

    


    //metodo teste para verificar se esta pegando os arquivos da pasta informada
    public void imprimirListaArq() {
        for (int i = 0; i < listaArq.size(); i++) {
            System.out.println(listaArq.get(i) + '\n');
        }
    }
}
