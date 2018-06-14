package de.datageneration.generator;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

import de.datageneration.generator.placement.structures.FunctionalDependency;
import de.metanome.algorithms.hyfd.structures.FDTree;
import javafx.util.Pair;
import org.apache.lucene.util.OpenBitSet;

public class RecordGenerator extends  Pipe<List<Pair<FDTree, FDTree>>, Void>{
    private Configuration configuration;
    FDTree functionalDependencies;
    FDTree nonFDs;
    int value;

    @Override
    public Void process(List<Pair<FDTree, FDTree>> input) {
        configuration = Configuration.getConfiguration();
        String directoryName = "data/" + configuration.getOutputDirectory() + configuration.getName() + "-" +
                new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + "/";
        if (input == null) {
            exceptionalProcess(directoryName);
        } else {
            int i = 1;
            for (Pair<FDTree, FDTree> pair : input) {
                regularProcess(pair, directoryName + i + "/");
                i++;
            }
        }
        return null;
    }

    private void exceptionalProcess(String directoryName) {
        File outputFile = new File(directoryName + "noPlacement.txt");
        outputFile.getParentFile().mkdirs();
        try {
            copyConfig(directoryName);

            Writer writer = new FileWriter(outputFile);

            writer.append("No Placement found");

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void regularProcess(Pair<FDTree, FDTree> input, String directoryName) {
        functionalDependencies = input.getKey();
        nonFDs = input.getValue();

        if (configuration.isDebugMode()) {
            File outputFile = new File(directoryName + "FDs.txt");
            Util.writeFDTreeToFile(functionalDependencies, outputFile);
            outputFile = new File(directoryName + "nonFDs.txt");
            Util.writeFDTreeToFile(nonFDs, outputFile);
            copyConfig(directoryName);
        }
        generateAndWriteRecords(directoryName);
    }

    private void copyConfig(String directoryName) {
        File source = configuration.getConfigurationFile();
        File target = new File(directoryName + source.getName());
        try {
            Files.copy(source.toPath(), target.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateAndWriteRecords(String directoryName) {
        File outputFile = new File(directoryName + configuration.getName() + "Data.csv");
        outputFile.getParentFile().mkdirs();
        try {
            Writer writer = new FileWriter(outputFile);

            ArrayList<String> attributes = new ArrayList<>();
            ArrayList<Integer> attributeShuffle = new ArrayList<>();
            for (int i = 0; i < configuration.getAmountOfAttributes(); i++) {
                attributes.add("A" + i);
                attributeShuffle.add(i);
            }
            if (configuration.isHasHeader()) {
                CSVUtils.writeLine(writer, attributes);
            }

            if (!configuration.isDebugMode()) {
                Collections.shuffle(attributeShuffle);
            }

            ArrayList<ArrayList<String>> records = generateRecords(attributeShuffle);

            if (!configuration.isDebugMode()) {
                Collections.shuffle(records);
            }

            writeRecords(records, writer);

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<ArrayList<String>> generateRecords(ArrayList<Integer> attributeShuffle) {
        if (configuration.isGenerateMinimalRecords()) {
            return generateMinimalRecords(attributeShuffle);
        }

        if (configuration.getAmountOfRecords() == 0) {
            return generatePairwiseRecords(attributeShuffle);
        }

        ArrayList<ArrayList<String>> records = generatePairwiseRecords(attributeShuffle);
        if (records.size() > configuration.getAmountOfRecords()) {
            records = generateMinimalRecords(attributeShuffle);
        }

        int neededRecords = configuration.getAmountOfRecords() - records.size();
        if (neededRecords > 0) {
            records.addAll(generateAdditionalRecords(neededRecords, attributeShuffle));
        }
        else if (neededRecords < 0) {
            System.err.println("Warning: " + configuration.getAmountOfRecords() + " records requested, but at least " +
                    records.size() + " records need to be generated");
        }
        return records;
    }

    private ArrayList<ArrayList<String>> generateAdditionalRecords(int neededRecords, ArrayList<Integer> attributeShuffle) {
        ArrayList<ArrayList<String>> records = new ArrayList<>();
        if (neededRecords % 2 == 1) {
            records.add(generateAllEqualRecord(String.valueOf(value)));
            value++;
        }

        while (records.size() < neededRecords) {
            if (Thread.currentThread().isInterrupted()) {
                //throw new PlacementFailedException("Interrupted");
                System.err.println("Warning: Interrupted. Continuing to write files");
                break;
            }
            FunctionalDependency fd = Util.randomFD(nonFDs);
            OpenBitSet randomGeneralization = Util.getRandomGeneralization(fd.getLhs());
            records.addAll(generatePairOfRecords(randomGeneralization, String.valueOf(value), String.valueOf(value +1), attributeShuffle));
            value += 2;
        }
        return records;
    }

    private ArrayList<ArrayList<String>> generatePairOfRecords(OpenBitSet lhs, String value1, String value2, ArrayList<Integer> attributeShuffle) {
        ArrayList<ArrayList<String>> records = new ArrayList<>();
        records.add(generateAllEqualRecord(String.valueOf(value1)));

        OpenBitSet xStar = lhs.clone();

        ArrayList<String> valuesSecondTuple = generateTwoValuesRecord(xStar, value1, value2, attributeShuffle);
        records.add(valuesSecondTuple);

        return records;
    }

    private ArrayList<String> generateTwoValuesRecord(OpenBitSet value1positions, String value1, String value2, ArrayList<Integer> attributeShuffle) {
        ArrayList<String> record = new ArrayList<>();
        for (int i = 0; i < configuration.getAmountOfAttributes(); i++) {
            if (value1positions.get(attributeShuffle.get(i))) {
                record.add(String.valueOf(value1));
            } else {
                record.add(String.valueOf(value2));
            }
        }
        return record;
    }

    private ArrayList<String> generateAllEqualRecord(String value) {
        ArrayList<String> record = new ArrayList<>();
        for (int i = 0; i < configuration.getAmountOfAttributes(); i++) {
            record.add(value);
        }

        return record;
    }

    private ArrayList<ArrayList<String>> generateMinimalRecords(ArrayList<Integer> attributeShuffle) {
        value = 0;
        ArrayList<ArrayList<String>> records = new ArrayList<>();
        HashMap<OpenBitSet, Pair<String, String>> previousVals = new HashMap<>();
        records.add(generateAllEqualRecord("0"));
        value++;
        Util.forEachLHSdo(nonFDs, entry -> {
                    OpenBitSet lhs = entry.getLhs();
                    records.add(generateTwoValuesRecord(lhs, "0", String.valueOf(value), attributeShuffle));
                    value++;
                    return null;
                }
        );
        return records;
    }


    private ArrayList<ArrayList<String>> generatePairwiseRecords(ArrayList<Integer> attributeShuffle) {
        value = 0;
        ArrayList<ArrayList<String>> records = new ArrayList<>();
        Util.forEachLHSdo(nonFDs, entry -> {
            OpenBitSet lhs = entry.getLhs();
            records.addAll(generatePairOfRecords(lhs, String.valueOf(value), String.valueOf(value +1), attributeShuffle));
            value += 2;
            return null;
        }
        );
        return records;
    }

    private void writeRecords(ArrayList<ArrayList<String>> records, Writer writer) throws IOException {
        for (ArrayList<String> record : records) {
                CSVUtils.writeLine(writer, record);
        }
    }
}
