package models;


import java.util.Objects;


public class Address {
    private String street;//Поле может быть null

    // Конструктор класса Address
    public Address(String street){
        this.street=street;
    }

    // Метод для определения равенства объектов
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return street == address.street;
    }

    // методы для возврата полей
    public String getStreet(){
        return street;
    }

    // хэш-код объекта
    @Override
    public int hashCode() {
        return Objects.hash(street);
    }

    // возвращение строкового представления объекта
    @Override
    public String toString() {
        return "ул. " + street;
    }
}