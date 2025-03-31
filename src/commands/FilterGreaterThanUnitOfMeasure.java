package commands;

import exceptions.WrongAmountOfElementsException;
import managers.CollectionManager;
import models.Product;
import utility.console.Console;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда 'filter_greater_than_unit_of_measure'. Фильтрация продуктов по полю unitOfMeasure.
 */
public class FilterGreaterThanUnitOfMeasure extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public FilterGreaterThanUnitOfMeasure(Console console, CollectionManager collectionManager) {
        super("filter_greater_than_unit_of_measure <UNIT>", "вывести элементы, значение поля unitOfMeasure которых больше заданного");
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
            if (arguments[1].isEmpty()) throw new WrongAmountOfElementsException();

            var unitOfMeasure = Integer.parseInt(arguments[1]);
            var products = filterGreaterThanUnitOfMeasure(unitOfMeasure);

            if (products.isEmpty()) {
                console.println("Продуктов с unitOfMeasure больше " + unitOfMeasure + " не обнаружено.");
            } else {
                console.println("Продуктов с unitOfMeasure больше " + unitOfMeasure + ": " + products.size() + " шт.\n");
                products.forEach(console::println);
            }

            return true;

        } catch (NumberFormatException exception) {
            console.printError("unitOfMeasure должно быть представлено числом!");
        } catch (WrongAmountOfElementsException exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
        }
        return false;
    }

    private List<Product> filterGreaterThanUnitOfMeasure(int unitOfMeasure) {
        return collectionManager.getCollection().stream()
                .filter(product -> product.getUnitOfMeasure().ordinal() > unitOfMeasure) // фильтруем по порядковому номеру
                .collect(Collectors.toList());
    }
}
