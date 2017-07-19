package chatredes;

import chatredes.Usuario;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor implements Runnable{
    
    private Usuario usuario;
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    
    public Servidor(){
        //criei esse construtor mas acho que não vai precisar.
    }
    
    public void Conectar(Usuario user) throws IOException{
        /* metodo para receber as conexoes. Provavelmente isso aqui vai ter que rodar como thread, ou 
        entao roda no Main e as conexoes que forem estabelecidas ficam rodando nas threads.*/
        ServerSocket welcomeSocket = new ServerSocket(6789);
        
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            
        }
    }
    
    //METODOS PARA ENVIAR E RECEBER
    public void EnviarArq(){
        //Inserir comandos para enviar arquivos.
    }
    
    public void ReceberArq(){
        //inserir comandos para receber arquivos.
    }
    
    public void EnviarMsg(){
        //inserir comandos para enviar mensagens.
    }
    
    public void ReceberMsg(){
        //inserir comandos para receber mensagens.
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
