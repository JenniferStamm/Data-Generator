package de.datageneration.generator.placement.structures.levelCalculator;

import de.datageneration.generator.Configuration;

import java.util.Map;

public class RepetitiveBottomUpLevelCalculator extends LevelCalculator {

    public RepetitiveBottomUpLevelCalculator(Map<Integer, Integer> fDCount) {
        super(fDCount);
    }

    private int currentLevel = 0;

    @Override
    public int nextFDSize() {
        Configuration configuration = Configuration.getConfiguration();
        if (fDCount.size() == 0
                || (configuration.getCandidateSize() != null
                && fDCount.get(configuration.getCandidateSize()) == null)) {
            return configuration.getAmountOfAttributes();
        }

        if (configuration.getCandidateSize() != null) {
            return configuration.getCandidateSize();
        }

        while (currentLevel < configuration.getAmountOfAttributes()) {
            currentLevel++;
            if (currentLevel == configuration.getAmountOfAttributes()) {
                currentLevel = 1;
            }
            if (fDCount.get(currentLevel) != null) {
                return currentLevel;
            }
        }
        return 0;
    }
}
