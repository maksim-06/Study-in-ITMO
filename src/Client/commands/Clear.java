package Client.commands;

import Client.network.UPDClient;
import Client.utility.console.Console;
import Common.exception.APIException;
import Common.exception.WrongAmountOfElementsException;
import Common.network.requests.ClearRequest;
import Common.network.responses.ClearResponse;

import java.io.IOException;

/**
 * Команда 'clear'. Очищает коллекцию.
 */
public class Clear extends Command {
    private final Console console;
    private final UPDClient client;

    public Clear(Console console, UPDClient client) {
        super("clear");
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
            if (!arguments[1].isEmpty()) throw new WrongAmountOfElementsException();

            var response = (ClearResponse) client.sendAndReceiveCommand(new ClearRequest());
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            console.println("Коллекция очищена!");
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