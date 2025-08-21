package Server;

import Server.commands.*;
import Server.handler.CommandHandler;
import Server.managers.*;
import Server.network.DatabaseHandler;
import Server.network.UPDDatagramServer;
import java.net.*;
import java.util.List;


public class Server {
    public static final String HASHING_ALGORITHM = "SHA-1";
    public static final String DATABASE_URL = "jdbc:postgresql://localhost:15432/studs";
    public static final String DATABASE_URL_HELIOS = "jdbc:postgresql://localhost:15432/studs";
    public static final String DATABASE_CONFIG_PATH = "C:\\Users\\nekru\\IdeaProjects\\progalab7\\dbconfig.cfg";

    public static final int PORT = 49125;

    public static void main(String[] args) {
        CollectionManager collectionManager = new CollectionManager(DatabaseHandler.getDatabaseManager());

        CommandManager commandManager = new CommandManager(DatabaseHandler.getDatabaseManager());
        commandManager.addCommand(List.of(
                new Add(collectionManager),
                new Show(collectionManager),
                new Help(commandManager),
                new Info(collectionManager),
                new Clear(collectionManager, DatabaseHandler.getDatabaseManager()),
                new FilterContainsName(collectionManager),
                new RemoveById(collectionManager),
                new Update(collectionManager),
                new Execute_script()
        ));

        // Сервер UDP
        try {
            CommandHandler handler = new CommandHandler(commandManager, DatabaseHandler.getDatabaseManager());
            UPDDatagramServer server = new UPDDatagramServer(InetAddress.getLocalHost(), PORT, handler);
            server.run();
        } catch (SocketException | UnknownHostException e) {
            System.out.println("Ошибка запуска сервера: " + e.getMessage());
        }
    }
}

