package Client.utility.console;


// Консоль для ввода команд и вывода результата
public interface Console {
    void print(Object obj);
    void println(Object obj);
    void printError(Object obj);
    void ps1();
}