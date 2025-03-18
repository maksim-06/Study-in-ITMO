package commands;

import managers.CollectionManager;
import utility.console.Console;
import models.Ask;
import models.Product;

import java.util.Scanner;

/**
 * Команда 'update'. Обновляет элемент коллекции.
 */
public class Update extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Update(Console console, CollectionManager collectionManager) {
        super("update <ID> {element}", "обновить значение элемента коллекции по ID");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] arguments) {
        try {
            if (arguments.length != 2) {
                console.printError("Неправильное количество аргументов!");
                console.println("Использование: '" + getName() + "'");
                return false;
            }

            long id;
            try {
                id = Long.parseLong(arguments[1].trim());
            } catch (NumberFormatException e) {
                console.printError("ID должен быть числом.");
                return false;
            }

            Product existingProduct = null;
            for (Product product : collectionManager.getCollection()) {
                if (product.getId().equals(id)) {
                    existingProduct = product;
                    break;
                }
            }

            if (existingProduct == null) {
                console.printError("Продукт с указанным ID не найден.");
                return false;
            }

            console.println("* Обновление продукта:");
            Scanner scanner = new Scanner(System.in); // Используем Scanner для ввода
            Product updatedProduct = Ask.askProduct(scanner, collectionManager);

            if (updatedProduct != null) {
                // Создаём новый продукт с тем же ID и датой создания
                Product newProduct = new Product(
                        existingProduct.getId(),
                        updatedProduct.getName(),
                        updatedProduct.getCoordinates(),
                        existingProduct.getCreationDate(), // Сохраняем старую дату
                        updatedProduct.getPrice(),
                        updatedProduct.getUnitOfMeasure(),
                        updatedProduct.getManufacturer()
                );

                // Удаляем старый продукт и добавляем новый
                collectionManager.getCollection().remove(existingProduct);
                collectionManager.getCollection().add(newProduct);

                console.println("Продукт успешно обновлён!");
                return true;
            } else {
                console.printError("Ошибка при обновлении продукта.");
            }
        } catch (Ask.AskBreak e) {
            console.printError("Ввод отменён пользователем.");
        }
        return false;
    }
}

