package Server.commands;

import Common.network.requests.Request;
import Common.network.responses.Response;
import Server.managers.CollectionManager;

/**
 * Команда 'clear'. Очищает коллекцию.
 */
public class Clear extends Command {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        try {
            collectionManager.clearCollection();
            return new Response("Коллекция очищена", "");
        } catch (Exception e) {
            return new Response("", "");
        }
    }
}