package pokemons;


import attack.Charm;
import ru.ifmo.se.pokemon.*;


public class Kirlia extends Ralts {
    public Kirlia(){
        this("Безымянный", 1);
    }
    public Kirlia(String name, int level){
        super(name,level);
        this.setType(Type.PSYCHIC, Type.FAIRY);
        this.setStats(38, 35, 35, 65, 55, 50);
        this.setMove(new Charm());
    }
}
