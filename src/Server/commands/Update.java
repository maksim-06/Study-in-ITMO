package Server.commands;

import Common.domein.Product;
import Common.exception.IllegalArguments;
import Common.network.requests.Request;
import Common.network.responses.Response;
import Server.managers.CollectionManager;


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
            if (request.getArgument().isBlank() || request.getBody() == null) {
                throw new IllegalArguments();
            }

            long id = Long.parseLong(request.getArgument().trim());
            Product existing = collectionManager.getById(id);

            if (existing == null) {
                return new Response("Продукт с таким ID не найден.", "");
            }

            if (existing.getCreatorId() != request.getUser().getId()) {
                return new Response("Вы не можете обновить чужой продукт.", "");
            }

            boolean success = collectionManager.update(id, request.getBody(), request.getUser());
            if (success) {
                return new Response("Продукт успешно обновлён!", "");
            } else {
                return new Response("Не удалось обновить продукт.", "");
            }

        } catch (IllegalArguments e) {
            return new Response("Ошибка: необходимо указать ID и передать данные продукта.", "");
        } catch (NumberFormatException e) {
            return new Response("Ошибка: ID должен быть числом.", "");
        } catch (Exception e) {
            return new Response("Произошла ошибка: " + e.getMessage(), "");
        }
    }
}