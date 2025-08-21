package Client.utility.console;



public class StandardConsole implements Console{
    private String ps1 = "$ ";

    // для вывода в консоль
    @Override
    public void print(Object obj) {
        System.out.print(obj);
    }

    // для вывода в консоль + \n
    @Override
    public void println(Object obj) {
        System.out.println(obj);
    }

    // для вывода ошибок в консоль
    @Override
    public void printError(Object obj) {
        System.out.println("Ошибка " + obj);
    }

    // для вывода "$ " в консоль
    @Override
    public void ps1() {
        print(ps1);
    }
}