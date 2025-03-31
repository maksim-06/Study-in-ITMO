package commands;

import managers.CommandManager;
import utility.console.Console;

/**
 * Команда 'history'. Вывыодит историю команд.
 */
public class History extends Command {
    private final Console console;
    private final CommandManager commandManager;

    public History(Console console, CommandManager commandManager) {
        super("history", "Вывыодит историю команд");
        this.console = console;
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (!arguments[1].isEmpty()) {
            console.println("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

            commandManager.getCommandHistory().forEach(command -> console.println(" " + command));
        return true;
    }
}
