package pokemons;

import attack.*;
import ru.ifmo.se.pokemon.*;


public class Natu extends Pokemon{
    public Natu(){
        this("Безымянный", 1);
    }
    public Natu(String name, int level){
        super(name,level);
        this.setType(Type.PSYCHIC, Type.FLYING);
        this.setStats(40,50,45, 70,45,70);
        this.setMove(new ThunderWave(),new Roost(),new Peck());
    }

}