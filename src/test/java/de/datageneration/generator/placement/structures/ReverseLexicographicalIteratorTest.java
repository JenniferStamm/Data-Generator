package de.datageneration.generator.placement.structures;

import de.datageneration.generator.placement.structures.levelIterator.FunctionalDependencyIterator;
import de.datageneration.generator.placement.structures.levelIterator.ReverseLexicographicalIterator;

class ReverseLexicographicalIteratorTest extends FunctionalDependencyIteratorTestBase {

    @Override
    protected FunctionalDependencyIterator createInstance(int attributes, int level) {
        return new ReverseLexicographicalIterator(attributes, level);
    }
}