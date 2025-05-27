package Server.managers;




import Common.domein.Organization;
import Common.domein.Product;

import java.time.LocalDateTime;
import java.util.*;


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

    public void validateAll() {
            Organization.allOrganizations().values().forEach(organization -> {
                if (!organization.validate()) {
                    System.out.println("Организация с id=" + organization.getId() + " имеет невалидные поля.");
                }
        });

        collection.forEach(product -> {
            if (!product.validate()) {
                System.out.println("Продукт с id=" + product.getId() + " имеет невалидные поля.");
            }
        });
        System.out.println("! Загруженные продукты валидны.");
    }



    /**
     * Сохраняет коллекцию в файл
     */
    public void save() {
        dumpManager.writeCollection(collection);
        lastSaveTime = LocalDateTime.now();
    }

    /**
     * @return Имя типа коллекции.
     */
    public String type() {
        return collection.getClass().getName();
    }

    /**
     * @param id ID элемента.
     * @return Элемент по его ID или null, если не найдено.
     */
    public Product getById(long id) {
        for (Product element : collection) {
            if (element.getId() == id) return element;
        }
        return null;
    }

    /**
     * @param id ID элемента.
     * @return Проверяет, существует ли элемент с таким ID.
     */
    public boolean checkExist(long id) {
        return getById(id) != null;
    }

    /**
     * @return Размер коллекции.
     */
    public int size() {
        return collection.size();
    }

    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    public void clearCollection() {
        collection.clear();
    }




    public long add(Product element) {
        var maxId = collection.stream().filter(Objects::nonNull)
                .map(Product::getId)
                .mapToLong(Long::longValue).max().orElse(0);
        var newId = maxId + 1;

        var nextOrgId = collection.stream()
                .map(Product::getManufacturer)
                .filter(Objects::nonNull)
                .map(Organization::getId)
                .mapToInt(Integer::intValue).max().orElse(0) + 1;

        if (element.getManufacturer() != null) {
            element.getManufacturer().setId(nextOrgId);
        }
        collection.add(element.copy(newId));
        return newId;
    }


    /**
     * Удаляет элемент из коллекции.
     * @param id ID элемента для удаления.
     */
    public void remove(long id) {
        collection.removeIf(product -> product.getId() == id);
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

    /**
     * @return коллекция.
     */
    public Stack<Product> get() {
        return collection;
    }

    private void loadCollection() {
        collection = (Stack<Product>) dumpManager.readCollection();
        lastInitTime = LocalDateTime.now();
    }

}