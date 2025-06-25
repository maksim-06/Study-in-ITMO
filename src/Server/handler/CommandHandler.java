package Server.handler;

import Common.network.requests.Request;
import Common.network.responses.Response;
import Server.managers.CommandManager;
import Server.managers.DatabaseManager;
import Common.user.User;

public class CommandHandler {
    private final CommandManager commandManager;
    private final DatabaseManager databaseManager;

    public CommandHandler(CommandManager commandManager, DatabaseManager databaseManager) {
        this.commandManager = commandManager;
        this.databaseManager = databaseManager;
    }

    public Response handle(Request request) {
        var command = commandManager.getCommands().get(request.getName());
        if (command != null) {
            try {
                User user = request.getUser();

                System.out.println("Проверка пользователя: " + (user != null ? user.getLogin() : "null"));
                boolean confirmed = databaseManager.confirmUser(user);
                System.out.println("Проверка пользователя завершена: " + confirmed);

                if (!confirmed) {
                    System.out.println("Пользователь не найден, регистрируем...");
                    databaseManager.registerUser(user);
                    System.out.println("Пользователь зарегистрирован с id=" + user.getId());
                }

                System.out.println("Выполнение команды: " + request.getName());
                Response response = command.apply(request);
                System.out.println("Команда выполнена успешно");

                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return new Response("Ошибка", "Внутренняя ошибка сервера.");
            }
        }
        return new Response("Ошибка", "Команда не найдена.");
    }
}
