package managers;


import models.*;
import utility.console.Console;

import java.io.*;
import java.time.LocalDate;
import java.util.*;


public class DumpManager {
    private final String fileName;
    private final Console console;

    public DumpManager(String fileName, Console console) {
        this.fileName = fileName;
        this.console = console;
    }

    /**
     * Записывает коллекцию в файл.
     * @param collection коллекция
     */
    public void writeCollection(Stack<Product> collection) {
        try (BufferedOutputStream collectionPrintWriter = new BufferedOutputStream(new FileOutputStream(fileName))) {
            for (Product product : collection) {
                collectionPrintWriter.write(convertProductToCsv(product).getBytes());
                collectionPrintWriter.write(System.lineSeparator().getBytes());
            }
            console.println("Коллекция успешно сохранена в файл!");
        } catch (IOException exception) {
            console.printError("Загрузочный файл не может быть открыт!");
        }
    }

    /**
     * Считывает коллекцию из файла.
     * @return Считанная коллекция
     */
    public Collection<Product> readCollection() {
        Stack<Product> collection = new Stack<>();
        console.println("Имя файла: " + fileName);
        if (fileName != null && !fileName.isEmpty()) {
            try (Scanner scanner = new Scanner(new File(fileName))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    Product product = parseCsvToProduct(line);
                    if (product != null) {
                        collection.add(product);
                    }
                }
                console.println("Коллекция успешно загружена!");
            } catch (FileNotFoundException exception) {
                console.printError("Загрузочный файл не найден!");
            } catch (NoSuchElementException exception) {
                console.printError("Загрузочный файл пуст!");
            }
        } else {
            console.printError("Аргумент командной строки с загрузочным файлом не найден!");
        }
        return collection;
    }

    /**
     * Преобразует объект Product в строку CSV.
     * @param product объект Product
     * @return строка в формате CSV
     */
    private String convertProductToCsv(Product product) {
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(String.valueOf(product.getId()))
                .add(product.getName())
                .add(String.valueOf(product.getCoordinates().getX()))
                .add(String.valueOf(product.getCoordinates().getY()))
                .add(product.getCreationDate().toString())
                .add(String.valueOf(product.getPrice()))
                .add(product.getUnitOfMeasure() != null ? product.getUnitOfMeasure().toString() : "null")
                .add(product.getManufacturer().getName())
                .add(String.valueOf(product.getManufacturer().getEmployeesCount()))
                .add(product.getManufacturer().getType() != null ? product.getManufacturer().getType().toString() : "null")
                .add(product.getManufacturer().getPostalAddress().getStreet() != null ? product.getManufacturer().getPostalAddress().getStreet() : "null");
        return joiner.toString();
    }

    /**
     * Преобразует строку CSV в объект Product.
     * @param csv строка в формате CSV
     * @return объект Product
     */
    private Product parseCsvToProduct(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 11) {
            console.printError("Ошибка! Недостаточно данных в строке CSV: " + csv);
            return null;
        }

        try {

            Long id = Long.parseLong(parts[0].trim());


            String name = parts[1].trim();
            if (name.isEmpty() || name.length() > 50 || !Character.isLetter(name.charAt(0))) {
                console.printError("Ошибка! Некорректное имя продукта: " + name);
                return null;
            }


            long x = Long.parseLong(parts[2].trim());
            int y = Integer.parseInt(parts[3].trim());
            Coordinates coordinates = new Coordinates(x, y);


            LocalDate creationDate = LocalDate.parse(parts[4].trim());


            int price = Integer.parseInt(parts[5].trim());
            if (price <= 0) {
                console.printError("Ошибка! Цена должна быть больше 0: " + price);
                return null;
            }


            UnitOfMeasure unitOfMeasure = parts[6].trim().equals("null") ? null : UnitOfMeasure.valueOf(parts[6].trim());


            String orgName = parts[7].trim();
            if (orgName.isEmpty() || orgName.length() > 50 || !Character.isLetter(orgName.charAt(0))) {
                console.printError("Ошибка! Некорректное имя организации: " + orgName);
                return null;
            }


            int employeesCount = Integer.parseInt(parts[8].trim());
            if (employeesCount <= 0) {
                console.printError("Ошибка! Количество сотрудников должно быть больше 0: " + employeesCount);
                return null;
            }


            OrganizationType type = parts[9].trim().equals("null") ? null : OrganizationType.valueOf(parts[9].trim());


            String street = parts[10].trim().equals("null") ? null : parts[10].trim();
            if (street != null && (street.length() > 50 || !Character.isLetter(street.charAt(0)))) {
                console.printError("Ошибка! Некорректное название улицы: " + street);
                return null;
            }
            Address address = new Address(street);


            Organization manufacturer = new Organization(employeesCount, orgName, employeesCount, type, address);


            return new Product(id, name, coordinates, creationDate, price, unitOfMeasure, manufacturer);
        } catch (NumberFormatException e) {
            console.printError("Ошибка! Неверный формат числовых данных в строке CSV: " + csv);
        } catch (Exception e) {
            console.printError("Ошибка при парсинге строки CSV: " + csv);
        }

        return null;
    }
}

