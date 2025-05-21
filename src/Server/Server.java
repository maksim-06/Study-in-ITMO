package Server;


import Common.utiluty.Commands;
import Server.handler.CommandHandler;
import Server.managers.CollectionManager;
import Server.managers.CommandManager;
import Server.managers.DumpManager;
import Server.commands.*;
import Server.network.UPDDatagramServer;


import java.io.IOException;
import java.net.InetAddress;


/**
 * Серверная часть приложения.
 */


public class Server {
    public static final int PORT = 49125;

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(1);
        }

        var dumpManager = new DumpManager(args[0]);
        var collectionmanager = new CollectionManager(dumpManager);

        collectionmanager.validateAll();

        Runtime.getRuntime().addShutdownHook(new Thread(collectionmanager::save));

        var commandManager = new CommandManager() {{
            register(Commands.HELP, new Help(this));
            register(Commands.INFO, new Info(collectionmanager));
            register(Commands.SHOW, new Show(collectionmanager));
            register(Commands.ADD, new Add(collectionmanager));
            register(Commands.UPDATE, new Update(collectionmanager));
            register(Commands.REMOVE_BY_ID, new RemoveById(collectionmanager));
            register(Commands.CLEAR, new Clear(collectionmanager));
            register(Commands.FILTER_CONTAINS_PART_NUMBER, new FilterContainsName(collectionmanager));
        }};

        try {
            var server = new UPDDatagramServer(InetAddress.getLocalHost(), PORT, new CommandHandler(commandManager));
            server.run();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

