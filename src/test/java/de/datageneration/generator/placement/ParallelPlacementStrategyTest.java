package de.datageneration.generator.placement;

import de.datageneration.generator.Configuration;
import de.datageneration.generator.Util;
import de.datageneration.generator.placement.structures.FunctionalDependency;
import de.metanome.algorithms.hyfd.structures.FDTree;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParallelPlacementStrategyTest {

    @ParameterizedTest
    @MethodSource("solvableParallelData")
    void solvableParallel(String configPath, int expectedFDs, int expectedAmountOfResults) throws PlacementFailedException {
        File configurationFile = new File(configPath);
        Configuration.setConfigurationFile(configurationFile);

        List<PlacementStrategies> strategies = new ArrayList<>();
        strategies.add(PlacementStrategies.ABUF);
        strategies.add(PlacementStrategies.CFF);
        strategies.add(PlacementStrategies.RBUF);

        PlacementStrategy strategy = new ParallelPlacementStrategy(strategies);

        List<FDTree> result = strategy.process();

        assertEquals(expectedAmountOfResults, result.size());

        for (FDTree functionalDependencies : result) {
            AtomicInteger fDCounter = new AtomicInteger(0);
            Util.forEachFDdo(functionalDependencies, (FunctionalDependency fD) -> {
                fDCounter.getAndIncrement();
                return null;
            });
            assertEquals(expectedFDs, fDCounter.intValue());
        }
    }

    private static Stream<Arguments> solvableParallelData() {
        return Stream.of(
                Arguments.of("src/test/resources/solvable_parallel_1.properties", 3, 1),
                Arguments.of("src/test/resources/solvable_parallel_2.properties", 16, 3)
        );
    }
}