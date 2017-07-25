package chatredes;

import java.util.Stack;

public class Server {

    public static void main(String argv[]) throws Exception {
        
        Stack pilha = new Stack();
        
        pilha.push(new Message("bruno", "bruno2", "Redes II", "Olá, tudo bem?"));

        Runnable r;
        r = () -> {
            if (!pilha.empty())
            {
                System.out.println(((Message)pilha.pop()).texto);
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

}
