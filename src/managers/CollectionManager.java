package managers;

import models.Organization;
import models.Product;
import utility.console.Console;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Stack;

/**
 * Оперирует коллекцией.
 */
public class CollectionManager {
    private Stack<Product> collection = new Stack<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final DumpManager dumpManager;

    public CollectionManager(DumpManager dumpManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.dumpManager = dumpManager;

        loadCollection();
    }

    public void validateAll(Console console) {
            Organization.allOrganizations().values().forEach(organization -> {
                if (!organization.validate()) {
                    console.printError("Организация с id=" + organization.getId() + " имеет невалидные поля.");
                }
        });

        collection.forEach(product -> {
            if (!product.validate()) {
                console.printError("Продукт с id=" + product.getId() + " имеет невалидные поля.");
            }
        });
        console.println("! Загруженные продукты валидны.");
    }

    // Метод для поиска продукта по ID
    public Product byId(long id) {
        for (Product product : collection) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null; // Если продукт с таким ID не найден
    }

    // Метод для удаления продукта по ID
    public void remove(long id) {
        Iterator<Product> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getId() == id) {
                iterator.remove(); // Удаляем продукт из коллекции
                return;
            }
        }
    }

    public void removeFirst() {
        if (!collection.isEmpty()) {
            collection.remove(0); // Удаляем первый элемент из коллекции
        }
    }

    public Stack<Product> getCollection() {
        return collection;
    }

    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    public String collectionType() {
        return collection.getClass().getName();
    }

    public int collectionSize() {
        return collection.size();
    }

    public void addToCollection(Product element) {
        collection.push(element);
    }

    public void clearCollection() {
        collection.clear();
    }

    public void saveCollection() {
        dumpManager.writeCollection(collection);
        lastSaveTime = LocalDateTime.now();
    }

    private void loadCollection() {
        collection = (Stack<Product>) dumpManager.readCollection();
        lastInitTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";

        StringBuilder info = new StringBuilder();
        for (Product product : collection) {
            info.append(product).append("\n\n");
        }
        return info.toString().trim();
    }
}