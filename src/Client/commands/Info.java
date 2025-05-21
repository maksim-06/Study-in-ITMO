package Client.commands;

import Client.network.UPDClient;
import Client.utility.console.Console;
import Common.network.requests.InfoRequest;
import Common.network.responses.InfoResponse;

import java.io.IOException;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 */
public class Info extends Command {
    private final Console console;
    private final UPDClient client;

    public Info(Console console, UPDClient client) {
        super("info");
        this.console = console;
        this.client = client;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (!arguments[1].isEmpty()) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        try {
            var response = (InfoResponse) client.sendAndReceiveCommand(new InfoRequest());
            console.println(response.infoMessage);
            return true;
        } catch(IOException | ClassNotFoundException e) {
            console.printError("Ошибка взаимодействия с сервером");
        }
        return false;
    }
}