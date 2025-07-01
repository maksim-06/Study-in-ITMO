package Server.commands;


import Common.network.requests.Request;
import Common.network.responses.Response;

/**
 * Команда 'execute_script'. Выполнить скрипт из файла.
 */
public class Execute_script extends Command {

    public Execute_script() {
        super("execute_script <file_name>", "исполнить скрипт из указанного файла");
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        String[] arguments = request.getArgument().split(" ", 2); // если аргумент содержит несколько частей
        if (arguments.length < 2 || arguments[1].isEmpty()) {
            System.out.println("Использование: '" + getName() + "'");
            return new Response(getName(), "Ошибка: не указан файл скрипта");
        }

        System.out.println("Выполнение скрипта '" + arguments[1] + "'...");
        return new Response(getName(), "Скрипт выполняется...");
    }

}