package de.datageneration.generator.placement;

import de.datageneration.generator.Util;
import de.datageneration.generator.placement.structures.levelCalculator.RandomLevelCalculator;
import de.datageneration.generator.placement.structures.FunctionalDependency;
import de.metanome.algorithms.hyfd.structures.FDTree;
import org.apache.lucene.util.OpenBitSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomPlacementStrategy extends BruteForcePlacementStrategy {
    List<FDPlacer> fdPlacers = new ArrayList<>();

    @Override
    public List<FDTree> process() throws PlacementFailedException {
        levelCalculator = new RandomLevelCalculator(fDCount);

        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                throw new PlacementFailedException("Interrupted");
            }
            if (!placeNextFD()) {
                undo();
            }

            if (fDCount.isEmpty()) {
                if (checkTransitives()) {
                    return Collections.singletonList(functionalDependencies);
                } else {
                    undo();
                }
            }
        }

        //throw new PlacementFailedException("Random Placement failed");
    }

    // remove a random amount of already placed FDs
    private void undo() {
        Collections.shuffle(fdPlacers);
        Random rand = new Random();
        int toRemoveCount = rand.nextInt(fdPlacers.size()) + 1;
        for (int i = 0; i < toRemoveCount; i++) {
            fdPlacers.get(i).unplace();
        }
        fdPlacers.subList(0, toRemoveCount).clear();
    }

    private boolean placeNextFD()  {
        int candidateSize = levelCalculator.nextFDSize();

        int failedCounter = 0;
        while (failedCounter < triesNeededForPlacement(candidateSize)) {
            FunctionalDependency fD = randomFD(candidateSize);
            FDPlacer fdPlacer = new BruteForceFDPlacer(this, fD);
            if (!fdPlacer.place()) {
                failedCounter++;
                fdPlacer.unplace();
            } else {
                if (fdPlacer.hasPlacedSelf()) {
                    fdPlacers.add(fdPlacer);
                }
                return true;
            }
        }
        return false;
    }

    private int triesNeededForPlacement(int candidateSize) {
        int amountOfPlacedFds = fDHistogram.get(candidateSize) - fDCount.get(candidateSize);
        float probabilityToFail = (float) amountOfPlacedFds /
                Util.numFDsInLevel(configuration.getAmountOfAttributes(), candidateSize);
        return Math.max(1, (int) Math.ceil(Math.log (1 - configuration.getConfidence()) / Math.log(probabilityToFail)));
    }

    private FunctionalDependency randomFD(int candidateSize) {
        ArrayList<Integer> columns = new ArrayList<>();
        for (int i = 0; i < configuration.getAmountOfAttributes(); i++) {
            columns.add(i);
        }
        Collections.shuffle(columns);

        OpenBitSet lhs = new OpenBitSet(configuration.getAmountOfAttributes());
        for (int i = 0; i < candidateSize; i++) {
            lhs.flip(columns.get(i));
        }

        return new FunctionalDependency(lhs, columns.get(candidateSize));
    }
}
