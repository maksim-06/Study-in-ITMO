package Server.network;

import Common.network.responses.NoSuchCommandResponse;
import Common.network.responses.Response;

import Server.handler.CommandHandler;
import Common.network.requests.Request;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ForkJoinPool;

public abstract class UPDServer {
    private final InetSocketAddress addr;
    private final CommandHandler commandHandler;
    private boolean running = true;
    private final ForkJoinPool processPool = new ForkJoinPool();
    private final ForkJoinPool responsePool = new ForkJoinPool();

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

    public void run() {
        System.out.println("Сервер запущен по адресу " + addr);

        while (running) {
            DataWithSocket dataWithSocket;
            try {
                dataWithSocket = receiveData();
            } catch (IOException e) {
                System.out.println("Ошибка получения данных: " + e);
                continue;
            }

            processPool.submit(() -> {
                SocketAddress clientAddr = dataWithSocket.getSocketAddress();
                byte[] dataFromClient = dataWithSocket.getData();

                // Десериализация
                Request request;
                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dataFromClient))) {
                    request = (Request) ois.readObject();
                    System.out.println("Обработка " + request + " от " + clientAddr);
                } catch (Exception e) {
                    System.out.println("Невозможно десериализовать запрос: " + e);
                    return;
                }

                // Обработка
                Response response;
                try {
                    response = commandHandler.handle(request);
                    if (response == null) {
                        response = new NoSuchCommandResponse(request.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response = new NoSuchCommandResponse(request.getName());
                }

                // Сериализация ответа
                byte[] responseData;
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                     ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                    oos.writeObject(response);
                    responseData = baos.toByteArray();
                } catch (IOException e) {
                    System.out.println("Ошибка сериализации ответа: " + e);
                    return;
                }

                // Отправка
                responsePool.submit(() -> {
                    try {
                        sendData(responseData, clientAddr);
                        System.out.println("Ответ отправлен клиенту " + clientAddr);
                    } catch (IOException e) {
                        System.out.println("Ошибка при отправке ответа: " + e);
                    }
                });
            });
        }
    }
}








