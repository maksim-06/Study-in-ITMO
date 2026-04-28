import Configreader.exeption.InsufficientCrystalsException;
import Story.constant.Stone;
import Story.items.Crystal;
import Story.person.Cheburashka.Cheburashka;
import Story.person.Ponchik.Ponchik;
import Story.scena.Shore;


public class Main{
    public static void main(String[] args){
        Shore shore = new Shore();
        Ponchik ponchik = new Ponchik("Mak", "");
        Cheburashka cheburashka = new Cheburashka("Nek", "");
        System.out.println(Stone.COLOUR);

        for (Crystal crystal : shore.getCrystals()) {
            cheburashka.setCollectedCrystals(crystal);
            cheburashka.interactWithCrystal(crystal);
        }

        System.out.println(cheburashka.getName() + " " + cheburashka.spisok());

        for (Crystal crystal : shore.getCrystals()) {
            ponchik.setCollectedCrystals(crystal);
            ponchik.interactWithCrystal(crystal);
        }

        System.out.println(ponchik.getName() + " " + ponchik.spisok());

        if (ponchik.kolcrust() < 4 && cheburashka.kolcrust() > 4){
            if ((cheburashka.kolcrust() - (4 - ponchik.kolcrust())) >= 4){
                System.out.println(cheburashka.getName() + " " + cheburashka.spisok());
                System.out.println(cheburashka.getName() + " помогает " + ponchik.getName());
                ponchik.colladd(cheburashka);

                System.out.println(ponchik.getName() + " " + ponchik.spisok());

                cheburashka.del();

                System.out.println(cheburashka.getName() + " " + cheburashka.spisok());

                try {
                    ponchik.grindSalt();
                } catch (InsufficientCrystalsException e) {
                    System.out.println(ponchik.getName() + " ошибка: " + e.getMessage());
                }
            }
            else {
                try {
                    ponchik.grindSalt();
                } catch (InsufficientCrystalsException e) {
                    System.out.println(ponchik.getName() + " ошибка: " + e.getMessage());
                }
            }
        }
        else {
            try {
                ponchik.grindSalt();
            } catch (InsufficientCrystalsException e) {
                System.out.println(ponchik.getName() + " ошибка: " + e.getMessage());
            }
        }

        try {
            cheburashka.grindSalt();
        } catch (InsufficientCrystalsException e) {
            System.out.println(cheburashka.getName() + " ошибка: " + e.getMessage());
        }
    }
}