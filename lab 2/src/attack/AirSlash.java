package attack;
import ru.ifmo.se.pokemon.*;

public class AirSlash extends StatusMove{
    public AirSlash(){
        super(Type.ELECTRIC,0,90);
    }

    protected String describe(){
        return "Использует Air Slash";
    }

    protected void applyOppEffects(Pokemon p){
        p.setCondition(new Effect().condition(Status.PARALYZE));
    }
}