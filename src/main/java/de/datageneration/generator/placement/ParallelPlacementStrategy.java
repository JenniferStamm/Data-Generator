package de.datageneration.generator.placement;

import de.metanome.algorithms.hyfd.structures.FDTree;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelPlacementStrategy extends PlacementStrategy {
    List<PlacementStrategy> strategies = new ArrayList<PlacementStrategy>();
    List<StrategyWorker> workers = new ArrayList<StrategyWorker>();
    List<Thread> threads = new ArrayList<Thread>();

    int actualAmountOfResults;
    CountDownLatch doneSignal;
    Semaphore pauseForCheck;

    public ParallelPlacementStrategy(List<PlacementStrategies> placementStrategies) {
        super();
        for (PlacementStrategies placementStrategy : placementStrategies) {
            switch (placementStrategy) {
                case SIZE_BASED:
                    strategies.add(new OneTryOnlyPlacementStrategy());
                    break;
                case RANDOM:
                    strategies.add(new RandomPlacementStrategy());
                    break;
                case COST_BASED:
                    strategies.add(new InitialCostBasedPlacementStrategy());
                    break;
                case ABUL:
                    strategies.add(BruteForcePlacementStrategyMaker.makeABULIterativeBruteForceStrategy());
                    break;
                case ABUF:
                    strategies.add(BruteForcePlacementStrategyMaker.makeABUFRecursiveBruteForceStrategy());
                    break;
                case ABUA:
                    strategies.add(BruteForcePlacementStrategyMaker.makeABUAIterativeBruteForceStrategy());
                    break;
                case RBUL:
                    strategies.add(BruteForcePlacementStrategyMaker.makeRBULRecursiveBruteForceStrategy());
                    break;
                case RBUF:
                    strategies.add(BruteForcePlacementStrategyMaker.makeRBUFIterativeBruteForceStrategy());
                    break;
                case RBUA:
                    strategies.add(BruteForcePlacementStrategyMaker.makeRBUAIterativeBruteForceStrategy());
                    break;
                case CFL:
                    strategies.add(BruteForcePlacementStrategyMaker.makeCFLIterativeBruteForceStrategy());
                    break;
                case CFF:
                    strategies.add(BruteForcePlacementStrategyMaker.makeCFFRecursiveBruteForceStrategy());
                    break;
                case CFA:
                    strategies.add(BruteForcePlacementStrategyMaker.makeCFAIterativeBruteForceStrategy());
                    break;
                case MaRL:
                    strategies.add(BruteForcePlacementStrategyMaker.makeMaRLIterativeBruteForceStrategy());
                    break;
                case MaRF:
                    strategies.add(BruteForcePlacementStrategyMaker.makeMaRFIterativeBruteForceStrategy());
                    break;
                case MaRA:
                    strategies.add(BruteForcePlacementStrategyMaker.makeMaRAIterativeBruteForceStrategy());
                    break;
                case MiRL:
                    strategies.add(BruteForcePlacementStrategyMaker.makeMiRLIterativeBruteForceStrategy());
                    break;
                case MiRF:
                    strategies.add(BruteForcePlacementStrategyMaker.makeMiRFIterativeBruteForceStrategy());
                    break;
                case MiRA:
                    strategies.add(BruteForcePlacementStrategyMaker.makeMiRAIterativeBruteForceStrategy());
                    break;
                case BRUTE_FORCE:
                    strategies.add(BruteForcePlacementStrategyMaker.makeABULIterativeBruteForceStrategy());
                    strategies.add(BruteForcePlacementStrategyMaker.makeABUFRecursiveBruteForceStrategy());
                    strategies.add(BruteForcePlacementStrategyMaker.makeABUAIterativeBruteForceStrategy());
                    strategies.add(BruteForcePlacementStrategyMaker.makeRBULRecursiveBruteForceStrategy());
                    strategies.add(BruteForcePlacementStrategyMaker.makeRBUFIterativeBruteForceStrategy());
                    strategies.add(BruteForcePlacementStrategyMaker.makeRBUAIterativeBruteForceStrategy());
                    strategies.add(BruteForcePlacementStrategyMaker.makeCFLIterativeBruteForceStrategy());
                    strategies.add(BruteForcePlacementStrategyMaker.makeCFFRecursiveBruteForceStrategy());
                    strategies.add(BruteForcePlacementStrategyMaker.makeCFAIterativeBruteForceStrategy());
                    strategies.add(BruteForcePlacementStrategyMaker.makeMaRLIterativeBruteForceStrategy());
                    strategies.add(BruteForcePlacementStrategyMaker.makeMaRFIterativeBruteForceStrategy());
                    strategies.add(BruteForcePlacementStrategyMaker.makeMaRAIterativeBruteForceStrategy());
                    strategies.add(BruteForcePlacementStrategyMaker.makeMiRLIterativeBruteForceStrategy());
                    strategies.add(BruteForcePlacementStrategyMaker.makeMiRFIterativeBruteForceStrategy());
                    strategies.add(BruteForcePlacementStrategyMaker.makeMiRAIterativeBruteForceStrategy());
                    break;
                default:
                    strategies.add(BruteForcePlacementStrategyMaker.makeABUFRecursiveBruteForceStrategy());
                    break;
            }
        }

        // We can at most generate one result per strategy
        actualAmountOfResults = Math.min(configuration.getAmountOfResults(), strategies.size());
        doneSignal = new CountDownLatch(actualAmountOfResults);
        pauseForCheck = new Semaphore(actualAmountOfResults);
    }

    @Override
    public List<FDTree> process() throws PlacementFailedException {
        for (PlacementStrategy strategy : strategies) {
            StrategyWorker worker = new StrategyWorker(doneSignal, pauseForCheck, strategy);
            Thread thread = new Thread(worker);
            thread.start();
            workers.add(worker);
            threads.add(thread);
        }

        //wait for necessary amount of threads to complete.
        try {
            List<FDTree> result = null;

            doneSignal.await();

            // Collect all results
            for (StrategyWorker worker : workers) {
                if (worker.isCompleted()) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    if (worker.getResult() != null) {
                        result.addAll(worker.getResult());
                    }
                }
            }

            pauseForCheck.release(workers.size() - actualAmountOfResults);

            for (Thread thread : threads) {
                thread.interrupt();
            }

            if (result != null) {
                return result;
            } else {
                throw new PlacementFailedException("No strategy found a solution.");
            }

        } catch (InterruptedException e) {
        }

        return null;
    }
}
