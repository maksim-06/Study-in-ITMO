package attack;
import ru.ifmo.se.pokemon.*;

public class FocusBlast extends StatusMove{
    public FocusBlast(){
        super(Type.FIGHTING,120,70);
    }
    protected String describe(){
        return "Использует Focus Blast";
    }

}