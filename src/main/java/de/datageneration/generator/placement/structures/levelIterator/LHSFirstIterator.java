package de.datageneration.generator.placement.structures.levelIterator;

import de.datageneration.generator.placement.structures.FunctionalDependency;
import org.apache.lucene.util.OpenBitSet;

public class LHSFirstIterator extends FunctionalDependencyIterator {

    public LHSFirstIterator(int nCols, int level) {
        super(nCols, level);
        currentLhs.clear(0);
        currentLhs.set(level);
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
        if (currentLhs.equals(maxLhsVal)) {
            currentLhs = initialBitSet(nCols, level);
            currentRhs++;
        } else {
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
        }

        if (currentLhs.equals(maxLhsVal) && currentRhs == maxRhsVal) {
            hasNext = false;
        }
    }
}
