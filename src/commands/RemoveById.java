package commands;

import managers.CollectionManager;
import utility.console.Console;
import models.Product;

/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции.
 */
public class RemoveById extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveById(Console console, CollectionManager collectionManager) {
        super("remove_by_id <ID>", "удалить элемент из коллекции по ID");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (arguments.length < 2 || arguments[1].isEmpty()) {
            console.println("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        long id = -1;
        try {
            id = Long.parseLong(arguments[1].trim());
        } catch (NumberFormatException e) {
            console.println("ID не распознан");
            return false;
        }

        // Проверка существования элемента по ID
        Product product = collectionManager.byId(id);
        if (product == null || !collectionManager.getCollection().contains(product)) {
            console.println("Не существующий ID");
            return false;
        }

        // Удаляем продукт из коллекции
        collectionManager.remove(id);

        console.println("Продукт успешно удалён!");
        return true;
    }
}
