package Server.network;

import Server.handler.CommandHandler;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Arrays;

public class UPDDatagramServer extends UPDServer {
    private final int PACKET_SIZE = 1024;
    private final int DATA_SIZE = PACKET_SIZE - 1;

    private final DatagramSocket datagramSocket;


    public UPDDatagramServer(InetAddress address, int port, CommandHandler commandHandler) throws SocketException {
        super(new InetSocketAddress(address, port), commandHandler);
        this.datagramSocket = new DatagramSocket(getAddr());
        this.datagramSocket.setReuseAddress(true);
    }

    @Override
    public DataWithSocket receiveData() throws IOException {
        boolean received = false;
        byte[] result = new byte[0];
        SocketAddress addr = null;

        while (!received) {
            var data = new byte[PACKET_SIZE];
            var dp = new DatagramPacket(data, PACKET_SIZE);
            datagramSocket.receive(dp);
            System.out.println(datagramSocket);

            addr = dp.getSocketAddress();
            int dataLength = dp.getLength();

            if (dataLength > 0) {
                if (data[dataLength - 1] == 1) {
                    received = true;
                    System.out.println("Получение данных от " + dp.getAddress() + " окончено");
                }
            } else {
                System.out.println("Получен пустой пакет от " + dp.getAddress());
            }



            byte[] newChunk = Arrays.copyOf(data, dataLength - (dataLength > 0 ? 1:0) );
            byte[] combined = new byte[result.length + newChunk.length];
            System.arraycopy(result, 0, combined, 0, result.length);
            System.arraycopy(newChunk, 0, combined, result.length, newChunk.length);
            result = combined;
        }
        return new DataWithSocket(result, addr);
    }

    @Override
    public void sendData(byte[] data, SocketAddress addr) throws IOException {
        int chunkCount = (int) Math.ceil(data.length / (double) DATA_SIZE);
        byte[][] chunks = new byte[chunkCount][];

        int start = 0;
        for (int i = 0; i < chunkCount; i++) {
            int end = Math.min(start + DATA_SIZE, data.length);
            chunks[i] = Arrays.copyOfRange(data, start, end);
            start += DATA_SIZE;
        }

        for (int i = 0; i < chunks.length; i++) {
            byte[] chunk = chunks[i];
            byte[] sendData;
            DatagramPacket dp;

            if (i == chunks.length - 1) {
                sendData = new byte[chunk.length + 1];
                System.arraycopy(chunk, 0, sendData, 0, chunk.length);
                sendData[chunk.length] = 1;
                dp = new DatagramPacket(sendData, sendData.length, addr);
            } else {
                sendData = chunk;
                dp = new DatagramPacket(sendData, chunk.length, addr);
            }

            datagramSocket.send(dp);
        }

        System.out.println("Отправка данных завершена");
    }

    @Override
    public void connectToClient(SocketAddress addr) throws SocketException {
        datagramSocket.connect(addr);
    }

    @Override
    public void disconnectFromClient() {
        datagramSocket.disconnect();
    }
}


