package attack;
import ru.ifmo.se.pokemon.*;

public class CalmMind extends SpecialMove{
    public CalmMind(){
        super(Type.PSYCHIC,0,0);
    }
    protected String describe(){
        return "Использует Calm Mind";
    }


    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        super.applySelfEffects(pokemon);
    }
}