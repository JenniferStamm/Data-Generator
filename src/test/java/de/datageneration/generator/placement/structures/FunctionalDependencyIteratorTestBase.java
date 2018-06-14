package de.datageneration.generator.placement.structures;

import de.datageneration.generator.Util;
import de.datageneration.generator.placement.structures.levelIterator.FunctionalDependencyIterator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class FunctionalDependencyIteratorTestBase<T extends FunctionalDependencyIterator>{
    protected T iterator;

    protected abstract T createInstance(int attributes, int level);

    @ParameterizedTest
    @MethodSource("testData")
    void testCounter(int attributes, int level) {
        T it = createInstance(attributes, level);
        while (it.hasNext()) {
            FunctionalDependency functionalDependency = it.next();
        }

        assertEquals(Util.numFDsInLevel(attributes, level), it.getCounter());
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(5, 1),
                Arguments.of(5, 2),
                Arguments.of(2, 1),
                Arguments.of(4, 2),
                Arguments.of(5, 3)
        );
    }
}
