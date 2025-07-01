package Server.managers;

public class DatabaseCommands {
    public static final String allTablesCreation = """ 
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                name VARCHAR(10) UNIQUE NOT NULL,
                password_digest VARCHAR(64) NOT NULL,
                salt VARCHAR(64) NOT NULL
            );
            
            CREATE TABLE IF NOT EXISTS organizations (
                id SERIAL PRIMARY KEY,
                name VARCHAR(255) NOT NULL CHECK(length(name) > 0),
                employees_count INTEGER NOT NULL CHECK (employees_count > 0),
                type organization_type,
                postal_address VARCHAR(255) CHECK(length(postal_address) > 0),
                creator_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
            );
            
            CREATE TABLE IF NOT EXISTS products (
                id SERIAL PRIMARY KEY,
                name VARCHAR(255) NOT NULL CHECK(length(name) > 0),
                x BIGINT NOT NULL,
                y INTEGER NOT NULL,
                creation_date DATE DEFAULT NOW() NOT NULL,
                price INT NOT NULL CHECK(price > 0),
                unit_of_measure unit_of_measure,
                manufacturer INT REFERENCES organizations(id) ON DELETE SET NULL,
                creator_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
            );
            """;

    public static final String allTYPECreation = """
    CREATE TYPE unit_of_measure AS ENUM (
                'CENTIMETERS',
                'SQUARE_METERS',
                'PCS',
                'GRAMS'
    );

    CREATE TYPE organization_type AS ENUM (
                'COMMERCIAL',
                'GOVERNMENT',
                'TRUST',
                'PRIVATE_LIMITED_COMPANY',
                'OPEN_JOINT_STOCK_COMPANY'
    );
    """;

    public static final String addUser = """
    INSERT INTO users(name, password_digest, salt) VALUES (?, ?, ?);
    """;

    public static final String getUser = """
    SELECT * FROM users WHERE name = ?;
    """;

    public static final String addProduct = """
            INSERT INTO products (name, x, y, price, unit_of_measure, manufacturer, creator_id)
            VALUES (?, ?, ?, ?, ?::unit_of_measure, ?, ?) RETURNING id
            """;

    public static final String addOrganization = """
        INSERT INTO organizations (name, employees_count, type, postal_address, creator_id)
        VALUES (?, ?, ?::organization_type, ?, ?) RETURNING id;
    """;

    public static final String update = """
        UPDATE products SET name=?, x=?, y=?, price=?, unit_of_measure=?::unit_of_measure
        WHERE id=? AND creator_id=?

    """;

    public static final String delete = """
        DELETE FROM products WHERE id=? AND creator_id=?
    """;


}
