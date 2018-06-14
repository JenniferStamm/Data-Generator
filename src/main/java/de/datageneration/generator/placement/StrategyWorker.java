package de.datageneration.generator.placement;

import de.metanome.algorithms.hyfd.structures.FDTree;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class StrategyWorker implements Runnable {
    private final CountDownLatch doneSignal;
    private final Semaphore pauseForCheck;

    public PlacementStrategy getStrategy() {
        return strategy;
    }

    private final PlacementStrategy strategy;

    public StrategyWorker(CountDownLatch doneSignal, Semaphore pauseForCheck, PlacementStrategy strategy) {
        this.doneSignal = doneSignal;
        this.pauseForCheck = pauseForCheck;
        this.strategy = strategy;
    }

    public boolean isCompleted() {
        return completed;
    }

    public List<FDTree> getResult() {
        return result;
    }

    private boolean completed = false;
    private List<FDTree> result;

    @Override
    public void run() {
        try
        {

            result = strategy.process();

            //try to get the semaphore. Since there is only
            //one permit, the first worker to finish gets it,
            //and the rest will block.
            pauseForCheck.acquire();

            //Use a completed flag instead of Thread.isAlive because
            //even though countDown is the last thing in the run method,
            //the run method may not have before the time the
            //controlling thread can check isAlive status
            completed = true;

            //tell controller we are finished
            doneSignal.countDown();
        }
        catch (InterruptedException e)
        {
            //e.printStackTrace();
        } catch (PlacementFailedException e) {
            //e.printStackTrace();
        }
    }
}
