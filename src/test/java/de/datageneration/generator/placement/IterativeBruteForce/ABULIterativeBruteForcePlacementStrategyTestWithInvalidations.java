package de.datageneration.generator.placement.IterativeBruteForce;

import de.datageneration.generator.placement.BruteForcePlacementStrategyMaker;
import de.datageneration.generator.placement.BruteForceStrategyBase;
import de.datageneration.generator.placement.PlacementStrategy;

class ABULIterativeBruteForcePlacementStrategyTestWithInvalidations extends BruteForceStrategyBase {

    @Override
    protected PlacementStrategy createInstance() {
        return BruteForcePlacementStrategyMaker.makeABULIterativeBruteForceStrategyWithInvalidations();
    }
}