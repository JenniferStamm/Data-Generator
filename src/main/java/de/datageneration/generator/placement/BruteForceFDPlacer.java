package de.datageneration.generator.placement;

import de.datageneration.generator.Util;
import de.datageneration.generator.placement.structures.FunctionalDependency;
import org.apache.lucene.util.OpenBitSet;

public class BruteForceFDPlacer extends FDPlacer {

    public boolean hasPlacedSelf() {
        return placedSelf;
    }

    BruteForceFDPlacer(PlacementStrategy placementStrategy, FunctionalDependency candidate) {
        super(placementStrategy, candidate);
    }

    public boolean place() {
        OpenBitSet lhs = candidateLhs;
        int rhs = candidateRhs;

        if (Util.isTrivial(lhs, rhs)) {
            return false;
        }

        if (placementStrategy.functionalDependencies.containsFd(lhs, rhs)) {
            return true;
        }

        // minimality is violated
        if (placementStrategy.illegalFDsManager == null) {
            if (placementStrategy.functionalDependencies.containsFdOrSpecialization(lhs, rhs)
                    || placementStrategy.functionalDependencies.containsFdOrGeneralization(lhs, rhs)) {
                return true;
            }
            if (violatesMinimality()) {
                return false;
            }
        } else {
            if (placementStrategy.illegalFDsManager.isIllegal(new FunctionalDependency(lhs, rhs)))
                return false;
        }

        placementStrategy.addFunctionalDependency(lhs, rhs);
        placedSelf = true;

        if (placementStrategy.illegalFDsManager != null) {
            invalidateSupersets();
        }

        return true;
    }

    public void unplace() {
        if (placedSelf) {
            placementStrategy.removeFunctionalDependency(candidateLhs, candidateRhs);
            placedSelf = false;
            if (placementStrategy.illegalFDsManager != null) {
                reverseInvalidate();
            }
        }
    }
}
