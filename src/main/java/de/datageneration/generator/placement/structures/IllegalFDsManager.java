package de.datageneration.generator.placement.structures;

import de.datageneration.generator.Configuration;
import org.apache.lucene.util.OpenBitSet;

import java.util.ArrayList;
import java.util.HashMap;

public class IllegalFDsManager {
    Configuration configuration;
    protected HashMap<OpenBitSet, OpenBitSet> illegalFDs;
    protected ArrayList<Integer> illegalFDsPerLevel;


    public IllegalFDsManager() {
        configuration = Configuration.getConfiguration();
        this.illegalFDs = new HashMap<>();
        this.illegalFDsPerLevel = new ArrayList<>(configuration.getAmountOfAttributes());
        for (int level = 0; level < configuration.getAmountOfAttributes(); level++) {
            illegalFDsPerLevel.add(level, 0);
        }
    }

    public boolean addIllegalFD(FunctionalDependency functionalDependency) {
        if (illegalFDs.get(functionalDependency.getLhs()) == null) {
            OpenBitSet rhs = new OpenBitSet(configuration.getAmountOfAttributes());
            rhs.set(functionalDependency.getRhs());
            illegalFDs.put(functionalDependency.getLhs().clone(), rhs);

            illegalFDsPerLevel.set((int) functionalDependency.getLhs().cardinality(),
                    illegalFDsPerLevel.get((int) functionalDependency.getLhs().cardinality()) + 1);

            return true;
        } else if (!illegalFDs.get(functionalDependency.getLhs()).get(functionalDependency.getRhs())) {
            illegalFDs.get(functionalDependency.getLhs()).set(functionalDependency.getRhs());

            illegalFDsPerLevel.set((int) functionalDependency.getLhs().cardinality(),
                    illegalFDsPerLevel.get((int) functionalDependency.getLhs().cardinality()) + 1);

            return true;
        } else {
            return false;
        }
    }

    public void removeIllegalFD(FunctionalDependency functionalDependency) {
        if (illegalFDs.get(functionalDependency.getLhs()) != null
                && illegalFDs.get(functionalDependency.getLhs()).get(functionalDependency.getRhs())) {
            illegalFDs.get(functionalDependency.getLhs()).clear(functionalDependency.getRhs());
            illegalFDsPerLevel.set((int) functionalDependency.getLhs().cardinality(),
                    illegalFDsPerLevel.get((int) functionalDependency.getLhs().cardinality()) - 1);
        }
    }

    public int getAmountOfIllegalFDs(int level) {
        return illegalFDsPerLevel.get(level);
    }

    public int getAmountOfIllegalFDs() {
        return  illegalFDsPerLevel.stream().mapToInt(Integer::intValue).sum();
    }

    public boolean isIllegal(FunctionalDependency functionalDependency) {
        OpenBitSet invalidationsPerLHS = illegalFDs.get(functionalDependency.getLhs());
        if (invalidationsPerLHS != null && invalidationsPerLHS.get(functionalDependency.getRhs())) {
            return true;
        }
        else {
            return false;
        }
    }

    public int getAmountOfIllegalKeys() {
        return illegalFDs.size();
    }
}
