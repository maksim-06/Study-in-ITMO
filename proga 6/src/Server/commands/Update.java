package Server.commands;

import Client.ask.Ask;
import Common.domein.Product;
import Common.exception.IllegalArguments;
import Common.network.requests.Request;
import Common.network.responses.Response;
import Server.managers.CollectionManager;

import java.util.Scanner;

/**
 * Команда 'update'. Обновляет элемент коллекции.
 */
public class Update extends Command {
    private final CollectionManager collectionManager;

    public Update(CollectionManager collectionManager) {
        super("update", "обновить значение элемента коллекции по ID");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        try {
            if (request.getArgument().isBlank()) throw new IllegalArguments();

            long id = Long.parseLong(request.getArgument().trim());
            Product existingProduct = null;
            for (Product product : collectionManager.get()) {
                if (product.getId().equals(id)) {
                    existingProduct = product;
                    break;
                }
            }
            System.out.println("* Обновление продукта:");
            Scanner scanner = new Scanner(System.in); // Используем Scanner для ввода
            Product updatedProduct = Ask.askProduct(scanner);

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
                collectionManager.get().remove(existingProduct);
                collectionManager.get().add(newProduct);

                System.out.println("Продукт успешно обновлён!");
            }
        } catch (Ask.AskBreak e) {
            throw new RuntimeException(e);
        } catch (IllegalArguments e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}