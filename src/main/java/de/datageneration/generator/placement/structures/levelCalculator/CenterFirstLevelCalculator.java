package de.datageneration.generator.placement.structures.levelCalculator;

import de.datageneration.generator.Configuration;
import de.datageneration.generator.Util;

import java.util.Map;

public class CenterFirstLevelCalculator extends LevelCalculator {

    public CenterFirstLevelCalculator(Map<Integer, Integer> fDCount) {
        super(fDCount);
    }

    @Override
    public int nextFDSize() {
        Configuration configuration = Configuration.getConfiguration();
        if (configuration.getCandidateSize() != null) {
            return configuration.getCandidateSize();
        }

        int upperLevel = (int) Math.ceil(configuration.getAmountOfAttributes() / 2.0);
        int lowerLevel = (int) Math.floor(configuration.getAmountOfAttributes() / 2.0);
        if (upperLevel == lowerLevel) {
            lowerLevel--;
        }
        boolean useUpperLevel = true;

        while (upperLevel <= configuration.getAmountOfAttributes() && lowerLevel > 0) {
            if (useUpperLevel) {
                upperLevel++;
                useUpperLevel = false;
                if (fDCount.getOrDefault(upperLevel - 1, 0) > 0) {
                    return upperLevel - 1;
                }
            } else {
                lowerLevel--;
                useUpperLevel = true;
                if (fDCount.getOrDefault(lowerLevel + 1, 0) > 0) {
                    return lowerLevel + 1;
                }
            }
        }
        return configuration.getAmountOfAttributes();
    }
}
