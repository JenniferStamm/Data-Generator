package de.datageneration.generator.placement;

import de.datageneration.generator.placement.structures.levelCalculator.*;
import de.datageneration.generator.placement.structures.levelIterator.AlternatingIterator;
import de.datageneration.generator.placement.structures.IllegalFDsManager;
import de.datageneration.generator.placement.structures.levelIterator.LHSFirstIterator;
import de.datageneration.generator.placement.structures.levelIterator.LexicographicalIterator;

import java.util.ArrayList;

public class BruteForcePlacementStrategyMaker {

    public static RecursiveBruteForcePlacementStrategy makeABULRecursiveBruteForceStrategyWithInvalidations() {
        RecursiveBruteForcePlacementStrategy strategy = new RecursiveBruteForcePlacementStrategy();
        strategy.levelCalculator = new AbsoluteBottomUpLevelCalculator(strategy.fDCount);
        strategy.illegalFDsManager = new IllegalFDsManager();
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LexicographicalIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static RecursiveBruteForcePlacementStrategy makeABULRecursiveBruteForceStrategy() {
        RecursiveBruteForcePlacementStrategy strategy = new RecursiveBruteForcePlacementStrategy();
        strategy.levelCalculator = new AbsoluteBottomUpLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LexicographicalIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static RecursiveBruteForcePlacementStrategy makeABUFRecursiveBruteForceStrategy() {
        RecursiveBruteForcePlacementStrategy strategy = new RecursiveBruteForcePlacementStrategy();
        strategy.levelCalculator = new AbsoluteBottomUpLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LHSFirstIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static RecursiveBruteForcePlacementStrategy makeABUARecursiveBruteForceStrategy() {
        RecursiveBruteForcePlacementStrategy strategy = new RecursiveBruteForcePlacementStrategy();
        strategy.levelCalculator = new AbsoluteBottomUpLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new AlternatingIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static RecursiveBruteForcePlacementStrategy makeRBULRecursiveBruteForceStrategy() {
        RecursiveBruteForcePlacementStrategy strategy = new RecursiveBruteForcePlacementStrategy();
        strategy.levelCalculator = new RepetitiveBottomUpLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LexicographicalIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static RecursiveBruteForcePlacementStrategy makeRBUFRecursiveBruteForceStrategy() {
        RecursiveBruteForcePlacementStrategy strategy = new RecursiveBruteForcePlacementStrategy();
        strategy.levelCalculator = new RepetitiveBottomUpLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LHSFirstIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static RecursiveBruteForcePlacementStrategy makeRBUARecursiveBruteForceStrategy() {
        RecursiveBruteForcePlacementStrategy strategy = new RecursiveBruteForcePlacementStrategy();
        strategy.levelCalculator = new RepetitiveBottomUpLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new AlternatingIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static RecursiveBruteForcePlacementStrategy makeCFLRecursiveBruteForceStrategy() {
        RecursiveBruteForcePlacementStrategy strategy = new RecursiveBruteForcePlacementStrategy();
        strategy.levelCalculator = new CenterFirstLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LexicographicalIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static RecursiveBruteForcePlacementStrategy makeCFFRecursiveBruteForceStrategy() {
        RecursiveBruteForcePlacementStrategy strategy = new RecursiveBruteForcePlacementStrategy();
        strategy.levelCalculator = new CenterFirstLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LHSFirstIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static RecursiveBruteForcePlacementStrategy makeCFARecursiveBruteForceStrategy() {
        RecursiveBruteForcePlacementStrategy strategy = new RecursiveBruteForcePlacementStrategy();
        strategy.levelCalculator = new CenterFirstLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new AlternatingIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static RecursiveBruteForcePlacementStrategy makeMaRLRecursiveBruteForceStrategy() {
        RecursiveBruteForcePlacementStrategy strategy = new RecursiveBruteForcePlacementStrategy();
        strategy.levelCalculator = new MaxRatioLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LexicographicalIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static RecursiveBruteForcePlacementStrategy makeMaRFRecursiveBruteForceStrategy() {
        RecursiveBruteForcePlacementStrategy strategy = new RecursiveBruteForcePlacementStrategy();
        strategy.levelCalculator = new MaxRatioLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LHSFirstIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static RecursiveBruteForcePlacementStrategy makeMaRARecursiveBruteForceStrategy() {
        RecursiveBruteForcePlacementStrategy strategy = new RecursiveBruteForcePlacementStrategy();
        strategy.levelCalculator = new MaxRatioLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new AlternatingIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static RecursiveBruteForcePlacementStrategy makeMiRLRecursiveBruteForceStrategy() {
        RecursiveBruteForcePlacementStrategy strategy = new RecursiveBruteForcePlacementStrategy();
        strategy.levelCalculator = new MinRatioLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LexicographicalIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static RecursiveBruteForcePlacementStrategy makeMiRFRecursiveBruteForceStrategy() {
        RecursiveBruteForcePlacementStrategy strategy = new RecursiveBruteForcePlacementStrategy();
        strategy.levelCalculator = new MinRatioLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LHSFirstIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static RecursiveBruteForcePlacementStrategy makeMiRARecursiveBruteForceStrategy() {
        RecursiveBruteForcePlacementStrategy strategy = new RecursiveBruteForcePlacementStrategy();
        strategy.levelCalculator = new MinRatioLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new AlternatingIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static IterativeBruteForcePlacementStrategy makeABULIterativeBruteForceStrategyWithInvalidations() {
        IterativeBruteForcePlacementStrategy strategy = new IterativeBruteForcePlacementStrategy();
        strategy.levelCalculator = new AbsoluteBottomUpLevelCalculator(strategy.fDCount);
        strategy.illegalFDsManager = new IllegalFDsManager();
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LexicographicalIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static IterativeBruteForcePlacementStrategy makeABULIterativeBruteForceStrategy() {
        IterativeBruteForcePlacementStrategy strategy = new IterativeBruteForcePlacementStrategy();
        strategy.levelCalculator = new AbsoluteBottomUpLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LexicographicalIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static IterativeBruteForcePlacementStrategy makeABUFIterativeBruteForceStrategy() {
        IterativeBruteForcePlacementStrategy strategy = new IterativeBruteForcePlacementStrategy();
        strategy.levelCalculator = new AbsoluteBottomUpLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LHSFirstIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static IterativeBruteForcePlacementStrategy makeABUAIterativeBruteForceStrategy() {
        IterativeBruteForcePlacementStrategy strategy = new IterativeBruteForcePlacementStrategy();
        strategy.levelCalculator = new AbsoluteBottomUpLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new AlternatingIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static IterativeBruteForcePlacementStrategy makeRBULIterativeBruteForceStrategy() {
        IterativeBruteForcePlacementStrategy strategy = new IterativeBruteForcePlacementStrategy();
        strategy.levelCalculator = new RepetitiveBottomUpLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LexicographicalIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static IterativeBruteForcePlacementStrategy makeRBUFIterativeBruteForceStrategy() {
        IterativeBruteForcePlacementStrategy strategy = new IterativeBruteForcePlacementStrategy();
        strategy.levelCalculator = new RepetitiveBottomUpLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LHSFirstIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static IterativeBruteForcePlacementStrategy makeRBUAIterativeBruteForceStrategy() {
        IterativeBruteForcePlacementStrategy strategy = new IterativeBruteForcePlacementStrategy();
        strategy.levelCalculator = new RepetitiveBottomUpLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new AlternatingIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static IterativeBruteForcePlacementStrategy makeCFLIterativeBruteForceStrategy() {
        IterativeBruteForcePlacementStrategy strategy = new IterativeBruteForcePlacementStrategy();
        strategy.levelCalculator = new CenterFirstLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LexicographicalIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static IterativeBruteForcePlacementStrategy makeCFFIterativeBruteForceStrategy() {
        IterativeBruteForcePlacementStrategy strategy = new IterativeBruteForcePlacementStrategy();
        strategy.levelCalculator = new CenterFirstLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LHSFirstIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static IterativeBruteForcePlacementStrategy makeCFAIterativeBruteForceStrategy() {
        IterativeBruteForcePlacementStrategy strategy = new IterativeBruteForcePlacementStrategy();
        strategy.levelCalculator = new CenterFirstLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new AlternatingIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static IterativeBruteForcePlacementStrategy makeMaRLIterativeBruteForceStrategy() {
        IterativeBruteForcePlacementStrategy strategy = new IterativeBruteForcePlacementStrategy();
        strategy.levelCalculator = new MaxRatioLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LexicographicalIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static IterativeBruteForcePlacementStrategy makeMaRFIterativeBruteForceStrategy() {
        IterativeBruteForcePlacementStrategy strategy = new IterativeBruteForcePlacementStrategy();
        strategy.levelCalculator = new MaxRatioLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LHSFirstIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static IterativeBruteForcePlacementStrategy makeMaRAIterativeBruteForceStrategy() {
        IterativeBruteForcePlacementStrategy strategy = new IterativeBruteForcePlacementStrategy();
        strategy.levelCalculator = new MaxRatioLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new AlternatingIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static IterativeBruteForcePlacementStrategy makeMiRLIterativeBruteForceStrategy() {
        IterativeBruteForcePlacementStrategy strategy = new IterativeBruteForcePlacementStrategy();
        strategy.levelCalculator = new MinRatioLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LexicographicalIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static IterativeBruteForcePlacementStrategy makeMiRFIterativeBruteForceStrategy() {
        IterativeBruteForcePlacementStrategy strategy = new IterativeBruteForcePlacementStrategy();
        strategy.levelCalculator = new MinRatioLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new LHSFirstIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }

    public static IterativeBruteForcePlacementStrategy makeMiRAIterativeBruteForceStrategy() {
        IterativeBruteForcePlacementStrategy strategy = new IterativeBruteForcePlacementStrategy();
        strategy.levelCalculator = new MinRatioLevelCalculator(strategy.fDCount);
        strategy.iterators = new ArrayList<>();

        for(int i = 0 ; i <= strategy.configuration.getAmountOfAttributes(); i++) {
            strategy.iterators.add(
                    new AlternatingIterator(strategy.configuration.getAmountOfAttributes(), i));
        }
        return strategy;
    }
}
