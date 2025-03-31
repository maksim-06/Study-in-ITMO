package commands;

import managers.CollectionManager;
import utility.console.Console;

/**
 * Команда 'remove_first'. Удаляет первый элемент из коллекции.
 */
public class Remove_first extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Remove_first(Console console, CollectionManager collectionManager) {
        super("remove_first", "удалить первый элемент из коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (collectionManager.getCollection().isEmpty()) {
            console.println("Коллекция пуста!");
            return false;
        }

        // Удаляем первый элемент из коллекции (стека)
        collectionManager.removeFirst();

        console.println("Первый элемент успешно удалён!");
        return true;
    }
}
