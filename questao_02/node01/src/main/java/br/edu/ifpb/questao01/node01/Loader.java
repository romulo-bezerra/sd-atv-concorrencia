package br.edu.ifpb.questao01.node01;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Esta é uma aplicação cliente que tem por finalidade gerar
 * dois numéros aleátorios para ser enviado ao node02 (servidor)
 * inicialmente e logo depois e continuamente ficar recebendo o
 * resultado da operação aleatória e reenviando junto a outro
 * número aleatório.
 */
public class Loader {

    public static void main(String[] args) throws IOException, InterruptedException {
        new Loader("localhost", 12345).run(); // dispara cliente
    }

    private String host;
    private int port;

    public Loader (String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws IOException, InterruptedException {
        Socket socket = new Socket(this.host, this.port);
        System.out.println("O cliente se conectou ao servidor!");

        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            executor.execute(new SenderRequest(socket));
            long timeFinal = System.currentTimeMillis();
            long seconds = (timeFinal - startTime) / 1000;
            System.out.println("Tempo de execução de 10 requisições: " + seconds);
        } catch (Exception e) {
            System.out.println("Executors falhou\n" + e.getMessage());
        }
        executor.shutdown();
    }

}
