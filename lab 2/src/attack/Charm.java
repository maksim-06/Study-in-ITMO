package attack;
import ru.ifmo.se.pokemon.*;


public class Charm extends SpecialMove{
    public Charm(){
        super(Type.FAIRY,0,100);
    }
    protected String describe(){
        return "Использует Charm";
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        super.applySelfEffects(pokemon);
    }
}