package pokemons;

import attack.*;
import ru.ifmo.se.pokemon.*;



public class Xatu extends Natu {
    public Xatu(){
        this("Безымянный", 1);
    }
    public Xatu(String name, int level){
        super(name,level);
        this.setType(Type.PSYCHIC, Type.FLYING);
        this.setStats(65, 75, 70, 95, 70, 95);
        this.setMove(new AirSlash());
    }
}
