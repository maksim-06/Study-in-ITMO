package utility;


import java.util.Scanner;


// Отвечает за режим ввода пользовательских данных
public class Interrogator{
    private static Scanner userScanner;
    private static boolean filemod = false;

    public static Scanner getUserScanner() {
        return userScanner;
    }

    public static void setUserScanner(Scanner userScanner) {
        Interrogator.userScanner = userScanner;
    }

    public static void setUsermod(){
        Interrogator.filemod = false;
    }

    public static void setfilemod(){
        Interrogator.filemod = true;
    }
}



