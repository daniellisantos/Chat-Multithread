package chatredes;

import chatredes.Usuario;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class Servidor implements Runnable{
    
    //private static String servidor = "localhost"; //endereco do servidor
    private static int porta = 6790; // porta do servidor
    private Usuario usuario;
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    ServerSocket socketInicial; //socket que vai ficar recebendo todas as conexoes na thread principal
    
    public Servidor() throws IOException{
        socketInicial = new ServerSocket(porta);
        conectar();
    }
    
    public void conectar() throws IOException{
        /* metodo para receber as conexoes. Provavelmente isso aqui vai ter que rodar como thread, ou 
        entao roda no Main e as conexoes que forem estabelecidas ficam rodando nas threads.*/
        System.out.println("Servidor iniciado, porta: "+porta);
        while (true) {
            System.out.println("Aguardando conexao...");
            Socket socketConexao = socketInicial.accept();//aceita a conexao
            System.out.println("Conexao aceita. Aguardando dados do usuario...");
            usuario = new Usuario(socketConexao);//inicializa um usuario com o seu socket
            DataOutputStream paraCliente = new DataOutputStream(socketConexao.getOutputStream());//inicializa uma varivel para enviar dados
            paraCliente.writeBytes("sala redes II"+'\n');//envia as salas disponiveis para o cliente
            BufferedReader doUsuario = new BufferedReader(new InputStreamReader(socketConexao.getInputStream()));//inicializa uma varivel com os dados vindos do cliente
            String dadosBrutoUsuario = doUsuario.readLine();//pega os bytes enviados do cliente e salva como String
            String[] dadosUsuario = dadosBrutoUsuario.split(Pattern.quote(";"));
            usuario.setNomeUsuario(dadosUsuario[0]);
            usuario.setSala(dadosUsuario[1]);//passando int aqui, depois associonar o numero à sala correta.
            String[] temp = dadosUsuario[2].split(Pattern.quote(";"));//separar a lista de arquivos
            ArrayList<String> temp2 = new ArrayList<>();//converter de String[] para ArrayList
            for(int i=0; i<temp.length; i++)
                temp2.add(temp[i]);
            usuario.setListaArq(temp2);//passa o ArrayList para o objeto usuario
            usuarios.add(usuario);
            System.out.println("Dados recebidos. Inserido na lista de Usuarios Ativos com sucesso! \n");
        }
    }
    
    //METODOS PARA ENVIAR E RECEBER
    public void enviarArq(){
        //Inserir comandos para enviar arquivos.
    }
    
    public void receberArq(){
        //inserir comandos para receber arquivos.
    }
    
    public void enviarMsg(){
        //inserir comandos para enviar mensagens.
    }
    
    public void receberMsg(){
        //inserir comandos para receber mensagens.
    }
    

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
     public static void main(String argv[]) throws Exception { 
         Servidor servidor = new Servidor();
         //servidor.run();
    }
}
