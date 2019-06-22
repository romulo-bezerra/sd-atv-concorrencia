package br.edu.ifpb.questao01.node01.main;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Esta é uma aplicação cliente que tem por finalidade gerar
 * dois numéros aleátorios para ser enviado ao node02 (servidor)
 * inicialmente e logo depois e continuamente ficar recebendo o
 * resultado da operação aleatória e reenviando junto a outro
 * número aleatório.
 */
public class Loader {

    public static void main(String[] args) throws IOException, InterruptedException {
        new Loader("localhost", 12345).executa(); // dispara cliente
    }

    private String host;
    private int porta;

    public Loader (String host, int porta) {
        this.host = host;
        this.porta = porta;
    }

    /**
     * Gera dois números inteiros aleatórios
     * - Consideracoes:
     *      - x = primeiro numero para a operacao
     *      - y = segundo numero para a operacao
     * @return um array de int contendo dois números aleatórios
     */
    private int[] generateTwoNumbersRandom() {
        Random random = new Random();
        int x = random.nextInt(10); //numeros entre 0 e 9
        int y = random.nextInt(10); //numeros entre 0 e 9
        int op = 0;
        int[] setOperation = {x, y, op};

        return setOperation;
    }

    /**
     * Gera um número aleatório
     * @return
     */
    private int generateOneNumberRandom() {
        Random random = new Random();
        return random.nextInt(10);
    }

    /**
     * Gera um número aleatório entre 1 e 2 representando o cod
     * da operacao a ser realizada pelo server
     * - Consideracoes:
     *   - return 1 = diff
     *   - return 2 = sum
     * @return um numero inteiro aleatório representando o cod da operacao
     */
    private int generateNumberOperationRandom() {
        Random random = new Random();
        return random.nextInt(2) + 1; //numeros entre 1|-|2
    }

    public void executa() throws IOException, InterruptedException {
        Socket socket = new Socket(this.host, this.porta);
        System.out.println("O cliente se conectou ao servidor!");

        int[] setPlanOperationRandom = generateTwoNumbersRandom();
        setPlanOperationRandom[2] = generateNumberOperationRandom();
        makeAndSendMessage(socket, setPlanOperationRandom);

        byte[] resultOperationInBytes = new byte[9];

        socket.getInputStream().read(resultOperationInBytes);
        int resultOperation = convertByteArrayToInt(resultOperationInBytes);
        System.out.println("Resultado processado pelo servidor recebido");
        System.out.println("Resultado da operacao: " + resultOperation);
        socket.close();

        do {
            Socket socketConst = new Socket(this.host, this.porta);
            Thread.sleep(5000);
            int[] setOperationConstRandom = {generateOneNumberRandom(), resultOperation, generateNumberOperationRandom()};
            makeAndSendMessage(socketConst, setOperationConstRandom);

            byte[] resultOperationConstInBytes = new byte[9];

            socketConst.getInputStream().read(resultOperationConstInBytes);
            int resultOperationConst = convertByteArrayToInt(resultOperationConstInBytes);
            System.out.println("Resultado processado pelo servidor recebido");
            System.out.println("Resultado da operacao: " + resultOperationConst);
            socket.close();
        } while (true);

    }

    private void makeAndSendMessage(Socket socket, int[] setPlanOperationRandom) throws IOException {
        byte[] setOperation = new byte[9];
        setOperation = convertIntArrayToByteArray(setPlanOperationRandom);

        socket.getOutputStream().write(setOperation);
        System.out.println("Mensagem enviada para o servidor!");
        System.out.println("Detalhes: Array enviado: {"
                + setPlanOperationRandom[0] + ", " + setPlanOperationRandom[1]
                + ", " + setPlanOperationRandom[2] + "}");
    }

    /**
     * Converte um array de int de três posições em um array de byte
     * @param array array de inteiros para a conversão
     * @return array de bytes correpondentes
     */
    private byte[] convertIntArrayToByteArray(int[] array) {
        return new byte[]{(byte) array[0], (byte) array[1], (byte) array[2]};
    }

    /**
     * Converte um array de bytes em um inteiro
     * @param intBytes array de bytes correspondendo a um número inteiro
     * @return número inteiro correnpondente
     */
    private int convertByteArrayToInt(byte[] intBytes){
        ByteBuffer byteBuffer = ByteBuffer.wrap(intBytes);
        return byteBuffer.getInt();
    }

}
