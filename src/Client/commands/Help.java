package Client.commands;


import Client.network.UPDClient;
import Client.utility.console.Console;
import Common.network.requests.HelpRequest;
import Common.network.responses.HelpResponse;

import java.io.IOException;

/**
 * Команда 'help'. Выводит справку по доступным командам
 */
public class Help extends Command {
    private final Console console;
    private final UPDClient client;

    public Help(Console console, UPDClient client) {
        super("help");
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
            var response = (HelpResponse) client.sendAndReceiveCommand(new HelpRequest());
            console.print(response.helpMessage);
            return true;
        } catch(IOException | ClassNotFoundException e) {
            console.printError("Ошибка взаимодействия с сервером");
        }
        return false;
    }
}