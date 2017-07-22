package chatredes;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ConectarServidor {
    
    private Socket clientSocket;
    private DataOutputStream outToServer;
    private BufferedReader inFromServer;
    private String listaSalas;
    
    public ConectarServidor() throws IOException{
        clientSocket = new Socket("localhost", 6790); // cria um novo socket
        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));//cria uma variavel para receber os dados do servidor
        outToServer = new DataOutputStream(clientSocket.getOutputStream());//cria uma variavel para enviar os dados para o servidor e ja conecta com ele
        listaSalas = inFromServer.readLine();//recebe as salas do servidor
    }
    
    public String getListaSalas(){
        return listaSalas;
    }
    
    public void setDadosLogin(String login, String sala, String arquivosPasta, String caminhoPasta) throws IOException{
        String dados = login+";"+sala+";"+arquivosPasta+";"+caminhoPasta+"\n";
        outToServer.writeBytes(dados);
    }
    
}
