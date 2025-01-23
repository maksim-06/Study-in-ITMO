package pokemons;

import attack.*;
import ru.ifmo.se.pokemon.*;



public class Gardevoir extends Kirlia {
    public Gardevoir(){
        this("Безымянный", 1);
    }
    public Gardevoir(String name, int level){
        super(name,level);
        this.setType(Type.PSYCHIC, Type.FAIRY);
        this.setStats(68, 65, 65, 125, 115, 80);
        this.addMove(new FocusBlast());
    }
}
