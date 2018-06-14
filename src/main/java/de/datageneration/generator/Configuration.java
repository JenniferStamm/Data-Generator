package de.datageneration.generator;

import de.datageneration.generator.placement.PlacementStrategies;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Configuration {
    static private Configuration configuration = null;
    static private File configurationFile = null;
    private String name;
    private String outputDirectory;
    private int amountOfAttributes;
    private Integer candidateSize = null;
    private boolean isDebugMode;
    private int maximumLHSSize;
    private Map<Integer, Integer> fDHistogram = new HashMap<>();
    private List<PlacementStrategies> placementStrategies;
    private float confidence;
    private int repetitions;
    private int amountOfResults;
    private boolean generateMinimalRecords;
    private int amountOfRecords;
    private boolean hasHeader;

    protected Configuration() {
        if (configurationFile == null) {
            configurationFile = new File("config.properties");
        }
        loadProperties();
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    private void loadProperties() {
        Properties properties = new Properties();
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(configurationFile);
            properties.load(inStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setName(properties.getProperty("name", "noName"));
        setOutputDirectory(properties.getProperty("outputDirectory", ""));
        setAmountOfAttributes(properties.getProperty("amountOfAttributes", "5"));
        setDebugMode(properties.getProperty("debugMode", "0"));
        setMaximumSizeOfRequestedFD(properties.getProperty("maximumLHSSize", "0"));
        setFDHistogram(properties);
        setPlacementStrategies(properties.getProperty("strategy", ""));
        setConfidence(properties.getProperty("confidence", "0.99"));
        setRepetitions(properties.getProperty("repetitions", "1000"));
        setCandidateSize(properties.getProperty("candidateSize", null));
        setAmountOfResults(properties.getProperty("amountOfResults", "1"));
        setGenerateMinimalRecords(properties.getProperty("generateMinimalRecords", "0"));
        setAmountOfRecords(properties.getProperty("amountOfRecords", "0"));
        setHasHeader(properties.getProperty("hasHeader", "1"));
    }

    static public Configuration getConfiguration() {
        if (configuration == null) {
            configuration = new Configuration();
        }
        return configuration;
    }

    static public void setConfigurationFile(File input) {
        configurationFile = input;
        if (configuration != null) {
            configuration.loadProperties();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlacementStrategies> getPlacementStrategies() {
        return placementStrategies;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(String repetitions) {
        this.repetitions = Integer.parseInt(repetitions);
    }

    public Integer getCandidateSize() {
        return candidateSize;
    }

    public void setCandidateSize(String candidateSize) {
        if (candidateSize != null) {
            this.candidateSize = Integer.parseInt(candidateSize);
        } else {
            this.candidateSize = null;
        }
    }

    public void setPlacementStrategies(String placementStrategies) {
        this.placementStrategies = new ArrayList<>();

        String[] placementStrategiesArray = placementStrategies.split(",");
        for (String placementStrategy : placementStrategiesArray) {
            switch (placementStrategy.trim().toUpperCase()) {
                case "SIZE_BASED":
                    this.placementStrategies.add(PlacementStrategies.SIZE_BASED);
                    break;
                case "RANDOM":
                    this.placementStrategies.add(PlacementStrategies.RANDOM);
                    break;
                case "COST_BASED":
                    this.placementStrategies.add(PlacementStrategies.COST_BASED);
                    break;
                case "BRUTE_FORCE":
                    this.placementStrategies.add(PlacementStrategies.BRUTE_FORCE);
                    break;
                case "ABUL":
                    this.placementStrategies.add(PlacementStrategies.ABUL);
                    break;
                case "ABUF":
                    this.placementStrategies.add(PlacementStrategies.ABUF);
                    break;
                case "ABUA":
                    this.placementStrategies.add(PlacementStrategies.ABUA);
                    break;
                case "RBUL":
                    this.placementStrategies.add(PlacementStrategies.RBUL);
                    break;
                case "RBUF":
                    this.placementStrategies.add(PlacementStrategies.RBUF);
                    break;
                case "RBUA":
                    this.placementStrategies.add(PlacementStrategies.RBUA);
                    break;
                case "CFL":
                    this.placementStrategies.add(PlacementStrategies.CFL);
                    break;
                case "CFF":
                    this.placementStrategies.add(PlacementStrategies.CFF);
                    break;
                case "CFA":
                    this.placementStrategies.add(PlacementStrategies.CFA);
                    break;
                case "MaRL":
                    this.placementStrategies.add(PlacementStrategies.MaRL);
                    break;
                case "MaRF":
                    this.placementStrategies.add(PlacementStrategies.MaRF);
                    break;
                case "MaRA":
                    this.placementStrategies.add(PlacementStrategies.MaRA);
                    break;
                case "MiRL":
                    this.placementStrategies.add(PlacementStrategies.MiRL);
                    break;
                case "MiRF":
                    this.placementStrategies.add(PlacementStrategies.MiRF);
                    break;
                case "MiRA":
                    this.placementStrategies.add(PlacementStrategies.MiRA);
                    break;
                default:
                    this.placementStrategies.add(PlacementStrategies.BRUTE_FORCE);
                    break;
            }
        }
    }

    public File getConfigurationFile() {
        return configurationFile;
    }


    public  void setFDHistogram(Properties properties) {
        fDHistogram.clear();
        for (int i = 1; i <= maximumLHSSize; i++) {
            if (properties.getProperty(String.valueOf(i)) != null
                    && Integer.valueOf(properties.getProperty(String.valueOf(i))) > 0) {
                fDHistogram.put(i, Integer.valueOf(properties.getProperty(String.valueOf(i))));
            }
        }
    }

    public Map<Integer, Integer> getFDHistogram() {
        return fDHistogram;
    }

    public void setFDHistogram(Map<Integer, Integer> fDHistogram) {
        this.fDHistogram = fDHistogram;
    }

    public int getMaximumLHSSize() {
        return maximumLHSSize;
    }

    public void setMaximumLHSSize(int maximumLHSSize) {
        this.maximumLHSSize = maximumLHSSize;
    }

    public void setMaximumSizeOfRequestedFD(String maximumSizeOfRequestedFD) {
        this.maximumLHSSize = Integer.parseInt(maximumSizeOfRequestedFD);
    }

    public int getAmountOfAttributes() {
        return amountOfAttributes;
    }

    public void setAmountOfAttributes(int amountOfAttributes) {
        this.amountOfAttributes = amountOfAttributes;
    }

    public void setAmountOfAttributes(String amountOfAttributes) {
        this.amountOfAttributes = Integer.parseInt(amountOfAttributes);
    }

    public boolean isGenerateMinimalRecords() {
        return generateMinimalRecords;
    }

    public void setGenerateMinimalRecords(String generateMinimalRecords) {
        if (generateMinimalRecords.equals("1")
                || generateMinimalRecords.equals("true")
                || generateMinimalRecords.equals("True")) {
            this.generateMinimalRecords = true;
        } else {
            this.generateMinimalRecords = false;
        }
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = Float.parseFloat(confidence);
    }

    public int getAmountOfResults() { return amountOfResults; }

    public void setAmountOfResults(String amountOfResults) { this.amountOfResults = Integer.parseInt(amountOfResults); }

    public boolean isDebugMode() {
        return isDebugMode;
    }

    public void setDebugMode(String debug) {
        if (debug.equals("1")) {
            this.isDebugMode = true;
        } else {
            this.isDebugMode = false;
        }
    }

    public int getAmountOfRecords() {
        return amountOfRecords;
    }

    public void setAmountOfRecords(String amountOfRecords) {
        this.amountOfRecords = Integer.parseInt(amountOfRecords);
    }

    public boolean isHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(String hasHeader) {
        if (hasHeader.equals("1")) {
            this.hasHeader = true;
        } else {
            this.hasHeader = false;
        }

    }
}
