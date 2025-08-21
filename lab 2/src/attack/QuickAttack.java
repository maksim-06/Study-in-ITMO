package attack;
import ru.ifmo.se.pokemon.*;

public class QuickAttack extends PhysicalMove{
    public QuickAttack(){
        super(Type.NORMAL,40,100);
    }
    protected String describe(){
        return "Использует QuickAttack";
    }

}