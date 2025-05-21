package Client.commands;



import Client.ask.Ask;
import Client.network.UPDClient;
import Client.utility.console.Console;
import Common.domein.Product;
import Common.exception.APIException;
import Common.network.requests.UpdateRequest;
import Common.network.responses.UpdateResponse;

import java.io.IOException;
import java.util.Scanner;

/**
 * Команда 'update'. Обновляет элемент коллекции.
 */
public class Update extends Command {
    private final Console console;
    private final UPDClient client;

    public Update(Console console, UPDClient client) {
        super("update <ID> {element}");
        this.console = console;
        this.client = client;
    }

    @Override
    public boolean apply(String[] arguments) {
        try {
            if (arguments.length != 2) {
                console.printError("Неправильное количество аргументов!");
                console.println("Использование: '" + getName() + "'");
                return false;
            }

            long id;
            try {
                id = Long.parseLong(arguments[1].trim());
            } catch (NumberFormatException e) {
                console.printError("ID должен быть числом.");
                return false;
            }

            console.println("* Обновление продукта:");
            Scanner scanner = new Scanner(System.in);
            Product updatedProduct = Ask.askProduct(scanner);
            var response = (UpdateResponse) client.sendAndReceiveCommand(new UpdateRequest(id, updatedProduct));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }
            console.println("Продукт успешно обновлен.");
            return true;
        } catch (NumberFormatException exception) {
            console.printError("ID должен быть представлен числом!");
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (APIException e) {
            console.printError(e.getMessage());
        } catch (Ask.AskBreak e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}

