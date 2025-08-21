package pokemons;

import attack.*;
import ru.ifmo.se.pokemon.*;



public class Ralts extends Pokemon {
    public Ralts(){

        this("Безымянный", 1);
    }
    public Ralts(String name, int level){
        super(name,level);
        this.setType(Type.PSYCHIC, Type.FAIRY);
        this.setStats(28, 25,25,45,35,40);
        this.setMove(new ThunderWave(),new DisarmingVoice());
    }
}
