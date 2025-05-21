package Server.commands;




import Common.network.requests.AddRequest;
import Common.network.requests.Request;
import Common.network.responses.AddResponse;
import Common.network.responses.Response;
import Server.managers.CollectionManager;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Add extends Command {
    private final CollectionManager collectionManager;
    public Add(CollectionManager collectionManager) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (AddRequest) request;
        try {
            if (!req.product.validate()) {
                return new AddResponse(-1, "Поля продукта не валидны! Продукт не добавлен!");
            }
            var newId = collectionManager.add(req.product);
            return new AddResponse(newId, null);
        } catch (Exception e) {
            return new AddResponse(-1, e.toString());
        }
    }

}
