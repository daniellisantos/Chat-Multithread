package chatredes;

import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

public class Server {
   
    public static void main(String argv[]) throws Exception {
        
        ArrayBlockingQueue pilha = new ArrayBlockingQueue(5);
        
        pilha.put(new Message("bruno2", "bruno1", "Redes II", "Tudo certo!"));

        Runnable r;
        r = () -> {
            if (!pilha.isEmpty())
            {
                System.out.println(((Message)pilha.poll()).texto);
            }   
        };
        Thread t = new Thread(r);
        t.start();
    }

}
