package de.datageneration.generator;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class DataGenerator {

    public static void main(String[] args) {

        try {
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File configurationFile;
        if (args.length > 0) {
            configurationFile = new File(args[0]);
            System.out.println("Testing with " + args[0]);
         } else {
            configurationFile = new File("config.properties");
        }
        Configuration.setConfigurationFile(configurationFile);

        // Begin pipeline
        final InputValidator inputValidator = new InputValidator();
        final FunctionalDependencyPlacer functionalDependencyPlacer = new FunctionalDependencyPlacer();
        final NonFunctionalDependencyGenerator nonFunctionalDependencyGenerator = new NonFunctionalDependencyGenerator();
        final RecordGenerator recordGenerator = new RecordGenerator();

        GeneratorPipeline<File, String> pipeline = new GeneratorPipeline<>(
                inputValidator,
                functionalDependencyPlacer,
                nonFunctionalDependencyGenerator,
                recordGenerator
        );
        pipeline.process(null);
    }

}
