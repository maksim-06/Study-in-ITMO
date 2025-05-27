package Common.domein;



import Client.utility.Validatable;

import java.io.Serializable;
import java.util.Objects;


public class Coordinates implements Validatable, Serializable {
    private long x;
    private Integer y;

    // Конструктор класса Coordinates
    public Coordinates(long x, Integer y){
        this.x=x;
        this.y=y;
    }

    // метод для проверки правильности полей
    @Override
    public boolean validate() {
        if (y == null) return false;
        return true;
    }

    // метод для определения равенства объектов
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o==null || getClass() != o.getClass()) return false;
        Coordinates coordinates = (Coordinates) o;
        return x==coordinates.x && Objects.equals(y,coordinates.y);
    }

    // методы для возврата полей
    public long getX(){
        return x;
    }

    public Integer getY(){
        return y;
    }

    // хэш-код объекта
    @Override
    public int hashCode() {
        return Objects.hash(x,y);
    }

    // возвращение строкового представления объекта
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
