package attack;
import ru.ifmo.se.pokemon.*;

public class Screech extends SpecialMove{
    public Screech(){
        super(Type.NORMAL,0,85);
    }
    protected String describe(){
        return "Использует Screech";
    }
    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        super.applySelfEffects(pokemon);
    }
}