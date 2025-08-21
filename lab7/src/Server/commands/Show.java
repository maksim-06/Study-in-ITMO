package Server.commands;

import Common.domein.Product;
import Common.network.requests.Request;
import Common.network.responses.Response;
import Server.managers.CollectionManager;

import java.util.ArrayList;
import java.util.List;

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
        List<Product> collection = collectionManager.get();
        System.out.println("Текущая коллекция: " + collection);
        if (collection.isEmpty()) {
            return new Response("Коллекция пуста.", "");
        }
        List<Product> productsList = new ArrayList<>(collection);
        return new Response("Коллекция: ", productsList);
    }
}
