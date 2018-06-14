package de.datageneration.generator.placement;

import de.datageneration.generator.Configuration;
import de.datageneration.generator.Util;
import de.datageneration.generator.placement.structures.levelCalculator.LevelCalculator;
import de.datageneration.generator.placement.structures.FunctionalDependency;
import de.datageneration.generator.placement.structures.IllegalFDsManager;
import de.metanome.algorithms.hyfd.structures.FDTree;
import org.apache.lucene.util.OpenBitSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PlacementStrategy {
    Configuration configuration ;
    Map<Integer, Integer> fDHistogram;
    Map<Integer, Integer> fDCount;
    FDTree functionalDependencies;
    public IllegalFDsManager illegalFDsManager;
    LevelCalculator levelCalculator;

    PlacementStrategy() {
        configuration = Configuration.getConfiguration();
        fDHistogram = configuration.getFDHistogram();
        fDCount = new HashMap<>(fDHistogram);
        functionalDependencies = new FDTree(configuration.getAmountOfAttributes(), configuration.getAmountOfAttributes() - 1);
        //transitives = new HashMap<>();
    }

    public abstract List<FDTree> process() throws PlacementFailedException;

    protected void addFunctionalDependency(OpenBitSet lhs, int rhs) {
        //System.out.println("Placed " + Util.openBitSetToString(lhs) + "-> A" + rhs);
        if (functionalDependencies.containsFd(lhs, rhs)) {
            throw new Error("Tried to add Functional Dependency that was already added: "
                    + Util.openBitSetToString(lhs) + " -> " + rhs);
        }
        functionalDependencies.addFunctionalDependency(lhs, rhs);
        if (fDCount.get((int) lhs.cardinality()) > 1) {
            fDCount.put((int) lhs.cardinality(), fDCount.get((int) lhs.cardinality()) - 1);
        }
        else {
            fDCount.remove((int) lhs.cardinality());
        }
    }
    protected void addFunctionalDependencies(OpenBitSet lhs, OpenBitSet rhs) {
        for (int j = rhs.nextSetBit(0); j >= 0; j = rhs.nextSetBit(j + 1)) {
            addFunctionalDependency(lhs, j);
        }
    }

    protected void removeFunctionalDependency(OpenBitSet lhs, int rhs) {
        if (functionalDependencies.containsFd(lhs, rhs)) {
            functionalDependencies.removeFunctionalDependency(lhs, rhs);
            if (fDCount.get((int) lhs.cardinality()) != null) {
                fDCount.put((int) lhs.cardinality(), fDCount.get((int) lhs.cardinality()) + 1);
            } else {
                fDCount.put((int) lhs.cardinality(), 1);
            }
        }
    }


    protected boolean isValidFD(FunctionalDependency fD) {
        OtherFDPlacer placer = new OtherFDPlacer(this, new FunctionalDependency(fD.getLhs(), fD.getRhs()));
        return placer.place();
    }

}
