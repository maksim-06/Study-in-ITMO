package Story.person;


import Story.items.Crystal;

import java.util.List;

public abstract class Person {
    protected String name;
    protected String state;

    public Person(String name, String state){
        this.name = name;
        this.state = state;
    }

    abstract public String getName();

    abstract public List<Crystal> spisok();

    abstract public int kolcrust();

    public abstract void interactWithCrystal(Crystal crystal);

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Person)) {
            return false;
        }
        Person person = (Person) obj;
        return this.name.equals(person.name);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
