package de.datageneration.generator.placement;

import static org.junit.jupiter.api.Assertions.*;

class InitialCostBasedPlacementStrategyTest extends StrategyTestBase {

    @Override
    protected PlacementStrategy createInstance() {
        return new InitialCostBasedPlacementStrategy();
    }
}