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
            if (!collectionManager.checkExist(Long.parseLong(request.getArgument()))) {
                return new Response("Продукта с таким ID в коллекции нет!", "");
            }

            collectionManager.remove(Long.parseLong(request.getArgument()));
            return new Response("Удален", "");
        } catch (Exception e) {
            return new Response(e.toString(), "");
        }
    }
}