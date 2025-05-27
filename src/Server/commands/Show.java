package Server.commands;

import Common.domein.Product;
import Common.network.requests.Request;
import Common.network.responses.Response;
import Server.managers.CollectionManager;

import java.util.Stack;

/**
 * Команда 'show'. Выводит все элементы коллекции.
 */
public class Show extends Command {
    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        super("show", "Выводит все элементы коллекции.");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response apply(Request request) {
        Stack<Product> collection = collectionManager.get();
        System.out.println(collection);
        if (collectionManager.get().isEmpty()) {
            return new Response("Коллекция пуста.", "");
        }
        return new Response("Коллекция: ", collection);
    }
}
