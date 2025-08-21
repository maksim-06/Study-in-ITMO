package Server.managers;

import Common.domein.Organization;
import Common.domein.Product;
import Common.user.User;
import Server.Server;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class DatabaseManager {
    private Connection connection;
    private AuthManager authManager;
    private static final String PEPPER = "d&5qf]";


    public DatabaseManager() {
            this.connection = this.connectToDatabase();
            this.authManager = new AuthManager(this.connection, PEPPER);
            this.createTypes();
            this.createMainBase();
    }


    public Connection connectToDatabase() {
        Properties info = new Properties();
        try (FileInputStream fis = new FileInputStream(Server.DATABASE_CONFIG_PATH)) {
            info.load(fis);
            System.out.println("Попытка подключения к основной БД...");
            return DriverManager.getConnection(Server.DATABASE_URL, info);
        } catch (Exception e) {
            System.out.println("Подключение к основной БД не удалось: " + e.getMessage());
            try {
                System.out.println("Попытка подключения к HELIOS...");
                return DriverManager.getConnection(Server.DATABASE_URL_HELIOS, info);
            } catch (SQLException ex) {
                System.out.println("Подключение к HELIOS не удалось: " + ex.getMessage());
                ex.printStackTrace();
                System.exit(1);
            }
        }
        return null;
    }

    public void createTypes() {
        if (connection == null) {
            System.err.println("Connection is null!");
            return;
        }

        String[] commands = DatabaseCommands.allTYPECreation.split("(?<=;)\n");

        for (String command : commands) {
            String trimmed = command.trim();
            if (!trimmed.isEmpty()) {
                try (PreparedStatement stmt = connection.prepareStatement(trimmed)) {
                    stmt.execute();
                    System.out.println("Выполнено: " + trimmed.split("\n")[0]);
                } catch (SQLException e) {
                    // Игнорируем ошибку, если тип уже существует
                    if (e.getMessage().contains("already exists")) {
                        System.out.println("Тип уже существует, пропускаем: " + trimmed.split("\n")[0]);
                    } else {
                        System.err.println("Ошибка при выполнении команды: " + trimmed);
                        e.printStackTrace();
                    }
                }
            }
        }
    }



    public void createMainBase() {
        if (connection == null) {
            System.err.println("Connection is null!");
            return;
        }

        String[] commands = DatabaseCommands.allTablesCreation.split("(?<=;)\n"); // разделение по ; и новой строке
        for (String command : commands) {
            String trimmed = command.trim();
            if (!trimmed.isEmpty()) {
                try (PreparedStatement stmt = connection.prepareStatement(trimmed)) {
                    stmt.execute();
                    System.out.println("Выполнено: " + trimmed.split("\n")[0]);
                } catch (SQLException e) {
                    System.err.println("Ошибка при выполнении команды: " + trimmed);
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Таблицы созданы");
    }

    public int add(Product product, User user) {
        try {
            connection.setAutoCommit(false);

            Integer orgId = null;
            if (product.getManufacturer() != null) {
                orgId = addOrganization(product.getManufacturer(), user);
            }

            String sql = "INSERT INTO products (name, x, y, price, unit_of_measure, manufacturer, creator_id) " +
                    "VALUES (?, ?, ?, ?, ?::unit_of_measure, ?, ?) RETURNING id";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, product.getName());
                stmt.setLong(2, product.getCoordinates().getX());
                stmt.setInt(3, product.getCoordinates().getY());
                stmt.setInt(4, product.getPrice());

                if (product.getUnitOfMeasure() != null) {
                    stmt.setString(5, product.getUnitOfMeasure().name());
                } else {
                    stmt.setNull(5, Types.VARCHAR);
                }

                if (orgId != null) {
                    stmt.setInt(6, orgId);
                } else {
                    stmt.setNull(6, Types.INTEGER);
                }

                stmt.setInt(7, user.getId());

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int productId = rs.getInt("id");
                    connection.commit();
                    System.out.println("Добавлен продукт с id=" + productId);
                    return productId;
                } else {
                    connection.rollback();
                }
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении продукта: " + e.getMessage());
            e.printStackTrace();
            try {
                if (!connection.getAutoCommit()) connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return -1;
    }



    public int addOrganization(Organization organization, User user) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DatabaseCommands.addOrganization)) {
            stmt.setString(1, organization.getName());
            stmt.setInt(2, organization.getEmployeesCount());

            if (organization.getType() != null) {
                stmt.setString(3, organization.getType().name());
            } else {
                stmt.setNull(3, Types.OTHER);
            }

            stmt.setString(4, organization.getPostalAddress().getStreet());
            stmt.setInt(5, user.getId());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                System.out.println("Добавлена организация с id=" + id);
                return id;
            }
        }

        throw new SQLException("Не удалось добавить организацию");
    }



    public boolean update(Product product, User user) {

        try (PreparedStatement stmt = connection.prepareStatement(DatabaseCommands.update)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getCoordinates().getX());
            stmt.setDouble(3, product.getCoordinates().getY());
            stmt.setFloat(4, product.getPrice());

            if (product.getUnitOfMeasure() != null) {
                stmt.setString(5, product.getUnitOfMeasure().toString());
            } else {
                stmt.setNull(5, java.sql.Types.VARCHAR);
            }

            stmt.setInt(6, Math.toIntExact(product.getId()));
            stmt.setInt(7, user.getId());

            System.out.println("Обновляем продукт: id=" + product.getId() + ", creator_id=" + user.getId());


            int affected = stmt.executeUpdate();
            System.out.println("Обновление продукта id=" + product.getId() + ", затронуто записей: " + affected);
            return affected > 0;
        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении продукта: " + e.getMessage());
        }
        return false;
    }

    public void registerUser(User user) throws SQLException {
        int id = authManager.registerUser(user.getLogin(), user.getPassword());
        user.setId(id);
    }

    public boolean confirmUser(User user) {
        try {
            int id = authManager.authenticateUser(user.getLogin(), user.getPassword());
            if (id != -1) {
                user.setId(id);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при аутентификации: " + e.getMessage());
        }
        return false;
    }

    public boolean remove(int id, User user) {
        try (PreparedStatement stmt = connection.prepareStatement(DatabaseCommands.delete)) {
            stmt.setInt(1, id);
            stmt.setInt(2, user.getId());
            int affected = stmt.executeUpdate();
            System.out.println("Удалено: " + affected + " продуктов");
            return affected > 0;
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении продукта: " + e.getMessage());
        }
        return false;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products " +
                "JOIN users ON products.creator_id = users.id " +
                "LEFT JOIN organizations ON products.manufacturer = organizations.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Product p = Product.fromResultSet(rs);
                products.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при загрузке коллекции из БД: " + e.getMessage());
        }
        return products;
    }

    public int clearProductsByUser(User user) {
        String sql = "DELETE FROM products WHERE creator_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            int affected = stmt.executeUpdate();
            System.out.println("Удалено продуктов: " + affected);
            return affected;
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении: " + e.getMessage());
            return -1;
        }
    }
}
