package Client.network;

import Common.network.requests.Request;
import Common.network.responses.Response;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

public class UPDClient {
    private final int PACKET_SIZE = 1024;
    private final int DATA_SIZE = PACKET_SIZE - 1;
    private final DatagramChannel client;
    private final InetSocketAddress addr;

    public UPDClient(InetAddress address, int port) throws IOException{
        this.addr = new InetSocketAddress(address,port);
        this.client=DatagramChannel.open().bind(null).connect(addr);
        this.client.configureBlocking(false);
        System.out.println("DatagramChannel подключен к " + addr);
    }

    public Response sendAndReceiveCommand(Request request) throws IOException, ClassNotFoundException {
        var data = serialize(request);
        var responceBytes = sendAndReceiveData(data);

        // Десериализация ответа из байтового массива
        ByteArrayInputStream byteIn = new ByteArrayInputStream(responceBytes);
        Response response;
        try (ObjectInputStream in = new ObjectInputStream(byteIn)) {
            response = (Response) in.readObject();
        }
        return response;
    }

    public byte[] serialize(Request request) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(request);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new IOException("Ошибка при сериализации запроса: " + e.getMessage());
        }
    }


    private void sendData(byte[] data) throws IOException {
        int totalChunks = (int) Math.ceil(data.length / (double) DATA_SIZE);
        byte[][] ret = new byte[totalChunks][DATA_SIZE];

        int start = 0;
        for (int i = 0; i < ret.length; i++) {
            ret[i] = Arrays.copyOfRange(data, start, Math.min(start + DATA_SIZE, data.length));
            start += DATA_SIZE;
        }

        for (int i = 0; i < ret.length; i++) {
            byte[] chunk = ret[i];
            byte[] packet;

            if (i == ret.length - 1) {
                packet = new byte[chunk.length + 1];
                System.arraycopy(chunk, 0, packet, 0, chunk.length);
                packet[packet.length - 1] = 1;
            } else {
                packet = new byte[chunk.length + 1];
                System.arraycopy(chunk, 0, packet, 0, chunk.length);
                packet[packet.length - 1] = 0;
            }

            client.send(ByteBuffer.wrap(packet), addr);
        }
    }

    private byte[] receiveData() throws IOException {
        boolean received = false;
        byte[] result = new byte[0];

        while (!received) {
            byte[] packetData = receiveData(PACKET_SIZE);

            int dataLength = packetData.length;
            if (dataLength == 0) {
                System.out.println("Получен пустой пакет!");
                continue;
            }

            if (packetData[dataLength - 1] == 1) {
                received = true;

                byte[] newChunk = Arrays.copyOf(packetData, dataLength - 1);
                byte[] newResult = new byte[result.length + newChunk.length];
                System.arraycopy(result, 0, newResult, 0, result.length);
                System.arraycopy(newChunk, 0, newResult, result.length, newChunk.length);
                result = newResult;

            } else {

                byte[] newResult = new byte[result.length + packetData.length];
                System.arraycopy(result, 0, newResult, 0, result.length);
                System.arraycopy(packetData, 0, newResult, result.length, packetData.length);
                result = newResult;
            }
        }

        return result;
    }

    private byte[] receiveData(int bufferSize) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        SocketAddress address = null;
        int bytesRead = 0;

        while(address == null) {
            address = client.receive(buffer);
            if(address != null) {
                bytesRead = buffer.position();
            }
        }

        byte[] receivedData = new byte[bytesRead];
        buffer.flip();
        buffer.get(receivedData, 0, bytesRead);
        return receivedData;
    }


    private byte[] sendAndReceiveData(byte[] data) throws IOException {
        sendData(data);
        return receiveData();
    }
}
