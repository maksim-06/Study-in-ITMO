package Common.domein;




import Client.utility.Validatable;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.time.LocalDate;

public class Product implements Validatable, Serializable, Comparable<Product> {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int price; //Значение поля должно быть больше 0
    private UnitOfMeasure unitOfMeasure; //Поле может быть null
    private Organization manufacturer; //Поле не может быть null
    private int creatorId;

    // Конструктор класса Product
    public Product(Long id, String name, Coordinates coordinates, LocalDate creationDate,
                   int price, UnitOfMeasure unitOfMeasure, Organization manufacturer, int creatorId) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.unitOfMeasure = unitOfMeasure;
        this.manufacturer = manufacturer;
        this.creatorId = creatorId;
    }

    public Product copy(long id) {
        return new Product(id, this.name, this.coordinates, this.creationDate, this.price, this.unitOfMeasure, this.manufacturer, this.creatorId);
    }


    // Метод для проверки правильности полей
    @Override
    public boolean validate() {
        if (id == null || id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null) return false;
        if (creationDate == null) return false;
        if (price <= 0) return false;
        if (manufacturer == null) return false;
        return true;
    }

    // Метод для определения равенства объектов
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Objects.equals(name, product.name) &&
                Objects.equals(coordinates,product.coordinates) &&
                Objects.equals(creationDate, product.creationDate) &&
                Objects.equals(price, product.price) &&
                unitOfMeasure == product.unitOfMeasure &&
                Objects.equals(manufacturer, product.manufacturer);
    }

    public int getCreatorId() {
        return creatorId;
    }


    @Override
    public int compareTo(Product other) {
        return Long.compare(this.id, other.id);
    }

    // методы для возврата полей
    public Long getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public int getPrice(){
        return price;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public Organization getManufacturer() {
        return manufacturer;
    }

    // хэш-код объекта
    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, price, unitOfMeasure, manufacturer);
    }



    public static Product fromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");

        long coordX = rs.getLong("x");
        int coordYValue = rs.getInt("y");
        Integer coordY = rs.wasNull() ? null : coordYValue;
        Coordinates coordinates = new Coordinates(coordX, coordY);

        java.sql.Date sqlDate = rs.getDate("creation_date");
        LocalDate creationDate = sqlDate != null ? sqlDate.toLocalDate() : null;

        int price = rs.getInt("price");

        String unitStr = rs.getString("unit_of_measure");
        UnitOfMeasure unitOfMeasure = unitStr != null ? UnitOfMeasure.valueOf(unitStr) : null;

        int creatorId = rs.getInt("creator_id");
        if (rs.wasNull()) {
            creatorId = -1;
        }

        Integer orgId = rs.getInt("manufacturer");
        if (rs.wasNull()) {
            orgId = null;
        }

        Organization manufacturer = null;
        if (orgId != null) {
            String orgName = rs.getString("name");
            int employeesCount = rs.getInt("employees_count");

            String orgTypeStr = rs.getString("type");
            OrganizationType orgType = orgTypeStr != null ? OrganizationType.valueOf(orgTypeStr) : null;

            String street = rs.getString("postal_address");
            Address postalAddress = street != null ? new Address(street) : null;

            manufacturer = new Organization(orgId, orgName, employeesCount, orgType, postalAddress);
        }

        return new Product(id, name, coordinates, creationDate, price, unitOfMeasure, manufacturer, creatorId);
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    // возвращение строкового представления объекта
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", price=" + price +
                ", unitOfMeasure=" + unitOfMeasure +
                ", manufacturer=" + manufacturer +
                ", creatorId=" + creatorId +
                '}';
    }
}