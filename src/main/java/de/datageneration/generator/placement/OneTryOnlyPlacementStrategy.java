package de.datageneration.generator.placement;

import de.datageneration.generator.placement.structures.levelCalculator.CenterFirstLevelCalculator;
import de.datageneration.generator.placement.structures.levelIterator.LexicographicalIterator;
import de.datageneration.generator.placement.structures.FunctionalDependency;
import de.metanome.algorithms.hyfd.structures.FDTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OneTryOnlyPlacementStrategy extends PlacementStrategy {
    ArrayList<LexicographicalIterator> iteratorPerLevel;

    public OneTryOnlyPlacementStrategy() {
        this.levelCalculator = new CenterFirstLevelCalculator(fDCount);
    }

    @Override
    public List<FDTree> process() throws PlacementFailedException {
        iteratorPerLevel = new ArrayList<>();

        for(int i = 0 ; i<=configuration.getAmountOfAttributes(); i++) {
            iteratorPerLevel.add(new LexicographicalIterator(configuration.getAmountOfAttributes(), i));
        }

        while ((configuration.getCandidateSize() == null && fDCount.size() > 0)
                || (configuration.getCandidateSize() != null
                && fDCount.get(configuration.getCandidateSize()) != null
                && fDCount.get(configuration.getCandidateSize()) > 0)) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new PlacementFailedException("Interrupted");
                }
                placeNextFD();
        }

        return Collections.singletonList(functionalDependencies);
    }

    private void placeNextFD() throws PlacementFailedException {
        int candidateSize = levelCalculator.nextFDSize();

        LexicographicalIterator iterator = iteratorPerLevel.get(candidateSize);

        while(iterator.hasNext()) {
            FunctionalDependency fD = iterator.next();

            if (isValidFD(fD)) {
                return;
            }
        }
        throw new PlacementFailedException("No placement found.");
    }
}
