package de.datageneration.generator.placement.structures.levelCalculator;

import de.datageneration.generator.Configuration;
import de.datageneration.generator.Util;
import de.datageneration.generator.placement.BruteForcePlacementStrategy;

import java.util.Map;

/* Strategy Pattern */
public abstract class LevelCalculator {
    protected Map<Integer, Integer> fDCount;

    public LevelCalculator(Map<Integer, Integer> fDCount) {
        this.fDCount = fDCount;
    }

    public abstract int nextFDSize();

    public boolean enoughFDsLeft(BruteForcePlacementStrategy strategy) {
        Configuration configuration = Configuration.getConfiguration();
        for (int level: fDCount.keySet()) {
            if ((configuration.getCandidateSize() == null || configuration.getCandidateSize() == level)
                    && fDCount.get(level)
                    > Util.numFDsInLevel(configuration.getAmountOfAttributes(), level) - strategy.illegalFDsManager.getAmountOfIllegalFDs(level)) {
                return false;
            }
        }
        return true;
    }
}
