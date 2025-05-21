package Server.commands;

import Common.network.requests.Request;
import Common.network.requests.UpdateRequest;
import Common.network.responses.Response;
import Common.network.responses.UpdateResponse;
import Server.managers.CollectionManager;

/**
 * Команда 'update'. Обновляет элемент коллекции.
 * @author maxbarsukov
 */
public class Update extends Command {
    private final CollectionManager collectionManager;

    public Update(CollectionManager collectionManager) {
        super("update <ID> {element}", "обновить значение элемента коллекции по ID");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (UpdateRequest) request;
        try {
            if (!collectionManager.checkExist(req.id)) {
                return new UpdateResponse("Продукта с таким ID в коллекции нет!");
            }
            if (!req.updatedProduct.validate()) {
                return new UpdateResponse( "Поля продукта не валидны! Продукт не обновлен!");
            }

            collectionManager.getById(req.id).update(req.updatedProduct);
            return new UpdateResponse(null);
        } catch (Exception e) {
            return new UpdateResponse(e.toString());
        }
    }
}