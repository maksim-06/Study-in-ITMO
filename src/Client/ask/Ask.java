package Client.ask;

import java.time.LocalDate;
import java.util.*;

import Client.utility.Interrogator;
import Common.domein.*;



public class Ask {
    public static class AskBreak extends Exception {}

    public static Product askProduct(Scanner scanner) throws AskBreak {
        try {
            if (!Interrogator.isFileMode()) System.out.print("Введите имя продукта: ");
            String name = readValidatedString(scanner);

            Coordinates coordinates = askCoordinates(scanner);

            if (!Interrogator.isFileMode()) System.out.print("Введите цену (больше 0): ");
            int price = readPositiveInt(scanner);

            if (!Interrogator.isFileMode()) System.out.print("Введите единицу измерения (CENTIMETERS, SQUARE_METERS, PCS, GRAMS) или оставьте пустым: ");
            UnitOfMeasure unitOfMeasure = readUnitOfMeasure(scanner);

            Organization manufacturer = askOrganization(scanner);

            return new Product(generateId(), name, coordinates, LocalDate.now(), price, unitOfMeasure, manufacturer);
        } catch (NoSuchElementException | IllegalStateException e) {
            System.err.println("Ошибка чтения");
        }
        return null;
    }

    public static Coordinates askCoordinates(Scanner scanner) {
        try {
            if (!Interrogator.isFileMode()) System.out.print("Введите координату x: ");
            long x = readLong(scanner);

            if (!Interrogator.isFileMode()) System.out.print("Введите координату y: ");
            int y = readInteger(scanner);

            return new Coordinates(x, y);
        } catch (NoSuchElementException | IllegalStateException e) {
            System.err.println("Ошибка чтения");
            return null;
        }
    }

    public static Organization askOrganization(Scanner scanner) throws AskBreak {
        try {
            if (!Interrogator.isFileMode()) System.out.print("Введите имя организации: ");
            String name = readValidatedString(scanner);

            if (!Interrogator.isFileMode()) System.out.print("Введите количество сотрудников (больше 0): ");
            int employeesCount = readPositiveInt(scanner);

            if (!Interrogator.isFileMode()) System.out.print("Введите тип организации (COMMERCIAL, GOVERNMENT, TRUST, PRIVATE_LIMITED_COMPANY, OPEN_JOINT_STOCK_COMPANY) или оставьте пустым: ");
            OrganizationType type = readOrganizationType(scanner);

            if (!Interrogator.isFileMode()) System.out.print("Введите адрес (улица) или оставьте пустым: ");
            String street =  readOrganization(scanner);
            Address address = new Address(street);

            return new Organization(generateId().intValue(), name, employeesCount, type, address);
        } catch (NoSuchElementException | IllegalStateException e) {
            System.err.println("Ошибка чтения");
            return null;
        }
    }

    private static long readLong(Scanner scanner) {
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Ошибка! Введите корректное число: ");
            }
        }
    }

    private static int readInteger(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Ошибка! Введите корректное целое число: ");
            }
        }
    }

    private static int readPositiveInt(Scanner scanner) {
        int value;
        do {
            value = readInteger(scanner);
            if (value <= 0) {
                System.out.print("Ошибка! Число должно быть больше 0: ");
            }
        } while (value <= 0);
        return value;
    }

    private static UnitOfMeasure readUnitOfMeasure(Scanner scanner) {
        String input = scanner.nextLine().trim().toUpperCase();
        if (input.isEmpty()) return null;
        try {
            return UnitOfMeasure.valueOf(input);
        } catch (IllegalArgumentException e) {
            System.out.print("Ошибка! Неверный формат единицы измерения. Попробуйте снова: ");
            return readUnitOfMeasure(scanner);
        }
    }

    private static OrganizationType readOrganizationType(Scanner scanner) {
        String input = scanner.nextLine().trim().toUpperCase();
        if (input.isEmpty()) return null;
        try {
            return OrganizationType.valueOf(input);
        } catch (IllegalArgumentException e) {
            System.out.print("Ошибка! Неверный формат типа организации. Попробуйте снова: ");
            return readOrganizationType(scanner);
        }
    }

    private static String readOrganization(Scanner scanner) {
        String input;
        do {
            input = scanner.nextLine().trim();

            if (input.isEmpty()) return null; // Разрешаем пустой ввод

            if (input.length() > 50) {
                System.out.print("Ошибка! Длина не должна превышать 50 символов. Попробуйте снова: ");
                continue;
            }

            if (Character.isDigit(input.charAt(0))) {
                System.out.print("Ошибка! Улица не должна начинаться с цифры. Попробуйте снова: ");
            }

        } while (input.length() > 50 || Character.isDigit(input.charAt(0)));

        return input;
    }


    private static String readValidatedString(Scanner scanner) throws AskBreak {
        String input;
        do {
            input = scanner.nextLine().trim();
            if (input.equals("exit")) throw new AskBreak();
            if (input.isEmpty()) {
                System.out.print("Ошибка! Поле не может быть пустым. Попробуйте снова: ");
                continue;
            }
            if (input.length() > 50) {
                System.out.print("Ошибка! Длина не должна превышать 50 символов. Попробуйте снова: ");
                continue;
            }
            if (!Character.isLetter(input.charAt(0))) {
                System.out.print("Ошибка! Поле должно начинаться с буквы. Попробуйте снова: ");
            }

        } while (input.isEmpty() || input.length() > 50 || !Character.isLetter(input.charAt(0)));
        return input;
    }

    private static Long generateId() {
        return System.currentTimeMillis(); // Упрощённый способ генерации уникального ID
    }
}