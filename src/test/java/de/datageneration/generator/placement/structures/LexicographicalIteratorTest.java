package de.datageneration.generator.placement.structures;

import de.datageneration.generator.placement.structures.levelIterator.FunctionalDependencyIterator;
import de.datageneration.generator.placement.structures.levelIterator.LexicographicalIterator;

class LexicographicalIteratorTest extends FunctionalDependencyIteratorTestBase {

    @Override
    protected FunctionalDependencyIterator createInstance(int attributes, int level) {
        return new LexicographicalIterator(attributes, level);
    }
}