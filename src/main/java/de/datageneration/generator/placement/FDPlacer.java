package de.datageneration.generator.placement;

import de.datageneration.generator.Configuration;
import de.datageneration.generator.Util;
import de.datageneration.generator.placement.structures.FunctionalDependency;
import org.apache.lucene.util.OpenBitSet;

import java.util.HashSet;
import java.util.Set;

public abstract class FDPlacer {
    Configuration configuration;
    PlacementStrategy placementStrategy;
    OpenBitSet candidateLhs;
    Integer candidateRhs;

    boolean placedSelf = false;
    boolean invalidationFound;
    Set<FunctionalDependency> invalidations;

    public boolean hasPlacedSelf() {
        return placedSelf;
    }

    FDPlacer(PlacementStrategy placementStrategy, FunctionalDependency candidate) {
        this.configuration = Configuration.getConfiguration();
        this.placementStrategy = placementStrategy;
        this.candidateLhs = candidate.getLhs().clone();
        this.candidateRhs = candidate.getRhs();
    }

    public abstract boolean place();

    public abstract void unplace();

    void invalidateSupersets() {
        invalidations = new HashSet<>();
        addInvalidations(new FunctionalDependency(candidateLhs, candidateRhs));

        OpenBitSet newLhs = candidateLhs.clone();
        newLhs.set(candidateRhs);
        OpenBitSet possibleRhs = newLhs.clone();
        possibleRhs.flip(0, configuration.getAmountOfAttributes());
        for (int i = possibleRhs.nextSetBit(0); i >= 0; i = possibleRhs.nextSetBit(i + 1)) {
            FunctionalDependency invalidation = new FunctionalDependency(newLhs, i);
            if (!invalidations.contains(invalidation)) {
                if (placementStrategy.illegalFDsManager.addIllegalFD(invalidation)) {
                    invalidations.add(invalidation);
                    if (newLhs.cardinality() != configuration.getAmountOfAttributes() - 1) {
                        addInvalidations(invalidation);
                    }
                }
            }
        }

    }

    private void addInvalidations(FunctionalDependency functionalDependency) {
        for (OpenBitSet invalidationLhs: Util.getDirectSupersets(functionalDependency.getLhs(), configuration.getAmountOfAttributes())) {
            if (invalidationLhs.get(functionalDependency.getRhs())) {
                continue;
            }

            FunctionalDependency invalidation = new FunctionalDependency(invalidationLhs, functionalDependency.getRhs());
            if (!invalidations.contains(invalidation)) {
                if (placementStrategy.illegalFDsManager.addIllegalFD(invalidation)) {
                    invalidations.add(invalidation);
                    if (invalidationLhs.cardinality() != configuration.getAmountOfAttributes() - 1) {
                        addInvalidations(invalidation);
                    }
                }
            }
        }
    }

    void reverseInvalidate() {
        for (FunctionalDependency invalidation: invalidations) {
            placementStrategy.illegalFDsManager.removeIllegalFD(invalidation);
        }
    }


    boolean violatesMinimality = false;
    protected boolean violatesMinimality() {
        Util.forEachFDdo(placementStrategy.functionalDependencies, (FunctionalDependency fd) -> {
            if (violatesMinimality) {
                return null;
            }
            if (violatesMinimality(fd)) {
                violatesMinimality = true;
                return null;
            }
            return null;
        });

        return violatesMinimality;
    }

    // A -> B violates the minimality of AB -> X
    // A -> B cannot be placed after AB -> X and vice versa
    private boolean violatesMinimality(FunctionalDependency fd) {
        OpenBitSet completeSetA = candidateLhs.clone();
        completeSetA.set(candidateRhs);

        if (Util.contains(fd.getLhs(), completeSetA)) {
            return true;
        }

        OpenBitSet completeSetAB = fd.getLhs().clone();
        completeSetAB.set(fd.getRhs());
        if (Util.contains(candidateLhs, completeSetAB)) {
            return true;
        }

        return false;
    }
}
