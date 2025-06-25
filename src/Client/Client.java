package Client;


import Client.cli.Runner;
import Client.network.UPDClient;
import Client.utility.console.StandardConsole;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
    private static int PORT = 49125;

    public static void main(String[] args) {
        var console = new StandardConsole();

        try (var scanner = new Scanner(System.in)) {
            var client = new UPDClient(InetAddress.getLocalHost(), PORT);
            var cli = new Runner(client, console);

            cli.interactiveMode();

        } catch (IOException e) {
            console.printError("Невозможно подключиться к серверу!");
        } catch (Exception e) {
            console.printError("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }
}
