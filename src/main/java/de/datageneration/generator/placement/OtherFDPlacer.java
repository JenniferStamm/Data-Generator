package de.datageneration.generator.placement;

import de.datageneration.generator.Util;
import de.datageneration.generator.placement.structures.FunctionalDependencies;
import de.datageneration.generator.placement.structures.FunctionalDependency;
import org.apache.lucene.util.OpenBitSet;

import java.util.ArrayList;
import java.util.List;

public class OtherFDPlacer extends FDPlacer {
    private OpenBitSet candidateCompleteRhs;
    List<OtherFDPlacer> placers;

    public boolean hasPlacedSelf() {
        return placedSelf;
    }

    OtherFDPlacer(PlacementStrategy placementStrategy, FunctionalDependency candidate) {
        super(placementStrategy, candidate);
        this.placers = new ArrayList<>();
    }

    public boolean place() {
        OpenBitSet lhs = candidateLhs;
        int rhs = candidateRhs;

        if (Util.isTrivial(lhs, rhs)) {
            return false;
        }

        if (placementStrategy.functionalDependencies.containsFd(lhs, rhs)
                || placementStrategy.functionalDependencies.containsFdOrGeneralization(lhs, rhs)) {
            return true;
        }

        if (placementStrategy.functionalDependencies.containsFdOrSpecialization(lhs, rhs)) {
            return false;
        }

        if (violatesMinimality()) {
            return false;
        }

        placementStrategy.addFunctionalDependency(lhs, rhs);
        placedSelf = true;

        if (placementStrategy.illegalFDsManager != null) {
            invalidateSupersets();
        }

        this.candidateCompleteRhs = Util.getCompleteRHS(placementStrategy.functionalDependencies, this.candidateLhs);

        if (checkTransitives()) {
            return true;
        } else {
            unplace();
            return false;
        }

    }

    public void unplace() {
        if (placedSelf) {
            placementStrategy.removeFunctionalDependency(candidateLhs, candidateRhs);
            placedSelf = false;
            if (placementStrategy.illegalFDsManager != null) {
                reverseInvalidate();
            }
        }

        for (OtherFDPlacer placer : placers) {
            placer.unplace();
        }
        placers.clear();
    }

    private boolean checkTransitives() {
        Util.forEachLHSdo(placementStrategy.functionalDependencies, this::identifyTransitives);
        return !invalidationFound;
    }

    private Void identifyTransitives(FunctionalDependencies placedFD) {
        if (invalidationFound) {
            return null;
        }

        // test for placedLhs -> placedRhs -> candidateRHS
        FunctionalDependencies transitives = Util.findTransitives(placedFD, new FunctionalDependencies(candidateLhs, candidateCompleteRhs));
        if (transitives != null && !invalidationFound) {
            processTransitives(transitives);
        }


        // candidateLhs -> candidateRHS -> placedRhs
        transitives = Util.findTransitives(new FunctionalDependencies(candidateLhs, candidateCompleteRhs), placedFD);
        if (transitives != null  && !invalidationFound) {
            processTransitives(transitives);
        }


        // test for (X)placedLhs -> (Y)placedRhs  and (WY) candidateLhs -> (Z)candidateRHS
        FunctionalDependencies pseudoTransitives = Util.findPseudoTransitives(placedFD, new FunctionalDependencies(candidateLhs, candidateCompleteRhs));
        if (pseudoTransitives != null && !invalidationFound) {
            processTransitives(pseudoTransitives);
        }

        // test for (X)candidateLhs -> (Y)candidateCompleteRhs and (WY) placedLhs -> placedRhs
        pseudoTransitives = Util.findPseudoTransitives(new FunctionalDependencies(candidateLhs, candidateCompleteRhs), placedFD);
        if (pseudoTransitives != null && !invalidationFound) {
            processTransitives(pseudoTransitives);
        }

        return null;
    }

    private void processTransitives(FunctionalDependencies transitives) {
        OpenBitSet transitiveLhs = transitives.getLhs();
        OpenBitSet transitiveRhs = transitives.getRhs();
        for (int i = transitiveRhs.nextSetBit(0); i >= 0; i = transitiveRhs.nextSetBit(i + 1)) {
            if (invalidationFound) {
                return;
            }
            processTransitive(transitiveLhs, i);
        }
    }

    private void processTransitive(OpenBitSet transitiveLHS, Integer transitiveRHS) {
        if (transitiveLHS.equals(candidateLhs) && candidateCompleteRhs.get(transitiveRHS)
                || Util.isTrivial(transitiveLHS, transitiveRHS)
                || placementStrategy.functionalDependencies.containsFd(transitiveLHS, transitiveRHS)) {
            return;
        }

        int transitiveSize = (int) transitiveLHS.cardinality();
        if (placementStrategy.fDCount.get(transitiveSize) == null) {
            invalidationFound = true;
            return;
        }

        OtherFDPlacer placer = new OtherFDPlacer(placementStrategy, new FunctionalDependency(transitiveLHS, transitiveRHS));

        if (placer.place()) {
            placers.add(placer);
        } else {
            placer.unplace();
            invalidationFound = true;
            return;
        }

    }
}
