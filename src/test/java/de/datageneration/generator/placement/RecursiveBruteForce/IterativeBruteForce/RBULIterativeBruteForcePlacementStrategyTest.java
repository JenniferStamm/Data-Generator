package de.datageneration.generator.placement.RecursiveBruteForce.IterativeBruteForce;

import de.datageneration.generator.placement.BruteForcePlacementStrategyMaker;
import de.datageneration.generator.placement.BruteForceStrategyBase;
import de.datageneration.generator.placement.PlacementStrategy;

class RBULIterativeBruteForcePlacementStrategyTest extends BruteForceStrategyBase {

    @Override
    protected PlacementStrategy createInstance() {
        return BruteForcePlacementStrategyMaker.makeRBULRecursiveBruteForceStrategy();
    }
}