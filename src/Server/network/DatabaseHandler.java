package Server.network;


import Server.managers.DatabaseManager;

import java.util.Objects;

public class DatabaseHandler {
    private static DatabaseManager databaseManager;

    static {
        try {
            System.out.println("Статический блок DatabaseHandler: инициализация экземпляра DatabaseManager...");
            databaseManager = new DatabaseManager();
            System.out.println("Статический блок DatabaseHandler: экземпляр DatabaseManager успешно создан.");
        } catch (Exception e) {
            System.err.println("ОШИБКА: ошибка инициализации DatabaseManager. Сервер не может запуститься. Проверьте логи.");
            System.exit(1);
        }
    }

    public static DatabaseManager getDatabaseManager(){
        if (Objects.isNull(databaseManager)) {
            System.out.println("КРИТИЧЕСКАЯ ОШИБКА: вызвана функция getDatabaseManager(), но значение экземпляра DatabaseManager равно null.");
            throw new IllegalStateException("DatabaseManager не инициализирован или произошла ошибка инициализации.");
        }
        return databaseManager;
    }
}
