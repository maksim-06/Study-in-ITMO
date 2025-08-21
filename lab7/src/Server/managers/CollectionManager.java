package Server.managers;




import Common.domein.Organization;
import Common.domein.Product;
import Common.user.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Оперирует коллекцией.
 */
public class CollectionManager {
    private DatabaseManager databaseManager;
    private Stack<Product> collection = new Stack<>();
    private final LocalDateTime initializationTime;
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock writeLock = lock.writeLock();
    Lock readLock = lock.readLock();


    public CollectionManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.initializationTime = LocalDateTime.now();
        loadCollectionFromDatabase();
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

    public long add(Product product, User user) {
        if (user == null) throw new SecurityException("Пользователь не авторизован");

        writeLock.lock();
        try {
            int id = databaseManager.add(product, user);
            if (id != -1) {
                Product dbProduct = product.copy(id);
                dbProduct.setCreatorId(user.getId());  // Указываем создателя
                collection.add(dbProduct);
                return id;
            } else {
                throw new RuntimeException("Не удалось добавить продукт в БД.");
            }
        } finally {
            writeLock.unlock();
        }
    }



    public void loadCollectionFromDatabase() {
        writeLock.lock();
        try {
            collection.clear();
            collection.addAll(databaseManager.getAllProducts());
            lastInitTime = LocalDateTime.now();
            System.out.println("Коллекция загружена из БД.");
        } finally {
            writeLock.unlock();
        }
    }




    /**
     * Удаляет элемент из коллекции.
     * @param id ID элемента для удаления.
     */
    public boolean remove(long id, User user) {
        if (user == null) {
            throw new SecurityException("Пользователь не авторизован");
        }
        writeLock.lock();
        try {
            boolean deleted = databaseManager.remove((int) id, user);
            if (deleted) {
                collection.removeIf(p -> p.getId() == id);
            }
            return deleted;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public String toString() {
        readLock.lock();
        try {
            if (collection.isEmpty()) return "Коллекция пуста!";
            StringBuilder info = new StringBuilder();
            for (Product product : collection) {
                info.append(product).append("\n\n");
            }
            return info.toString().trim();
        } finally {
            readLock.unlock();
        }
    }


    public List<Product> get() {
        readLock.lock();
        try {
            return new ArrayList<>(collection);
        } finally {
            readLock.unlock();
        }
    }

    public int removeProductsByUser(User user) {
        if (user == null) {
            throw new SecurityException("Пользователь не авторизован");
        }

        writeLock.lock();
        try {
            // Удаляем из базы данных
            int deleted = databaseManager.clearProductsByUser(user);

            if (deleted > 0) {
                // Удаляем из коллекции все продукты пользователя
                collection.removeIf(p -> p.getCreatorId() == user.getId());
            }

            return deleted;
        } finally {
            writeLock.unlock();
        }
    }

    public boolean checkExist(long id) {
        readLock.lock();
        try {
            return collection.stream().anyMatch(p -> p.getId() == id);
        } finally {
            readLock.unlock();
        }
    }

    public boolean update(long id, Product newProduct, User user) {
        writeLock.lock();
        try {
            Product existing = getById(id);
            if (existing == null || existing.getCreatorId() != user.getId()) {
                return false;
            }

            Product toUpdate = newProduct.copy(id);
            toUpdate.setCreatorId(user.getId());

            if (databaseManager.update(toUpdate, user)) {
                collection.remove(existing);
                collection.add(toUpdate);
                return true;
            }

            return false;
        } finally {
            writeLock.unlock();
        }
    }

}