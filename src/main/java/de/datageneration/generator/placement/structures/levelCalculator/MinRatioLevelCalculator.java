package de.datageneration.generator.placement.structures.levelCalculator;

import de.datageneration.generator.Configuration;
import de.datageneration.generator.Util;

import java.util.HashMap;
import java.util.Map;

public class MinRatioLevelCalculator extends LevelCalculator {
    Map<Integer, Float> ratioPerLevel;

    public MinRatioLevelCalculator(Map<Integer, Integer> fDCount) {
        super(fDCount);
        calculateRatioPerLevel();
    }

    private void calculateRatioPerLevel() {
        Configuration configuration = Configuration.getConfiguration();
        ratioPerLevel = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : configuration.getFDHistogram().entrySet()) {
            Integer requestedFDCount = entry.getValue();
            Integer level = entry.getKey();
            ratioPerLevel.put(level, Float.valueOf(requestedFDCount) / Util.numFDsInLevel(Configuration.getConfiguration().getAmountOfAttributes(), level));
        }
    }

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

        Integer minLevel = 0;
        Float minRatio = 1.f;
        for (Integer level : fDCount.keySet()) {
            Float ratio = ratioPerLevel.get(level);
            if (ratio <= minRatio) {
                minLevel = level;
                minRatio = ratio;
            }
        }
        return minLevel;
    }

}
