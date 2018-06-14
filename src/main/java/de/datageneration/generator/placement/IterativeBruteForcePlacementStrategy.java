package de.datageneration.generator.placement;

import de.datageneration.generator.Util;
import de.datageneration.generator.placement.structures.FunctionalDependency;
import de.datageneration.generator.placement.structures.levelIterator.FunctionalDependencyIterator;
import de.metanome.algorithms.hyfd.structures.FDTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class IterativeBruteForcePlacementStrategy extends BruteForcePlacementStrategy {
    protected Stack<BruteForceFDPlacer> placersStack = new Stack<>();
    protected Stack<ArrayList<FunctionalDependencyIterator>> iteratorsStack = new Stack<>();

    public List<FDTree> process() throws PlacementFailedException {
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                throw new PlacementFailedException("Interrupted");
            }

            if (fDCount.isEmpty() || (configuration.getCandidateSize() != null && fDCount.get(configuration.getCandidateSize()) == null)) {
                if (checkTransitives()) {
                    return Collections.singletonList(functionalDependencies);
                } else {
                    for (OtherFDPlacer fdPlacer: transitiveFDPlacers) {
                        fdPlacer.unplace();
                    }
                    undo();
                    continue;
                }
            }

            int level = levelCalculator.nextFDSize();

            if (level >= configuration.getAmountOfAttributes()) {
                undo();
                continue;
            }

            // #FDs to addFDToSolutionSet > (#FDs left = #available FDs - #FDs already tried)
            if (fDCount.get(level) >
                    Util.numFDsInLevel(configuration.getAmountOfAttributes(), level)
                            - getIterator(level).getCounter()) {
                undo();
                continue;
            }

            if (illegalFDsManager != null && !levelCalculator.enoughFDsLeft(this)) {
                undo();
                continue;
            }

            boolean placedSomething = false;
            while (iterators.get(level).hasNext()) {
                FunctionalDependency fD = iterators.get(level).next();
                BruteForceFDPlacer placer = new BruteForceFDPlacer(this, new FunctionalDependency(fD.getLhs().clone(), fD.getRhs()));
                if (placer.place() && placer.hasPlacedSelf()) {
                    placersStack.push(placer);
                    ArrayList<FunctionalDependencyIterator> newIterators = new ArrayList<>();
                    for (FunctionalDependencyIterator iterator : iterators) {
                        newIterators.add((FunctionalDependencyIterator) iterator.clone());
                    }
                    iteratorsStack.push(newIterators);
                    placedSomething = true;
                    break;
                } else {
                    placer.unplace();
                }
            }
            if (!placedSomething) {
                undo();
            }

        }
    }

    protected void undo() throws PlacementFailedException {
        if (placersStack.isEmpty()) {
            throw new PlacementFailedException("No placement found.");
        }
        iterators = iteratorsStack.pop();
        placersStack.pop().unplace();
    }
}
