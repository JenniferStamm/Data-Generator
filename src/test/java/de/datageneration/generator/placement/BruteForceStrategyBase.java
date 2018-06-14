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

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class BruteForceStrategyBase extends StrategyTestBase {
    @ParameterizedTest
    @MethodSource("solvableBF")
    void solvableBF(String configPath, int expectedFDs) throws PlacementFailedException {
        File configurationFile = new File(configPath);
        Configuration.setConfigurationFile(configurationFile);

        strategy = createInstance();

        FDTree functionalDependencies = strategy.process().get(0);
        AtomicInteger fDCounter = new AtomicInteger(0);
        Util.forEachFDdo(functionalDependencies, (FunctionalDependency fD) -> {
            fDCounter.getAndIncrement();
            return null;
        });
        assertEquals(expectedFDs, fDCounter.intValue());
    }

    private  static Stream<Arguments> solvableBF() {
        return Stream.of(
                Arguments.of("src/test/resources/solvable_3_CS.properties", 9),
                Arguments.of("src/test/resources/solvable_4_CS.properties", 10),
                Arguments.of("src/test/resources/solvable_iris.properties", 4),
                Arguments.of("src/test/resources/solvable_balance-scale.properties", 1),
                Arguments.of("src/test/resources/solvable_chess.properties", 1),
                Arguments.of("src/test/resources/solvable_even_5.properties", 4)
        );
    }
}
