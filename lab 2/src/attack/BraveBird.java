package attack;
import ru.ifmo.se.pokemon.*;


public class BraveBird extends PhysicalMove{
    public BraveBird(){
        super(Type.FLYING,120,100);
    }
    protected String describe(){
        return "Использует Brave Bird";
    }

    protected void applySelfDamage(Pokemon att, double damage){
    }
}