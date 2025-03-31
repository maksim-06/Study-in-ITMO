package commands;

import managers.CollectionManager;
import utility.console.Console;
import models.Product;

/**
 * Команда 'remove_any_by_manufacturer'. Удаляет элемент из коллекции по производителю.
 */
public class RemoveAnyByManufacturer extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveAnyByManufacturer(Console console, CollectionManager collectionManager) {
        super("remove_any_by_manufacturer <MANUFACTURER>", "удалить из коллекции один элемент, значение поля manufacturer которого эквивалентно заданному");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        // Проверка на правильность количества аргументов
        if (arguments.length < 2 || arguments[1].isEmpty()) {
            console.println("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        String manufacturerName = arguments[1].trim();

        // Проверка наличия элемента с таким производителем в коллекции
        Product productToRemove = collectionManager.getCollection().stream()
                .filter(product -> product.getManufacturer() != null && product.getManufacturer().getName().equals(manufacturerName))
                .findFirst()
                .orElse(null);

        if (productToRemove == null) {
            console.println("Продукта с производителем '" + manufacturerName + "' не найдено!");
            return false;
        }

        // Удаляем продукт из коллекции
        collectionManager.remove1(productToRemove);

        console.println("Продукт с производителем '" + manufacturerName + "' успешно удалён!");
        return true;
    }
}
