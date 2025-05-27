package Server.commands;

import Common.network.requests.RemoveByIdRequest;
import Common.network.requests.Request;
import Common.network.responses.RemoveByIdResponse;
import Common.network.responses.Response;
import Server.managers.CollectionManager;

/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции.
 * @author maxbarsukov
 */
public class RemoveById extends Command {
    private final CollectionManager collectionManager;

    public RemoveById(CollectionManager collectionManager) {
        super("remove_by_id <ID>", "удалить элемент из коллекции по ID");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (RemoveByIdRequest) request;

        try {
            if (!collectionManager.checkExist(req.id)) {
                return new RemoveByIdResponse("Продукта с таким ID в коллекции нет!");
            }

            collectionManager.remove(req.id);
            return new RemoveByIdResponse(null);
        } catch (Exception e) {
            return new RemoveByIdResponse(e.toString());
        }
    }
}