package Common.domein;





import Client.utility.Validatable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Organization implements Validatable, Serializable {
    private static Map<Integer, Organization> organizations = new HashMap<>();
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private int employeesCount; //Значение поля должно быть больше 0
    private OrganizationType type; //Поле может быть null
    private Address postalAddress; //Поле не может быть null

    // Конструктор класса Organization
    public Organization(Integer id, String name, int employeesCount,
                        OrganizationType type, Address postalAddress){
        this.id=id;
        this.name=name;
        this.employeesCount=employeesCount;
        this.type=type;
        this.postalAddress=postalAddress;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // Метод для проверки правильности полей
    @Override
    public boolean validate() {
        if (id == null || id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (employeesCount <= 0) return false;
        if (postalAddress == null) return false;
        return true;
    }

    // Метод для определения равенства объектов
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization organization = (Organization) o;
        return id == organization.id && Objects.equals(name,organization.name) &&
                Objects.equals(employeesCount, organization.employeesCount) &&
                Objects.equals(type,organization.type) && Objects.equals(postalAddress,organization.postalAddress);
    }

    // методы для возврата полей
    public Integer getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public int getEmployeesCount() {
        return employeesCount;
    }

    public OrganizationType getType() {
        return type;
    }

    public Address getPostalAddress() {
        return postalAddress;
    }

    // хэш-код объекта
    @Override
    public int hashCode() {
        return Objects.hash(id,name,employeesCount,type,postalAddress);
    }

    public static Map<Integer, Organization> allOrganizations() {
        return organizations;
    }

    // возвращение строкового представления объекта
    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", employeesCount=" + employeesCount +
                ", type=" + type +
                ", postalAddress=" + postalAddress +
                '}';
    }
}