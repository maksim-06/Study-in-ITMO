package Story.person.Ponchik;

import Configreader.exeption.InsufficientCrystalsException;
import Story.constant.Condition;
import Story.constant.Stone;
import Story.items.Crystal;
import Story.person.Cheburashka.Cheburashka;
import Story.person.Person;

import java.util.ArrayList;
import java.util.List;


public class Ponchik extends Person {
    private List<Crystal> collectedCrystals;


    public Ponchik(String name, String state){
        super(name, state);
        this.collectedCrystals = new ArrayList<>();
    }

    public void setCollectedCrystals(Crystal crystal){
        System.out.println(name + " собрал " + crystal.size() + " кристал");
    }

    public void interactWithCrystal(Crystal crystal){
        System.out.println(name + " взаимодействует с " + crystal.size() + " кристал");
        if (crystal.size() == Stone.BIG) {
            collectedCrystals.add(crystal);
            System.out.println(name + " облизал " + crystal.size() + " кристал" + " и нашел его вкусным!");
            System.out.println(name + " " + Condition.HAPPY);
        }
        else {
            System.out.println(name + " не понравился " + crystal.size() + " кристал");
            System.out.println(name + " " + Condition.SAD);
        }
    }

    // выводит кол-во кристаллов
    public int kolcrust(){
        return collectedCrystals.size();
    }

    // выводит список кристаллов
    public List<Crystal> spisok(){
        return collectedCrystals;
    }

    // с помощью функции help в collectedCrystals добавляются недостающие кристаллы
    public boolean colladd(Cheburashka cheburashka){
        List<Crystal> pomosh = cheburashka.help();
        return collectedCrystals.addAll(pomosh);
    }

    public void grindSalt() throws InsufficientCrystalsException {
        if (collectedCrystals.size() < 4) {
            throw new InsufficientCrystalsException("Недостаточно кристаллов для толчения соли.");
        }
        else {
            System.out.println(name + " измельчает соль из собранных кристаллов.");
            collectedCrystals.clear();
        }
    }

    public String getName() {
        return this.name;
    }
}
