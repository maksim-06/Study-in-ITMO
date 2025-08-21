package Server.commands;


import Common.network.requests.Request;
import Common.network.responses.Response;

/**
 * Интерфейс для всех выполняемых команд.
 */
public interface Executable {
    Response apply(Request request);
}