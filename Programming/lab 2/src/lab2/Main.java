package lab2;

import ru.ifmo.se.pokemon.*;
import pokemons.*;


public class Main {
    public static void main(String[] args){
        Battle b = new Battle();
        Pokemon p1 = new Tapulele("Tap", 1);
        Pokemon p2 = new Natu("Nat",2);
        Pokemon p3 = new Xatu("Xat", 3);
        Pokemon p4 = new Ralts("Ral",4);
        Pokemon p5 = new Kirlia("Kir", 5);
        Pokemon p6 = new Gardevoir("Gar",6);
        b.addAlly(p1);
        b.addAlly(p2);
        b.addAlly(p3);
        b.addFoe(p4);
        b.addFoe(p5);
        b.addFoe(p6);
        b.go();
    }
}




