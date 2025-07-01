package Client.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import Client.ask.Ask;
import Client.utility.Interrogator;
import Common.domein.Product;
import Common.network.requests.Request;
import Common.network.responses.Response;
import Client.network.UPDClient;
import Client.utility.console.Console;
import Common.exception.*;
import Common.user.User;

public class Runner {


    public enum ExitCode {
        OK,
        ERROR,
        EXIT,
    }

    private User user;
    private final Console console;
    private final UPDClient client;
    private final List<String> scriptStack = new ArrayList<>();


    public Runner(UPDClient client, Console console) {
        Interrogator.setUserScanner(new Scanner(System.in));
        this.client = client;
        this.console = console;
    }

    /**
     * Интерактивный режим
     */
    public void interactiveMode() {
        var ask = new Ask(console);
        if (user == null) {
            user = ask.askUser();
        }
        var userScanner = Interrogator.getUserScanner();
        try {
            ExitCode commandStatus;
            String[] userCommand;

            do {
                console.ps1();
                userCommand = (userScanner.nextLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                commandStatus = launchCommand(userCommand);
            } while (commandStatus != ExitCode.EXIT);

        } catch (NoSuchElementException exception) {
            console.printError("Пользовательский ввод не обнаружен!");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
        }
    }


    /**
     * Режим для запуска скрипта.
     * @param argument Аргумент скрипта
     * @return Код завершения.
     */
    public ExitCode scriptMode(String argument) {
        String[] userCommand;
        ExitCode commandStatus;
        scriptStack.add(argument);
        if (!new File(argument).exists()) {
            argument = "../" + argument;
        }
        try (Scanner scriptScanner = new Scanner(new File(argument))) {
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            Scanner tmpScanner = Interrogator.getUserScanner();
            Interrogator.setUserScanner(scriptScanner);
            Interrogator.setfilemod();

            do {
                userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                while (scriptScanner.hasNextLine() && userCommand[0].isEmpty()) {
                    userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                }
                console.println(String.join(" ", userCommand));
                if (userCommand[0].equals("execute_script")) {
                    for (String script : scriptStack) {
                        if (userCommand[1].equals(script)) throw new ScriptRecursionException();
                    }
                }
                commandStatus = launchCommand(userCommand);
            } while (commandStatus == ExitCode.OK && scriptScanner.hasNextLine());

            Interrogator.setUserScanner(tmpScanner);
            Interrogator.setUsermod();

            if (commandStatus == ExitCode.ERROR && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
                console.println("Проверьте скрипт на корректность введенных данных!");
            }

            return commandStatus;

        } catch (FileNotFoundException exception) {
            console.printError("Файл со скриптом не найден!");
        } catch (NoSuchElementException exception) {
            console.printError("Файл со скриптом пуст!");
        } catch (ScriptRecursionException exception) {
            console.printError("Скрипты не могут вызываться рекурсивно!");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
            System.exit(0);
        } finally {
            scriptStack.removeLast();
        }
        return ExitCode.ERROR;
    }

    private ExitCode launchCommand(String[] userCommand) {
        String commandName = userCommand[0];
        String argument = userCommand.length > 1 ? userCommand[1] : "";

        if (commandName.isEmpty()) return ExitCode.OK;

        if (commandName.equals("exit")) return ExitCode.EXIT;

        if (user == null) {
            console.printError("Вы не авторизованы.");
            return ExitCode.ERROR;
        }

        if (commandName.equals("execute_script")) return scriptMode(argument);

        if (commandName.equals("update")) {
            console.println("* Обновление продукта:");
            Scanner scanner = Interrogator.getUserScanner();
            try {
                Product product = Ask.askProduct(scanner);
                if (product == null || !product.validate()) throw new InvalidFormException();

                Response response = client.sendAndReceiveCommand(
                        new Request(commandName, argument, product, user));

                console.println(response.toString());
                return ExitCode.OK;

            } catch (Ask.AskBreak | InvalidFormException | IOException | ClassNotFoundException e) {
                console.printError("Ошибка при обновлении продукта: " + e.getMessage());
                return ExitCode.ERROR;
            }
        }


        if (commandName.equals("add")) {
            console.println("* Создание нового продукта:");
            Scanner scanner = Interrogator.getUserScanner();
            try {
                Product product = Ask.askProduct(scanner);
                if (product == null || !product.validate()) throw new InvalidFormException();
                Response response = client.sendAndReceiveCommand(
                        new Request(userCommand[0].trim(),
                                userCommand[1].trim(),
                                product, user));
                console.println(response.toString());
                return ExitCode.OK;

            } catch (Ask.AskBreak | InvalidFormException | IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                Response response = client.sendAndReceiveCommand(new Request(userCommand[0].trim(), userCommand[1].trim(), null, user));
                console.println(response.toString());
                return ExitCode.OK;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}



