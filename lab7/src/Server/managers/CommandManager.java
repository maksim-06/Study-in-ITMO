package Server.managers;

import Server.commands.Command;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private DatabaseManager databaseManager;

    public CommandManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void addCommand(Collection<Command> commands) {
        this.commands.putAll(commands.stream()
                .collect(Collectors.toMap(Command::getName, s -> s)));
    }

    /**
     * @return Словарь команд.
     */
    public Map<String, Command> getCommands() {
        return commands;
    }
}
