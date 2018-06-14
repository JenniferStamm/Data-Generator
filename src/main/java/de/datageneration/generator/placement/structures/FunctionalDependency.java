package de.datageneration.generator.placement.structures;

import de.datageneration.generator.Util;
import org.apache.lucene.util.OpenBitSet;

public class FunctionalDependency {
    private OpenBitSet lhs;
    private int rhs;


    public FunctionalDependency(OpenBitSet lhs, int rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public OpenBitSet getLhs() {
        return lhs;
    }

    public void setLhs(OpenBitSet lhs) {
        this.lhs = lhs;
    }

    public int getRhs() {
        return rhs;
    }

    public void setRhs(int rhs) {
        this.rhs = rhs;
    }

    public String toString() {
        return Util.openBitSetToString(lhs) + " -> A" + rhs;
    }
}
