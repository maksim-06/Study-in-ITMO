package Story.person.Cheburashka;

import Configreader.exeption.InsufficientCrystalsException;
import Story.constant.Condition;
import Story.items.Crystal;
import Story.person.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Cheburashka extends Person {
    private List<Crystal> collectCrystals;


    public Cheburashka(String name, String state){
        super(name, state);
        this.collectCrystals = new ArrayList<>();
    }

    public void setCollectedCrystals(Crystal crystal){
        System.out.println(name + " собрал " + crystal.size() + " кристал");
    }

    public void interactWithCrystal(Crystal crystal){
        System.out.println(name + " взаимодействует с " + crystal.size() + " кристал");
        if (new Random().nextBoolean()){
            collectCrystals.add(crystal);
            System.out.println(name + " облизал " + crystal.size() + " кристал" + " и нашел его вкусным!");
            System.out.println(name +  " " + Condition.HAPPY);
        }

        else {
            System.out.println(name + " не понравился " + crystal.size() + " кристал");
            System.out.println(name + " " + Condition.SAD);
        }
    }

    // метод который дает лишние кристаллы, возвращает список
    public List<Crystal> help(){
        return collectCrystals.subList(0, kolcrust() - 4);
    }

    // удаляет кристалл из списка
    public void del(){
        collectCrystals.subList(0, kolcrust() - 4).clear();
    }

    // выводит список кристаллов
    public List<Crystal> spisok(){
        return collectCrystals;
    }

    // выводит кол-во кристаллов
    public int kolcrust(){
        return collectCrystals.size();
    }

    public void grindSalt() throws InsufficientCrystalsException {
        if (collectCrystals.size() < 4) {
            throw new InsufficientCrystalsException("Недостаточно кристаллов для толчения соли.");
        }
        else {
            System.out.println(name + " измельчает соль из собранных кристаллов.");
            collectCrystals.clear();
        }
    }

    public String getName() {
        return this.name;
    }
}

