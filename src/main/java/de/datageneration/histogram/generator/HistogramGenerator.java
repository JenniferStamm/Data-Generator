package de.datageneration.histogram.generator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistogramGenerator {

    public static void main(String[] args) {
        Map<Integer, Integer> histogram = new HashMap<>();
        try {
            File file = new File(args[0]);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                String[] functionalDependency = line.split("->");
                int lhsSize = functionalDependency[0].split(",").length;
                if (histogram.get(lhsSize) != null) {
                    histogram.put(lhsSize, histogram.get(lhsSize) + 1);
                } else {
                    histogram.put(lhsSize, 1);
                }
            }
            fileReader.close();

            File outputFile = new File(file.getParent() + "/" + file.getName().split("\\.")[0]
            + "Histogram.txt");
            Writer writer = new FileWriter(outputFile);

            for (Map.Entry<Integer, Integer> entry : histogram.entrySet()) {
                writer.write(entry.getKey() + " = " + entry.getValue() + "\n");
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
