package chatredes;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    
    private static String servidor = "localhost"; //endereco do servidor
    private int porta = 6790; // porta do servidor
    private String nomeUsuario;
    private Socket socketCliente;
    private ArrayList<String> listaArq;
    private String caminhoPasta;
    private String sala;

    public Usuario(String nomeUsuario, List<String> listaArq) throws IOException {//desconsiderar esse construtor, por enquanto
        this.nomeUsuario=nomeUsuario;
        this.listaArq=new ArrayList<String>(listaArq);
        this.socketCliente = new Socket(servidor, porta);
    }
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
    public ArrayList<String> getListaArq() {
        return listaArq;
    }
    public void setListaArq(ArrayList<String> listaArq) {
        this.listaArq = listaArq;
    }
    public String getSala() {
        return sala;
    }
    public void setSala(String sala) {
        this.sala = sala;
    }    
    public String getCaminhoPasta() {
        return caminhoPasta;
    }
    public void setCaminhoPasta(String caminhoPasta) {
        this.caminhoPasta = caminhoPasta;
    }
    
    public void imprimirListaArq(){
        for(int i=0; i<listaArq.size(); i++)
            System.out.println(listaArq.get(i)+'\n');
    }
}
