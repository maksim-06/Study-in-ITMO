package Server.commands;

import Common.network.requests.Request;
import Common.network.responses.Response;
import Server.managers.CollectionManager;

/**
* Команда 'remove_by_id'. Удаляет элемент из коллекции.
*/
public class RemoveById extends Command {
    private final CollectionManager collectionManager;

    public RemoveById(CollectionManager collectionManager) {
        super("remove_by_id", "удалить элемент из коллекции по ID");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        try {
            long id = Long.parseLong(request.getArgument());
            if (!collectionManager.checkExist(id)) {
                return new Response("Продукта с таким ID в коллекции нет!", "");
            }

            boolean success = collectionManager.remove(id, request.getUser());
            if (success) {
                return new Response("Продукт успешно удалён", "");
            } else {
                return new Response("Удаление отклонено: у вас нет прав на этот объект", "");
            }

        } catch (NumberFormatException e) {
            return new Response("Неверный формат ID", "");
        } catch (Exception e) {
            return new Response("Ошибка: " + e.getMessage(), "");
        }
    }
}