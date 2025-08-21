package Server;


import Server.handler.CommandHandler;
import Server.managers.CollectionManager;
import Server.managers.CommandManager;
import Server.managers.DumpManager;
import Server.commands.*;
import Server.network.UPDDatagramServer;


import java.io.IOException;
import java.net.InetAddress;
import java.util.List;


/**
 * Серверная часть приложения.
 */

public class Server {
    public static final int PORT = 49125;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Укажите файл с коллекцией как аргумент командной строки");
            System.exit(1);
        }

        var dumpManager = new DumpManager(args[0]);
        var collectionManager = new CollectionManager(dumpManager);
        collectionManager.validateAll();

        Runtime.getRuntime().addShutdownHook(new Thread(collectionManager::save));

        var commandManager = new CommandManager(collectionManager);
        commandManager.addCommand(List.of(
                new Add(collectionManager),
                new Show(collectionManager),
                new Help(commandManager),
                new Info(collectionManager),
                new Clear(collectionManager),
                new FilterContainsName(collectionManager),
                new RemoveById(collectionManager),
                new Update(collectionManager),
                new Execute_script()
        ));

        try {
            var server = new UPDDatagramServer(
                    InetAddress.getLocalHost(),
                    PORT,
                    new CommandHandler(commandManager)
            );
            server.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
