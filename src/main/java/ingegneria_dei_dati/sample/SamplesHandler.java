package ingegneria_dei_dati.sample;

import com.google.gson.Gson;
import ingegneria_dei_dati.table.Column;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SamplesHandler {
    private int sampleSize;
    private List<Column> sample;
    private int currentElementIndex;
    private Random random;
    private final String sampleFileName = "/columnsSamples.json";

    public void makeSample(int sampleSize) {
        this.sampleSize = sampleSize;
        this.sample = new ArrayList<>();
        this.currentElementIndex = 0;
        this.random = new Random(12345);
    }
    public void addToSampleProbabilistic(Column column) {
        this.currentElementIndex++;
        if (this.sample.size() < this.sampleSize)
            this.sample.add(column);
        else {
            int v = random.nextInt(this.currentElementIndex+1);
            if (v < this.sampleSize) {
                this.sample.remove(v);
                this.sample.add(column);
            }
        }
    }
    public void saveSample(String folderPath) throws IOException{
        Files.createDirectories(Paths.get(folderPath));
        Gson gson = new Gson();
        FileWriter writer = new FileWriter(folderPath+sampleFileName);
        gson.toJson(this.sample, writer);
        writer.close();
    }
    public List<Column> readSample(String folderPath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(folderPath+sampleFileName));
        String line = reader.readLine();
        reader.close();
        Gson gson = new Gson();
        return List.of(gson.fromJson(line, Column[].class));
    }
    public List<List<Column>> divideSample(List<Column> sample, int desiredSamplesNumber) {
        int totalSampleSize = sample.size();
        int lastSampleSize = totalSampleSize % desiredSamplesNumber;
        if (lastSampleSize == 0) lastSampleSize = totalSampleSize / desiredSamplesNumber;
        int desiredSampleSize = (totalSampleSize-lastSampleSize) / (desiredSamplesNumber-1);
        List<List<Column>> dividedSamples= new ArrayList<>();
        for (int i=0; i<(desiredSamplesNumber-1); i++) {
            int startIndex = i*desiredSampleSize;
            int endIndex = (i+1)*desiredSampleSize;
            dividedSamples.add(sample.subList(startIndex, endIndex));
        }
        int startIndex = desiredSampleSize*(desiredSamplesNumber-1);
        int endIndex = startIndex + lastSampleSize;
        dividedSamples.add(sample.subList(startIndex, endIndex));
        return dividedSamples;
    }
}