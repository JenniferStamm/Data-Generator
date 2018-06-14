package de.datageneration.generator.placement.structures.levelCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomLevelCalculator extends LevelCalculator {
    public RandomLevelCalculator(Map<Integer, Integer> fDCount) {
        super(fDCount);
    }

    @Override
    public int nextFDSize() {
        List<Integer> sizes = new ArrayList<>(fDCount.keySet());

        Random rand = new Random();

        return sizes.get(rand.nextInt(sizes.size()));
    }
}
