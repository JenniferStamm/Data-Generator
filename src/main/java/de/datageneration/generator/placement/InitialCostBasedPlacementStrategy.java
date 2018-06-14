package de.datageneration.generator.placement;

import de.datageneration.generator.placement.structures.levelIterator.LexicographicalIterator;
import de.datageneration.generator.placement.structures.FunctionalDependency;
import de.metanome.algorithms.hyfd.structures.FDTree;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class InitialCostBasedPlacementStrategy extends PlacementStrategy {
    ArrayList<LexicographicalIterator> iteratorPerLevel;
    HashMap<Integer, Long> initialCostPerLevel;


    @Override
    public List<FDTree> process() throws PlacementFailedException {
        iteratorPerLevel = new ArrayList<>();

        for(int i = 0 ; i <= configuration.getAmountOfAttributes(); i++) {
            iteratorPerLevel.add(new LexicographicalIterator(configuration.getAmountOfAttributes(), i));
        }

        initializeCostList();

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

    private void initializeCostList() {
        initialCostPerLevel = new HashMap<>();
        for (int level = 1; level < configuration.getAmountOfAttributes(); level++) {
            long amountOfInvalidations = invalidatedSpecializations(level) + invalidatedGeneralizations(level);
            initialCostPerLevel.put(level, amountOfInvalidations);
        }
    }

    public long invalidatedSpecializations(int lhsSize) {
        long result = 0L;
        for (int i = 0; i <= (configuration.getAmountOfAttributes() - (lhsSize + 2)); i++) {
            long relevantNodes = CombinatoricsUtils.binomialCoefficient(configuration.getAmountOfAttributes() - lhsSize - 1, i);
            long lhsSupersets = CombinatoricsUtils.binomialCoefficient(configuration.getAmountOfAttributes() - lhsSize - 1, i + 1);

            result += ((configuration.getAmountOfAttributes() - (lhsSize + 1 + i)) * relevantNodes) + lhsSupersets;
        }

        return result;
    }

    public long invalidatedGeneralizations(int lhsSize) {
        long result = 0L;
        for (int i = 0; i <= lhsSize - 2; i++) {
            long relevantNodes = CombinatoricsUtils.binomialCoefficient(lhsSize, lhsSize - i);
            long lhsSupersets = CombinatoricsUtils.binomialCoefficient(lhsSize, i + 1);

            result += ((lhsSize - i) * relevantNodes) + lhsSupersets;
        }

        return result;
    }

    private void placeNextFD() throws PlacementFailedException {
        int candidateSize = nextFDSize();

        LexicographicalIterator iterator = iteratorPerLevel.get(candidateSize);

        while(iterator.hasNext()) {
            FunctionalDependency fD = iterator.next();

            if (isValidFD(fD)) {
                return;
            }
        }
        throw new PlacementFailedException("No placement found.");
    }

    protected int nextFDSize() {
        if (configuration.getCandidateSize() != null) {
            return configuration.getCandidateSize();
        }

        long minimum = Long.MAX_VALUE;
        Integer nextFDSize = null;
        for (int level : fDCount.keySet()) {
            if (initialCostPerLevel.get(level) < minimum) {
                minimum = initialCostPerLevel.get(level);
                nextFDSize = level;
            }
        }
        return nextFDSize;
    }
}
