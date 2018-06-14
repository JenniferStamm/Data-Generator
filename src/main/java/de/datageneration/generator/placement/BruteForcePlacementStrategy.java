package de.datageneration.generator.placement;

import de.datageneration.generator.Util;
import de.datageneration.generator.placement.structures.levelCalculator.LevelCalculator;
import de.datageneration.generator.placement.structures.FunctionalDependencies;
import de.datageneration.generator.placement.structures.FunctionalDependency;
import de.datageneration.generator.placement.structures.levelIterator.FunctionalDependencyIterator;

import java.util.ArrayList;
import java.util.List;

public abstract class BruteForcePlacementStrategy extends PlacementStrategy {
    protected LevelCalculator levelCalculator;
    protected ArrayList<FunctionalDependencyIterator> iterators;
    protected boolean allTransitivesContained = true;
    List<OtherFDPlacer> transitiveFDPlacers = new ArrayList<>(); // only if candidatesize is set

    boolean checkTransitives() {
        allTransitivesContained = true;
        Util.forEachLHSdo(functionalDependencies, this::identifyTransitives);
        return allTransitivesContained;
    }

    private Void checkTransitive(FunctionalDependencies fD1, FunctionalDependencies fD2) {
        FunctionalDependencies transitive = Util.findTransitives(fD1, fD2);
        if (transitive != null) {
            for (int i = transitive.getRhs().nextSetBit(0); i >= 0; i = transitive.getRhs().nextSetBit(i + 1)) {
                if (!Util.isTrivial(transitive.getLhs(), i) && !functionalDependencies.containsFdOrGeneralization(transitive.getLhs(), i)) {
                    if (configuration.getCandidateSize() != null && transitive.getLhs().cardinality() != configuration.getCandidateSize()) {
                        OtherFDPlacer fdPlacer = new OtherFDPlacer(this, new FunctionalDependency(transitive.getLhs(), i));
                        fdPlacer.place();
                    } else {
                        allTransitivesContained = false;
                        return null;
                    }
                }
            }
        }
        FunctionalDependencies pseudoTransitive = Util.findPseudoTransitives(fD1, fD2);
        if (pseudoTransitive != null) {
            for (int i = pseudoTransitive.getRhs().nextSetBit(0); i >= 0; i = pseudoTransitive.getRhs().nextSetBit(i + 1)) {
                if (!Util.isTrivial(pseudoTransitive.getLhs(), i) && !functionalDependencies.containsFdOrGeneralization(pseudoTransitive.getLhs(), i)) {
                    if (configuration.getCandidateSize() != null && pseudoTransitive.getLhs().cardinality() != configuration.getCandidateSize()) {
                        OtherFDPlacer fdPlacer = new OtherFDPlacer(this, new FunctionalDependency(pseudoTransitive.getLhs(), i));
                        fdPlacer.place();
                        transitiveFDPlacers.add(fdPlacer);
                    } else {
                        allTransitivesContained = false;
                        return null;
                    }
                }
            }
        }

        return null;
    }

    private Void identifyTransitives(FunctionalDependencies fD) {
        if (!allTransitivesContained) {
            return null;
        }
        Util.forEachLHSdo(functionalDependencies, (fD2) -> checkTransitive(fD, fD2));
        return null;
    }

    public FunctionalDependencyIterator getIterator(int level) {
        return iterators.get(level);
    }
}
