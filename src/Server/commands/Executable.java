package Server.commands;


import Common.network.requests.Request;
import Common.network.responses.Response;

/**
 * Интерфейс для всех выполняемых команд.
 */
public interface Executable {
    /**
     * Выполнить что-либо.
     * @param request запрос с данными для выполнения команды
     * @return результат выполнения
     */
    Response apply(Request request);
}