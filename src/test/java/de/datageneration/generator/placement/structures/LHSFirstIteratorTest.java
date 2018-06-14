package de.datageneration.generator.placement.structures;

import de.datageneration.generator.placement.structures.levelIterator.FunctionalDependencyIterator;
import de.datageneration.generator.placement.structures.levelIterator.LHSFirstIterator;

class LHSFirstIteratorTest extends FunctionalDependencyIteratorTestBase {

    @Override
    protected FunctionalDependencyIterator createInstance(int attributes, int level) {
        return new LHSFirstIterator(attributes, level);
    }
}