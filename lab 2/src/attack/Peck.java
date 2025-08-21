package attack;
import ru.ifmo.se.pokemon.*;

public class Peck extends PhysicalMove{
    public Peck(){
        super(Type.FLYING,80,100);
    }
    protected String describe(){
        return "Использует Peck";
    }

}