package pokemons;

import attack.*;
import ru.ifmo.se.pokemon.*;


public class Tapulele extends Pokemon {
    public Tapulele(){
        this("Безымянный", 1);
    }
    public Tapulele(String name, int level){
        super(name,level);
        this.setType(Type.PSYCHIC, Type.FAIRY);
        this.setStats(70, 85,75,130, 115, 95);
        this.setMove(new BraveBird(),new CalmMind(),new QuickAttack(),new Screech());
    }
}




