package attack;
import ru.ifmo.se.pokemon.*;

public class DisarmingVoice extends StatusMove{
    public DisarmingVoice(){
        super(Type.FAIRY,40,0);
    }
    protected String describe(){
        return "Использует Disarming Voice";
    }
}