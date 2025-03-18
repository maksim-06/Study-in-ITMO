package utility.console;


// Консоль для ввода команд и вывода результата
public interface Console {
    void print(Object obj);
    void println(Object obj);
    void printError(Object obj);
    void printTable(Object obj1, Object obj2);
    void ps1();
}