package de.datageneration.generator.placement.structures.levelIterator;

import de.datageneration.generator.placement.structures.FunctionalDependency;
import org.apache.lucene.util.OpenBitSet;

import java.util.Iterator;

public abstract class FunctionalDependencyIterator implements Iterator<FunctionalDependency>, Cloneable {
    protected int nCols;
    protected int level;
    protected OpenBitSet currentLhs;
    protected OpenBitSet maxLhsVal;
    protected int currentRhs;
    protected int maxRhsVal;
    protected boolean hasNext;
    protected int counter;

    public int getCounter() {
        return counter;
    }

    public FunctionalDependencyIterator(int nCols, int level) {
        this(nCols, level, initialBitSet(nCols, level));
    }

    public FunctionalDependencyIterator(int nCols, int level, OpenBitSet currentLhs) {
        if (currentLhs.cardinality() != level) {
            throw new IllegalArgumentException("Node value does not correspond to level for lattice iterator.");
        }
        this.nCols = nCols;
        this.level = level;
        this.currentLhs = currentLhs.clone();
        this.maxLhsVal = maxBitSet(nCols, level);
        this.currentRhs = 0;
        this.maxRhsVal = nCols - 1;
        this.hasNext = true;
        this.counter = 0;
    }

    public void copy(FunctionalDependencyIterator other) {
        this.nCols = other.nCols;
        this.level = other.level;
        this.currentLhs = other.currentLhs.clone();
        this.maxLhsVal = other.maxLhsVal.clone();
        this.currentRhs = other.currentRhs;
        this.maxRhsVal = other.maxRhsVal;
        this.hasNext = other.hasNext;
        this.counter = other.counter;
    }

    @Override
    public Object clone() {
        FunctionalDependencyIterator result = null;
        try {
            result = (FunctionalDependencyIterator) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        result.nCols = nCols;
        result.level = level;
        result.currentLhs = currentLhs.clone();
        result.maxLhsVal = maxLhsVal.clone();
        result.currentRhs = currentRhs;
        result.maxRhsVal = maxRhsVal;
        result.hasNext = hasNext;
        result.counter = counter;

        return result;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    public static OpenBitSet initialBitSet(int nCols, int level) {
        OpenBitSet set = new OpenBitSet(nCols);
        for(int i = 0; i < level; i++) {
            set.set(i);
        }
        return set;
    }

    private static OpenBitSet maxBitSet(int nCols, int level) {
        OpenBitSet set = new OpenBitSet(nCols);
        for(int i = nCols - level; i < nCols; i++) {
            set.set(i);
        }
        return set;
    }
}
