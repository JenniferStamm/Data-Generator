package de.datageneration.generator.placement.structures.levelCalculator;

import de.datageneration.generator.Configuration;

import java.util.Map;

public class AbsoluteBottomUpLevelCalculator extends LevelCalculator {

    public AbsoluteBottomUpLevelCalculator(Map<Integer, Integer> fDCount) {
        super(fDCount);
    }

    @Override
    public int nextFDSize() {
        Configuration configuration = Configuration.getConfiguration();
        if (configuration.getCandidateSize() != null) {
            return  configuration.getCandidateSize();
        }

        int candidateSize = 0;
        while (fDCount.get(candidateSize) == null && candidateSize < configuration.getAmountOfAttributes()) {
            candidateSize++;
        }

        return candidateSize;
    }
}
