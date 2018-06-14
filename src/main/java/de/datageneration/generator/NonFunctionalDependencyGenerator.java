package de.datageneration.generator;

import de.datageneration.generator.placement.structures.FunctionalDependency;
import de.metanome.algorithms.hyfd.structures.FDTree;
import javafx.util.Pair;
import org.apache.lucene.util.OpenBitSet;

import java.util.*;

import static de.datageneration.generator.Util.forEachFDdo;
import static de.datageneration.generator.Util.getSpecializations;

public class NonFunctionalDependencyGenerator extends  Pipe<List<FDTree>, List<Pair<FDTree, FDTree>>> {
    Configuration configuration;

    @Override
    public List<Pair<FDTree, FDTree>> process(List<FDTree> input) {
        configuration = Configuration.getConfiguration();

        List<Pair<FDTree, FDTree>> result = new ArrayList<>();

        for (FDTree functionalDependencies : input) {
            FDTree nonFunctionalDependencies = new FDTree(configuration.getAmountOfAttributes(), configuration.getAmountOfAttributes());
            initializeMaxNonFDs(nonFunctionalDependencies);
            forEachFDdo(functionalDependencies, (FunctionalDependency fd) -> updateNonFDs(nonFunctionalDependencies, fd));
            result.add(new Pair<>(functionalDependencies, nonFunctionalDependencies));
        }

        return result;

    }

    private void initializeMaxNonFDs(FDTree nonFunctionalDependencies) {
        for (int i = 0; i < configuration.getAmountOfAttributes(); i++) {
            OpenBitSet lhs = new OpenBitSet(configuration.getAmountOfAttributes());
            lhs.flip(0, configuration.getAmountOfAttributes());
            lhs.flip(i);
            nonFunctionalDependencies.addFunctionalDependency(lhs, i);
        }
    }


    private Void updateNonFDs(FDTree nonFunctionalDependencies, FunctionalDependency fd) {
        List<OpenBitSet> violated = new ArrayList<>();
        getSpecializations(nonFunctionalDependencies, fd.getLhs(), fd.getRhs(), violated);
        for (OpenBitSet nonFD : violated) {
            nonFunctionalDependencies.removeFunctionalDependency(nonFD, fd.getRhs());
            for (int i = fd.getLhs().nextSetBit(0); i >= 0; i = fd.getLhs().nextSetBit(i + 1)) {
                OpenBitSet newLhs = nonFD.clone();
                newLhs.clear(i);
                int rhs = fd.getRhs();

                if (!nonFunctionalDependencies.containsFdOrSpecialization(newLhs, rhs))
                    nonFunctionalDependencies.addFunctionalDependency(newLhs, rhs);
            }
        }

        return null;
    }

}
