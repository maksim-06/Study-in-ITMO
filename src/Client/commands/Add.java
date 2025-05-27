package Client.commands;




import Client.ask.Ask;
import Client.network.UPDClient;
import Client.utility.Interrogator;
import Client.utility.console.Console;
import Common.domein.Product;
import Common.exception.APIException;
import Common.network.requests.AddRequest;
import Common.network.responses.AddResponse;

import java.io.IOException;
import java.util.Scanner;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Add extends Command {
    private final Console console;
    private final UPDClient client;

    public Add(Console console, UPDClient client) {
        super("add {element}");
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
            console.println("* Создание нового продукта:");
            Scanner scanner = Interrogator.getUserScanner();
            Product product = Ask.askProduct(scanner);

            var response = client.sendAndReceiveCommand(product);

            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            console.println("Новый продукт успешно добавлен!");
            return true;

        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (APIException e) {
            console.printError(e.getMessage());
        } catch (Ask.AskBreak | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
