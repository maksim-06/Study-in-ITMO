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
        if (parts.length >= 11) {
            try {
                Long id = Long.parseLong(parts[0]);
                String name = parts[1];
                Coordinates coordinates = new Coordinates(Long.parseLong(parts[2]), Integer.parseInt(parts[3]));
                LocalDate creationDate = LocalDate.parse(parts[4]);
                int price = Integer.parseInt(parts[5]);
                UnitOfMeasure unitOfMeasure = parts[6].equals("null") ? null : UnitOfMeasure.valueOf(parts[6]);
                Organization manufacturer = new Organization(Integer.parseInt(parts[8]), parts[7], Integer.parseInt(parts[8]), parts[9].equals("null") ? null : OrganizationType.valueOf(parts[9]), new Address(parts[10].equals("null") ? null : parts[10]));
                return new Product(id, name, coordinates, creationDate, price, unitOfMeasure, manufacturer);
            } catch (Exception e) {
                console.printError("Ошибка при парсинге строки CSV: " + csv);
            }
        }
        return null;
    }
}

