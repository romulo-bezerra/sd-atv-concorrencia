package br.edu.ifpb.questao01.node01;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Random;

public class SenderRequest implements Runnable {

    private Socket socket;

    public SenderRequest(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        for (int i=0; i<10; i++) {
            int[] setPlanOperationRandom = generateTwoNumbersRandom();
            setPlanOperationRandom[2] = generateNumberOperationRandom();

            if (i == 0){
                makeAndSendMessage(socket, setPlanOperationRandom);
            }

            byte[] resultOperationInBytes = new byte[9];
            try {
                socket.getInputStream().read(resultOperationInBytes);
                int resultOperation = convertByteArrayToInt(resultOperationInBytes);
                System.out.println("Resultado processado pelo servidor recebido");
                System.out.println("Resultado da operacao: " + resultOperation);

                if (i > 0) {
                    int[] setOperationConstRandom = {generateOneNumberRandom(), resultOperation, generateNumberOperationRandom()};
                    socket = new Socket("localhost", 12345);
                    makeAndSendMessage(socket, setOperationConstRandom);
                }
            } catch (IOException e) {
                System.out.println("Não foi possível lê a mensagem\n" + e.getMessage());
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Falha ao fechar socket\n" + e.getMessage());
        }
    }

    /**
     * Constroi e envia uma mensaagem para server de destino do socket
     * @param socket canal de destino da mensagem para o server
     * @param setPlanOperationRandom conjunto de operacao com tres parametros {x,y,op}
     */
    private void makeAndSendMessage(Socket socket, int[] setPlanOperationRandom){
        byte[] setOperation = new byte[9];
        setOperation = convertIntArrayToByteArray(setPlanOperationRandom);

        try {
            socket.getOutputStream().write(setOperation);
            System.out.println("Mensagem enviada para o servidor!");
            System.out.println("Detalhes: Array enviado: {"
                    + setPlanOperationRandom[0] + ", " + setPlanOperationRandom[1]
                    + ", " + setPlanOperationRandom[2] + "}");
        } catch (IOException e) {
            System.out.println("Não foi possível enviar a mensagem\n" + e.getMessage());
        }
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

    /**
     * Converte um array de bytes em um inteiro
     * @param intBytes array de bytes correspondendo a um número inteiro
     * @return número inteiro correnpondente
     */
    private int convertByteArrayToInt(byte[] intBytes){
        ByteBuffer byteBuffer = ByteBuffer.wrap(intBytes);
        return byteBuffer.getInt();
    }

    /**
     * Gera um número aleatório
     * @return
     */
    private int generateOneNumberRandom() {
        Random random = new Random();
        return random.nextInt(10);
    }

}
