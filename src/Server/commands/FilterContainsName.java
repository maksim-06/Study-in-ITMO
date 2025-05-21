package Server.commands;


import Common.domein.Product;
import Common.network.requests.FilterContainsNameRequest;
import Common.network.requests.Request;
import Common.network.responses.FilterContainsNameResponse;
import Common.network.responses.Response;
import Server.managers.CollectionManager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда 'filter_contains_name'. Фильтрация продуктов по полю name.
 */
public class FilterContainsName extends Command {
    private final CollectionManager collectionManager;

    public FilterContainsName(CollectionManager collectionManager) {
        super("filter_contains_name <NAME>", "вывести элементы, значение поля name которых содержит заданную подстроку");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (FilterContainsNameRequest) request;
        try {
            return new FilterContainsNameResponse(filterByName(req.name), null);
        } catch (Exception e) {
            return new FilterContainsNameResponse(null, e.toString());
        }
    }

    private List<Product> filterByName(String nameSubstring) {
        return collectionManager.get().stream()
                .filter(product -> (product.getName() != null && product.getName().contains(nameSubstring)))
                .sorted()
                .collect(Collectors.toList());
    }
}