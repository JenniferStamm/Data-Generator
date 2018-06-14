package de.datageneration.generator.placement.structures.levelIterator;

import de.datageneration.generator.placement.structures.FunctionalDependency;
import org.apache.lucene.util.OpenBitSet;

public class ReverseLexicographicalIterator extends  FunctionalDependencyIterator {


    private final OpenBitSet minLhsVal;

    public ReverseLexicographicalIterator(int nCols, int level) {
        super(nCols, level);
        this.currentLhs = initialBitSet2(nCols, level);
        this.currentRhs = nCols - 1 - level;
        this.minLhsVal = minLhsVal(nCols, level);
    }

    @Override
    public FunctionalDependency next() {
        OpenBitSet lhs = currentLhs.clone();
        Integer rhs = currentRhs;

        if (currentLhs.equals(minLhsVal) && currentRhs == 0) {
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
        if (currentRhs == 0) {
            currentRhs = nCols - 1;

            // Find most significant 1 that has 0 at next (lower) position
            int chosenOne = 0;
            for (int i = currentLhs.prevSetBit(nCols - 1); i >= 0; i = currentLhs.prevSetBit(i - 1)) {
                if (!currentLhs.get(i - 1)) {
                    chosenOne = i;
                    break;
                }
            }

            // Move chosen one to a lower position
            currentLhs.clear(chosenOne);
            currentLhs.set(chosenOne - 1);

            // Count ones at positions higher than the chosen one and clear them

            int onesCount = 0;
            for (int i = currentLhs.nextSetBit(chosenOne + 1); i >= 0; i = currentLhs.nextSetBit(i + 1)) {
                onesCount++;
                currentLhs.clear(i);
            }

            // Move them all to most significant positions
            for (int i = nCols - 1; i > nCols - 1 - onesCount; i--) {
                currentLhs.set(i);
            }
        } else {
            currentRhs--;
        }

        if (currentLhs.equals(minLhsVal) && currentRhs == 0) {
            hasNext = false;
        }
    }

    public static OpenBitSet initialBitSet2(int nCols, int level) {
        OpenBitSet set = new OpenBitSet(nCols);
        for(int i = nCols - 1; i > nCols - level - 1; i--) {
            set.set(i);
        }
        return set;
    }

    public static OpenBitSet minLhsVal(int nCols, int level) {
        OpenBitSet set = new OpenBitSet(nCols);
        for(int i = 0; i < level; i++) {
            set.set(i);
        }
        return set;
    }
}
