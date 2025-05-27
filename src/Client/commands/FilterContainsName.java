package Client.commands;


import Client.network.UPDClient;
import Client.utility.console.Console;
import Common.exception.APIException;
import Common.exception.WrongAmountOfElementsException;
import Common.network.requests.FilterContainsNameRequest;
import Common.network.responses.FilterContainsNameResponse;

import java.io.IOException;

/**
 * Команда 'filter_contains_name'. Фильтрация продуктов по полю name.
 */
public class FilterContainsName extends Command {
    private final Console console;
    private final UPDClient client;

    public FilterContainsName(Console console, UPDClient client) {
        super("filter_contains_name <NAME>");
        this.console = console;
        this.client = client;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        try {
            if (arguments[1].isEmpty()) throw new WrongAmountOfElementsException();

            var response = (FilterContainsNameResponse) client.sendAndReceiveCommand(
                    new FilterContainsNameRequest(arguments[1])
            );
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            if (response.filteredProducts.isEmpty()) {
                console.println("Продуктов, чьи имена содержат '" + arguments[1] + "' не обнаружено.");
                return true;
            }

            console.println("Продуктов, чьи имена содержат '" + arguments[1] + "' обнаружено " + response.filteredProducts.size() + " шт.\n");
            response.filteredProducts.forEach(console::println);

            return true;
        } catch (WrongAmountOfElementsException exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (APIException e) {
            console.printError(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}