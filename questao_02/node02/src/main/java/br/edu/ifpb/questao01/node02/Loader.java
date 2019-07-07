package br.edu.ifpb.questao01.node02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Loader {

    public static void main(String[] args) throws IOException {
        new Loader("localhost", 12345).run(); // inicia o servidor
    }

    private int port;
    private String host;

    public Loader (String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws IOException {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(this.host, this.port));
        System.out.println("Aguardando conexão na porta 12345...");

        byte[] setOperation = new byte[9];
        int[] setOperationPlan;

        while (true) {
            Socket socket = server.accept(); // aceita um cliente (node01)
            System.out.println("Nova conexão com o cliente " + server.getInetAddress().getHostAddress());

            socket.getInputStream().read(setOperation);
            System.out.println("Mensagem recebida do cliente!");

            setOperationPlan = convertByteArrayToIntArray(setOperation);
            System.out.println("Conjunto de operacao recebida do cliente: {"
                    + setOperationPlan[0] + ", " + setOperationPlan[1]
                    + ", " + setOperationPlan[2] + "}");

            int x = setOperationPlan[0];
            int y = setOperationPlan[1];
            int operation = setOperationPlan[2];

            final int resultErroOperation = -100; //identifica erro na operacao
            int resultOperation = resultErroOperation;

            if (operation == 1) resultOperation = opDiff(x, y);
            else resultOperation = opSum(x, y);

            System.out.println("Operação realizada: " + operation);
            System.out.println("Resultado da operacao: " + resultOperation);

            socket.getOutputStream().write(intToBytes(resultOperation));
            System.out.println("Resultado enviado para o cliente!");

            socket.close();
        }
    }

    /**
     * Converte um array de byte em array de int
     * @param entrada array de byte para a conversão
     * @return array de int correspondente
     */
    private int[] convertByteArrayToIntArray(byte[] entrada) {
        int[] sin = new int[entrada.length];
        for(int i =0; i<entrada.length; i++) {
            sin[i] = entrada[i];
        }
        return sin;
    }

    /**
     * Converte um inteiro para aray de byte
     * @param i número inteiro a ser convertido
     * @return array de byte correspondente
     */
    private byte[] intToBytes(int i) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        return bb.array();
    }

    private int opSum(int x, int y) {
        return (x + y);
    }

    private int opDiff(int x, int y) {
        return (x - y);
    }

}
