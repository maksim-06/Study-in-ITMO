package Server.handler;


import Common.network.requests.Request;
import Common.network.responses.NoSuchCommandResponse;
import Common.network.responses.Response;
import Server.managers.CommandManager;

public class CommandHandler {
    private final CommandManager manager;

    public CommandHandler(CommandManager manager){
        this.manager = manager;
    }

    public Response handle(Request request){
        var command = manager.getCommands().get(request.getName());
        if (command == null) return new NoSuchCommandResponse(request.getName());
        return command.apply(request);
    }
}
