package Server.commands;

import Common.network.requests.Request;
import Common.network.responses.Response;
import Common.user.User;
import Server.managers.CollectionManager;
import Server.managers.DatabaseManager;

/**
 * Команда 'clear'. Очищает коллекцию пользователя.
 */
public class Clear extends Command {
    private final CollectionManager collectionManager;
    private final DatabaseManager databaseManager;

    public Clear(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("clear", "очистить коллекцию пользователя");
        this.collectionManager = collectionManager;
        this.databaseManager = databaseManager;
    }

    /**
     * Выполняет команду
     */
    @Override
    public Response apply(Request request) {
        try {
            User user = request.getUser();
            if (user == null) {
                return new Response("Ошибка: пользователь не авторизован", "");
            }

            int deleted = collectionManager.removeProductsByUser(user);
            if (deleted < 0) {
                return new Response("Ошибка при удалении данных из базы", "");
            }

            return new Response("Коллекция очищена. Удалено " + deleted + " объектов.", "");
        } catch (Exception e) {
            return new Response("Ошибка при очистке коллекции: " + e.getMessage(), "");
        }
    }

}
