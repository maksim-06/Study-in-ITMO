package commands;

import exceptions.WrongAmountOfElementsException;
import managers.CollectionManager;
import models.Product;
import utility.console.Console;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда 'filter_contains_name'. Фильтрация продуктов по полю name.
 */
public class FilterContainsName extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public FilterContainsName(Console console, CollectionManager collectionManager) {
        super("filter_contains_name <name>", "вывести элементы, значение поля name которых содержит заданную подстроку");
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
            if (arguments.length < 2 || arguments[1].isEmpty()) throw new WrongAmountOfElementsException();
            var products = filterByName(arguments[1]);

            if (products.isEmpty()) {
                console.println("Продуктов, чьи name содержат '" + arguments[1] + "' не обнаружено.");
            } else {
                console.println("Продуктов, чьи name содержат '" + arguments[1] + "' обнаружено " + products.size() + " шт.\n");
                products.forEach(product -> console.println(product));
            }
            return true;
        } catch (WrongAmountOfElementsException exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
        }
        return false;
    }

    private List<Product> filterByName(String nameSubstring) {
        return collectionManager.getCollection().stream()
                .filter(product -> (product.getName() != null && product.getName().contains(nameSubstring)))
                .collect(Collectors.toList());
    }
}
