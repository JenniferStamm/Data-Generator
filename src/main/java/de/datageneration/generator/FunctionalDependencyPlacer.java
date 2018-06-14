package de.datageneration.generator;

import de.datageneration.generator.placement.*;
import de.metanome.algorithms.hyfd.structures.FDTree;

import java.util.List;

public class FunctionalDependencyPlacer extends Pipe<Object, List<FDTree>> {
    PlacementStrategy strategy;

    @Override
    List<FDTree> process(Object input) throws PlacementFailedException {
        Configuration configuration = Configuration.getConfiguration();

        strategy = new ParallelPlacementStrategy(configuration.getPlacementStrategies());

        return strategy.process();
    }
}
