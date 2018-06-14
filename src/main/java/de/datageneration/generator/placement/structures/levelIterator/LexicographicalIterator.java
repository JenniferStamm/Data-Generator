package de.datageneration.generator.placement.structures.levelIterator;

import de.datageneration.generator.placement.structures.FunctionalDependency;
import org.apache.lucene.util.OpenBitSet;

public class LexicographicalIterator extends FunctionalDependencyIterator {


    public LexicographicalIterator(int nCols, int level) {
        super(nCols, level);
        this.currentRhs = level;
    }

    @Override
    public FunctionalDependency next() {
        OpenBitSet lhs = currentLhs.clone();
        Integer rhs = currentRhs;

        if (currentLhs.equals(maxLhsVal) && currentRhs == maxRhsVal) {
            hasNext = false;
            return new FunctionalDependency(lhs, rhs);
        }

        // Generate next value
        generateNext();
        while (currentLhs.get(currentRhs) && hasNext) {
            generateNext();
        }
        counter++;
        return new FunctionalDependency(lhs, rhs);
    }

    private void generateNext() {
        if (currentRhs == maxRhsVal) {
            currentRhs = 0;
            OpenBitSet opposite = currentLhs.clone();
            opposite.flip(0, nCols);
            int leftestZero = opposite.prevSetBit(nCols - 1);
            int nextOne = currentLhs.prevSetBit(leftestZero);
            currentLhs.clear(nextOne);
            currentLhs.set(nextOne + 1);

            int onesCount = 0;
            for (int i = currentLhs.nextSetBit(leftestZero + 1); i >= 0; i = currentLhs.nextSetBit(i + 1)) {
                onesCount++;
                currentLhs.clear(i);
            }
            for (int i = nextOne + 2; i < nextOne + 2 + onesCount; i++) {
                currentLhs.set(i);
            }
        } else {
            currentRhs++;
        }

        if (currentLhs.equals(maxLhsVal) && currentRhs == maxRhsVal) {
            hasNext = false;
        }
    }
}
