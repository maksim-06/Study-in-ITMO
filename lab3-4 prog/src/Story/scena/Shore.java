package Story.scena;

import Story.constant.Stone;
import Story.items.Crystal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Shore {
    private List<Crystal> crystals;

    public Shore(){
        this.crystals = new ArrayList<>();
        generateCrystals();
    }
    private void generateCrystals() {
        Stone[] sizes = {Stone.BIG, Stone.AVERAGE, Stone.SMALL};
        Random random = new Random();
        for (int i = 0; i < 9; i++){
            crystals.add(new Crystal(sizes[random.nextInt(sizes.length)]));
        }
    }

    public List<Crystal> getCrystals() {
        return crystals;
    }
}
