package de.datageneration.generator.placement;

import de.datageneration.generator.Util;
import de.datageneration.generator.placement.structures.FunctionalDependencies;
import de.datageneration.generator.placement.structures.FunctionalDependency;
import de.datageneration.generator.placement.structures.levelIterator.FunctionalDependencyIterator;
import de.metanome.algorithms.hyfd.structures.FDTree;

import java.util.Collections;
import java.util.List;

public class RecursiveBruteForcePlacementStrategy extends BruteForcePlacementStrategy {
    public List<FDTree> process() throws PlacementFailedException {
        if (!addFDToSolutionSet(levelCalculator.nextFDSize())) {
            throw new PlacementFailedException("No placement found.");
        }

        return Collections.singletonList(functionalDependencies);
    }

    public boolean addFDToSolutionSet(int level) throws PlacementFailedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new PlacementFailedException("Interrupted");
        }

        if (fDCount.isEmpty() || (configuration.getCandidateSize() != null && fDCount.get(configuration.getCandidateSize()) == null)) {
            if (checkTransitives()) {
                return true;
            } else {
                for (OtherFDPlacer fdPlacer: transitiveFDPlacers) {
                    fdPlacer.unplace();
                }
                return false;
            }
        }

        if (level >= configuration.getAmountOfAttributes()) {
            return false;
        }

        // #FDs to addFDToSolutionSet > (#FDs left = #available FDs - #FDs already tried)
        if (fDCount.get(level) >
                Util.numFDsInLevel(configuration.getAmountOfAttributes(), level)
                        - getIterator(level).getCounter()) {
            return  false;
        }

        if (illegalFDsManager != null && !levelCalculator.enoughFDsLeft(this)) {
            return  false;
        }

        FunctionalDependencyIterator itOld = (FunctionalDependencyIterator) iterators.get(level).clone();
        while (iterators.get(level).hasNext()) {
            FunctionalDependency fD = iterators.get(level).next();
            BruteForceFDPlacer placer = new BruteForceFDPlacer(this, new FunctionalDependency(fD.getLhs(), fD.getRhs()));
            if (placer.place() && placer.hasPlacedSelf()) {
                if (addFDToSolutionSet(levelCalculator.nextFDSize())) {
                    return true;
                } else {
                    placer.unplace();
                }
            }
        }
        iterators.set(level, (FunctionalDependencyIterator) itOld.clone());
        return false;
    }

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
