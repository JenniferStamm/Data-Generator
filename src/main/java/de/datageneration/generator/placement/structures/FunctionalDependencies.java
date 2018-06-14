package de.datageneration.generator.placement.structures;

import de.datageneration.generator.Util;
import org.apache.lucene.util.OpenBitSet;

public class FunctionalDependencies {
    private OpenBitSet lhs;
    private OpenBitSet rhs;


    public FunctionalDependencies(OpenBitSet lhs, OpenBitSet rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public OpenBitSet getLhs() {
        return lhs;
    }

    public void setLhs(OpenBitSet lhs) {
        this.lhs = lhs;
    }

    public OpenBitSet getRhs() {
        return rhs;
    }

    public void setRhs(OpenBitSet rhs) {
        this.rhs = rhs;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Util.openBitSetToString(lhs));
        stringBuilder.append("->");
        for (int i = rhs.nextSetBit(0); i >= 0; i = rhs.nextSetBit(i + 1)) {
            stringBuilder.append("A" + i);
        }
        return stringBuilder.toString();
    }
}
