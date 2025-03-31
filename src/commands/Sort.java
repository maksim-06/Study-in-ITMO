package commands;

import managers.CollectionManager;
import utility.console.Console;
import models.Product;
import java.util.Comparator;

/**
 * Команда 'sort'. Сортирует коллекцию в естественном порядке.
 */
public class Sort extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Sort(Console console, CollectionManager collectionManager) {
        super("sort", "сортировать коллекцию в естественном порядке");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду сортировки коллекции.
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        // Сортируем коллекцию продуктов по полю 'id'
        try {
            // Используем Comparator для сортировки по id
            collectionManager.getCollection().sort(Comparator.comparingLong(Product::getId));

            console.println("Коллекция успешно отсортирована.");
            return true;
        } catch (Exception e) {
            console.println("Ошибка при сортировке коллекции: " + e.getMessage());
            return false;
        }
    }
}
