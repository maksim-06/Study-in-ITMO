package Client;


import Client.cli.Runner;
import Client.network.UPDClient;
import Client.utility.console.StandardConsole;
import java.io.IOException;
import java.net.InetAddress;


/**
 * Главный класс клиентского приложения.
 */
public class Client {
    private static int PORT = 49125;


    public static void main(String[] args){
        try {
            var console = new StandardConsole();
            var client = new UPDClient(InetAddress.getLocalHost(), PORT);
            var cli = new Runner(client, console);

            cli.interactiveMode();
        }

        catch (IOException e) {
            System.out.println("Невозможно подключиться к серверу!");
        }
    }
}
