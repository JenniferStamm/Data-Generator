package de.datageneration.generator.placement.structures.levelIterator;

import de.datageneration.generator.placement.structures.FunctionalDependency;
import org.apache.lucene.util.OpenBitSet;

public class AlternatingIterator extends  FunctionalDependencyIterator {
    protected OpenBitSet lastLhs;
    protected int lastRhs;
    protected boolean ascendingNext;
    protected LexicographicalIterator lexicographicalIterator;
    protected ReverseLexicographicalIterator reverseLexicographicalIterator;

    public AlternatingIterator(int nCols, int level) {
        super(nCols, level);

        this.ascendingNext = false;

        this.lexicographicalIterator = new LexicographicalIterator(nCols, level);
        FunctionalDependency functionalDependency = lexicographicalIterator.next();
        this.currentLhs = functionalDependency.getLhs();
        this.currentRhs = functionalDependency.getRhs();

        this.reverseLexicographicalIterator = new ReverseLexicographicalIterator(nCols, level);
        this.lastLhs = initialRhsBitSet(nCols, level);
        this.lastRhs = nCols - level;
    }

    public void copy(AlternatingIterator other) {
        super.copy(other);
        lastLhs = other.lastLhs.clone();
        lastRhs = other.lastRhs;
        ascendingNext = other.ascendingNext;
        lexicographicalIterator = (LexicographicalIterator) other.lexicographicalIterator.clone();
        reverseLexicographicalIterator = (ReverseLexicographicalIterator) other.reverseLexicographicalIterator.clone();
    }

    @Override
    public Object clone() {
        AlternatingIterator result =  (AlternatingIterator) super.clone();
        result.lastLhs = lastLhs.clone();
        result.lastRhs = lastRhs;
        result.ascendingNext = ascendingNext;
        result.lexicographicalIterator = (LexicographicalIterator) lexicographicalIterator.clone();
        result.reverseLexicographicalIterator = (ReverseLexicographicalIterator) reverseLexicographicalIterator.clone();
        return result;
    }

    @Override
    public FunctionalDependency next() {
        OpenBitSet lhs = currentLhs.clone();
        Integer rhs = currentRhs;

        //if (!(currentLhs.equals(lastLhs) && currentRhs == lastRhs)) {
        //if (true) {
            generateNext();
            while (currentLhs.get(currentRhs)
                    && !(currentLhs.equals(lastLhs) && currentRhs == lastRhs)
                    && hasNext) {
                generateNext();
            }
        //}
        counter++;
        return new FunctionalDependency(lhs, rhs);

    }

    private void generateNext() {
        prepareGeneration();
        FunctionalDependency nextFunctionalDependency;
        if (ascendingNext) {
            nextFunctionalDependency = lexicographicalIterator.next();
        } else {
            nextFunctionalDependency = reverseLexicographicalIterator.next();
        }
        currentLhs = nextFunctionalDependency.getLhs();
        currentRhs = nextFunctionalDependency.getRhs();
        ascendingNext = !ascendingNext;

        if (currentLhs.equals(lastLhs) && currentRhs == lastRhs) {
            hasNext = false;
        }
    }

    /*
    Last value is needed to generate next value. Thus currentFD and lastFD need to be swapped.
     */
    private void prepareGeneration() {
        OpenBitSet tmpLhs = currentLhs;
        currentLhs = lastLhs;
        lastLhs = tmpLhs;
        int tmpRhs = currentRhs;
        currentRhs = lastRhs;
        lastRhs = tmpRhs;
    }

    public static OpenBitSet initialRhsBitSet(int nCols, int level) {
        OpenBitSet set = new OpenBitSet(nCols);
        for(int i = nCols - 1; i > nCols - level - 1; i--) {
            set.set(i);
        }
        return set;
    }
}
