package Server.managers;

import Server.commands.Command;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final CollectionManager collectionManager;

    public CommandManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
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
