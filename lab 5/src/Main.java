import commands.*;

import managers.CollectionManager;
import managers.CommandManager;
import managers.DumpManager;
import utility.*;
import utility.console.StandardConsole;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Interrogator.setUserScanner(new Scanner(System.in));
        var console = new StandardConsole();

        if (args.length == 0) {
            console.println("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(1);
        }

        var dumpManager = new DumpManager(args[0], console);
        var collectionManager = new CollectionManager(dumpManager);



        collectionManager.validateAll(console);

        var commandManager = new CommandManager() {{
            register("help", new Help(console, this));
            register("info", new Info(console, collectionManager));
            register("show", new Show(console, collectionManager));
            register("add", new Add(console, collectionManager));
            register("update", new Update(console, collectionManager));
            register("remove_by_id", new RemoveById(console, collectionManager));
            register("clear", new Clear(console, collectionManager));
            register("save", new Save(console, collectionManager));
            register("remove_first", new Remove_first(console, collectionManager));
            register("exit", new Exit(console));
            register("history", new History(console, this));
            register("sort", new Sort(console, collectionManager));
            register("filter_contains_name", new FilterContainsName(console, collectionManager));
            register("filter_greater_than_unit_of_measure", new FilterGreaterThanUnitOfMeasure(console, collectionManager));
            register("remove_any_by_manufacturer", new RemoveAnyByManufacturer(console, collectionManager));
            register("execute_script", new Execute_script(console));
        }};
        new Runner(console, commandManager).interactiveMode();
    }
}