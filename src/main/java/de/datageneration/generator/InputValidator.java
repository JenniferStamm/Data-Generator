package de.datageneration.generator;

import java.util.Map;

public class InputValidator extends Pipe<Object, Object> {


    @Override
    public Object process(Object input) {
        Configuration configuration = Configuration.getConfiguration();
        checkTotalFDCount(configuration);
        checkEachFDCount(configuration);

        return input;
    }

    private void checkEachFDCount(Configuration configuration) {
        for (Map.Entry<Integer, Integer> entry : configuration.getFDHistogram().entrySet()) {
            Integer requestedFDCount = entry.getValue();
            long maximumFDCountPossible = Util.numFDsInLevel(configuration.getAmountOfAttributes(), entry.getKey());
            if (requestedFDCount > maximumFDCountPossible) {
                throw new InvalidConfigurationException("Level " + entry.getKey() + ": " + requestedFDCount + " FDs requested, but only " + maximumFDCountPossible + " FDs possible");
            }
        }
    }

    private void checkTotalFDCount(Configuration configuration) {
        long maximumFDCountPossible = Util.numFDsInLevel(configuration.getAmountOfAttributes(), Math.floorDiv(configuration.getAmountOfAttributes(), 2));

        long requestedFDCount = configuration.getFDHistogram().values().stream().mapToInt(Integer::intValue).sum();

        if (requestedFDCount > maximumFDCountPossible) {
            throw new InvalidConfigurationException(requestedFDCount + " FDs requested, but only " + maximumFDCountPossible + " FDs possible");
        }
    }
}
