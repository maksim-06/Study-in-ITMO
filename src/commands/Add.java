package commands;


import managers.CollectionManager;
import models.Ask;
import models.Product;
import utility.Interrogator;
import utility.console.Console;

import java.util.Scanner;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Add extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Add(Console console, CollectionManager collectionManager) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        try {
            console.println("* Создание нового продукта:");
            Scanner scanner = Interrogator.getUserScanner();
            Product product = Ask.askProduct(scanner, collectionManager);

            if (product != null) {
                collectionManager.addToCollection(product);
                console.println("Продукт успешно добавлен!");
                return true;
            } else {
                console.printError("Продукт не был создан.");
            }
        } catch (Ask.AskBreak ignored) {
            console.printError("Ввод отменён пользователем.");
        }

        return false;
    }
}
