package ingegneria_dei_dati.statistics;

import ingegneria_dei_dati.index.QueryResults;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableExpansionStatistics {
    public static long startTimeMilliseconds;
    public static double totalTime;
    public static long queriesNumber = 0;
    public static double totalGoodScores = 0;
    public static List<Float> goodScores = new ArrayList<>();
    public static double averageQueryTime;
    public static double averageGoodScore;
    public static double variance;
    public static double standardDeviation;
    public static double averageSimilarityRandomColumns;
    public static int counterNotFoundSelf;
    public static Map<Integer, Double> columnSize2totalTime = new HashMap<>();
    public static Map<Integer, Integer> columnSize2TotalQueriesNumber = new HashMap<>();

    public static void processExpansionStats(QueryResults queryResults) {
        if (queriesNumber==0) startTimeMilliseconds = System.currentTimeMillis();
        queriesNumber += 1;
        if (!queryResults.getResults().isEmpty()) {
            QueryResults.Result bestResult = queryResults.getBestResult();
            if (bestResult != null) {
                goodScores.add(bestResult.queryScore);
                totalGoodScores += bestResult.queryScore;
                if (bestResult.queryScore != 1)
                    counterNotFoundSelf += 1;
            }
        }
    }
    public static void finishedExpandingColumns() {
        totalTime = (System.currentTimeMillis() - startTimeMilliseconds) / 1000.0;
        averageQueryTime = totalTime / queriesNumber;
        averageGoodScore = totalGoodScores / goodScores.size();
        variance = 0;
        for (float score : goodScores)
            variance += ((averageGoodScore - score) * (averageGoodScore - score));
        variance /= (goodScores.size()-1);
        standardDeviation = Math.sqrt(variance);
    }
    public static void saveStats(String folderPath) {
        try {
            Files.createDirectories(Paths.get(folderPath));
            FileWriter myWriter = new FileWriter(folderPath+"/querySampleStats.csv");
            String line = "totalTime," + totalTime + "\n";
            line += "queriesNumber," + queriesNumber + "\n";
            line += "totalGoodQueriesNumber," + goodScores.size() + "\n";
            line += "averageQueryTime," + averageQueryTime + "\n";
            line += "totalGoodScores," + totalGoodScores + "\n";
            line += "averageGoodScore," + averageGoodScore + "\n";
            line += "variance," + variance + "\n";
            line += "standardDeviation," + standardDeviation + "\n";
            line += "averageSimilarityRandomColumns," + averageSimilarityRandomColumns + "\n";
            line += "counterNotFoundSelf," + counterNotFoundSelf + "\n";
            myWriter.write(line);
            myWriter.close();
        }
        catch (IOException ignored) { }
    }
    public static void printStats() {
        System.out.println("totalTime -> " + totalTime);
        System.out.println("queriesNumber -> " + queriesNumber);
        System.out.println("totalGoodQueriesNumber -> " + goodScores.size());
        System.out.println("averageQueryTime -> " + averageQueryTime);
        System.out.println("totalGoodScores -> " + totalGoodScores);
        System.out.println("averageGoodScore -> " + averageGoodScore);
        System.out.println("variance -> " + variance);
        System.out.println("standardDeviation -> " + standardDeviation);
        System.out.println("averageSimilarityRandomColumns -> " + averageSimilarityRandomColumns);
        System.out.println("counterNotFoundSelf -> " + counterNotFoundSelf);
    }
    public static void setAverageSimilarityRandomColumns(double averageSimilarityRandomColumns) {
        TableExpansionStatistics.averageSimilarityRandomColumns = averageSimilarityRandomColumns;
    }
}