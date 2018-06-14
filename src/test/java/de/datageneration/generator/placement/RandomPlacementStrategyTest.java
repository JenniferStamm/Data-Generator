package de.datageneration.generator.placement;

import de.datageneration.generator.Configuration;
import de.datageneration.generator.Util;
import de.datageneration.generator.placement.structures.FunctionalDependency;
import de.metanome.algorithms.hyfd.structures.FDTree;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RandomPlacementStrategyTest {

    @ParameterizedTest
    @MethodSource("solvableData")
    void solvable(String configPath, int expectedFDs) throws PlacementFailedException {
        File configurationFile = new File(configPath);
        Configuration.setConfigurationFile(configurationFile);

        PlacementStrategy strategy = new RandomPlacementStrategy();

        FDTree functionalDependencies = strategy.process().get(0);
        AtomicInteger fDCounter = new AtomicInteger(0);
        Util.forEachFDdo(functionalDependencies, (FunctionalDependency fD) -> {
            fDCounter.getAndIncrement();
            return null;
        });
        assertEquals(expectedFDs, fDCounter.intValue());
    }

    private static Stream<Arguments> solvableData() {
        return Stream.of(
                Arguments.of("src/test/resources/solvable_1.properties", 3),
                Arguments.of("src/test/resources/solvable_2.properties", 16)
        );
    }
}