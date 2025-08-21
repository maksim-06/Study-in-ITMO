package Server.handler;


import Common.network.requests.Request;

import Common.network.responses.Response;
import Server.managers.CommandManager;

public class CommandHandler {
    private final CommandManager commandManager;

    public CommandHandler(CommandManager commandManager) {
        this.commandManager = commandManager;
    }


    public Response handle(Request request) {
        var command = commandManager.getCommands().get(request.getName());
        if (command != null) {
            try {
                return command.apply(request);
            } catch (Exception e) {
                System.out.println(e.getClass().getName());
            }
        }


        return new Response("Команды нет", "");
    }
}
