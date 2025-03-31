package models;


import utility.Validatable;
import java.util.Objects;
import java.time.LocalDate;

public class Product implements Validatable {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int price; //Значение поля должно быть больше 0
    private UnitOfMeasure unitOfMeasure; //Поле может быть null
    private Organization manufacturer; //Поле не может быть null

    // Конструктор класса Product
    public Product(Long id, String name, Coordinates coordinates, LocalDate creationDate,
                   int price, UnitOfMeasure unitOfMeasure, Organization manufacturer){
        this.id = id;
        this.name = name;
        this.coordinates=coordinates;
        this.creationDate=creationDate;
        this.price=price;
        this.unitOfMeasure=unitOfMeasure;
        this.manufacturer=manufacturer;
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
                '}';
    }
}