package Server.network;

import Common.network.responses.NoSuchCommandResponse;
import Common.network.responses.Response;

import Server.handler.CommandHandler;
import Common.network.requests.Request;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public abstract class UPDServer {
    private final InetSocketAddress addr;
    private final CommandHandler commandHandler;
    private boolean running = true;

    public UPDServer(InetSocketAddress addr, CommandHandler commandHandler) {
        this.addr = addr;
        this.commandHandler = commandHandler;
    }

    public InetSocketAddress getAddr() {
        return addr;
    }


    public abstract DataWithSocket receiveData() throws IOException;

    public class DataWithSocket {
        private final byte[] data;
        private final SocketAddress socketAddress;

        public DataWithSocket(byte[] data, SocketAddress socketAddress) {
            this.data = data;
            this.socketAddress = socketAddress;
        }

        public byte[] getData() {
            return data;
        }

        public SocketAddress getSocketAddress() {
            return socketAddress;
        }
    }

    public abstract void sendData(byte[] data, SocketAddress addr) throws IOException;

    public abstract void connectToClient(SocketAddress addr) throws SocketException;

    public abstract void disconnectFromClient();

    public void run() {
        System.out.println("Сервер запущен по адресу " + addr);
        while (running) {
            DataWithSocket dataWithSocket;
            try {
                dataWithSocket = receiveData();
            } catch (Exception e) {
                System.out.println("Ошибка получения данных : " + e.toString());
                disconnectFromClient();
                continue;
            }

            byte[] dataFromClient = dataWithSocket.getData();
            SocketAddress clientAddr = dataWithSocket.getSocketAddress();
            try {
                connectToClient(clientAddr);
                System.out.println("Соединено с " + clientAddr);
            } catch (Exception e) {
                System.out.println("Ошибка соединения с клиентом: " + e.toString());
            }


            Request request;
            try (ByteArrayInputStream bais = new ByteArrayInputStream(dataFromClient);
                 ObjectInputStream ois = new ObjectInputStream(bais)) {

                request = (Request) ois.readObject();
                System.out.println("Обработка " + request + " из " + clientAddr);

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Невозможно десериализовать объект запроса " + e);
                disconnectFromClient();
                continue;
            }


            Response response = null;
            try {
                response = commandHandler.handle(request);
            } catch (Exception e) {
                System.out.println("Ошибка выполнения команды : " + e.toString());
            }

            if (response == null) {
                response = new NoSuchCommandResponse(request.getName());
            }

            byte[] data;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(baos)) {

                oos.writeObject(response);
                oos.flush();
                data = baos.toByteArray();

            } catch (IOException e) {
                System.out.println("Ошибка при сериализации объекта ответа: " + e.toString());
                disconnectFromClient();
                System.out.println("Отключение от клиента " + clientAddr);
                return;
            }

            // Отправка данных клиенту
            try {
                sendData(data, clientAddr);
                System.out.println("Отправлен ответ клиенту " + clientAddr);
            } catch (Exception e) {
                System.out.println("Ошибка ввода-вывода : " + e.toString());
            }

            disconnectFromClient();
            System.out.println("Отключение от клиента " + clientAddr);
        }
    }
}
