package de.datageneration.generator;

import org.apache.lucene.util.OpenBitSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void isTrivial() {
        OpenBitSet lhs = new OpenBitSet(2);
        lhs.set(1);

        assertTrue(Util.isTrivial(lhs, 1));
        assertFalse(Util.isTrivial(lhs, 0));
    }
}