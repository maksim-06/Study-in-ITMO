package attack;
import ru.ifmo.se.pokemon.*;

public class Roost extends SpecialMove{
    public Roost(){
        super(Type.FLYING,0,0);
    }
    protected String describe(){
        return "Использует Roost";
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        super.applySelfEffects(pokemon);
    }
}