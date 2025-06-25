package Server.commands;

import Common.network.requests.Request;
import Common.network.responses.Response;
import Server.managers.CollectionManager;


public class Add extends Command {
    private final CollectionManager collectionManager;

    public Add(CollectionManager collectionManager) {
        super("add", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response apply(Request request) {
        try {
            var product = request.getBody();
            var user = request.getUser();

            product.setCreatorId(user.getId());

            collectionManager.add(product, user);
            return new Response(request.getName(), "Продукт успешно добавлен!");
        } catch (Exception e) {
            System.err.println("Неизвестная ошибка при добавлении продукта: " + e.getMessage());
            return new Response(request.getName(), "Ошибка: Неизвестная ошибка. " + e.getMessage());
        }
    }
}
