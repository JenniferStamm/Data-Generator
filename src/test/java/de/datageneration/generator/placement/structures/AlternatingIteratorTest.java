package de.datageneration.generator.placement.structures;

import de.datageneration.generator.placement.structures.levelIterator.AlternatingIterator;
import de.datageneration.generator.placement.structures.levelIterator.FunctionalDependencyIterator;

class AlternatingIteratorTest extends FunctionalDependencyIteratorTestBase {

    @Override
    protected FunctionalDependencyIterator createInstance(int attributes, int level) {
        return new AlternatingIterator(attributes, level);
    }
}