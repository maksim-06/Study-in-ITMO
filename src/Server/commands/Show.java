package Server.commands;

import Common.network.requests.Request;
import Common.network.responses.Response;
import Common.network.responses.ShowResponse;
import Server.managers.CollectionManager;

/**
 * Команда 'show'. Выводит все элементы коллекции.
 * @author maxbarsukov
 */
public class Show extends Command {
    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        super("show", "вывести все элементы коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        try {
            return new ShowResponse(collectionManager.sorted(), null);
        } catch (Exception e) {
            return new ShowResponse(null, e.toString());
        }
    }
}