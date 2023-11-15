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
    public static long totalTimeMilliseconds = 0;
    public static double totalTimeSeconds;
    public static long queriesNumber = 0;
    public static double totalGoodScores = 0;
    public static List<Float> goodScores = new ArrayList<>();
    public static double averageQueryTime;
    public static double averageGoodScore;
    public static double variance;
    public static double standardDeviation;
    public static double averageSimilarityRandomColumns;
    public static int counterNotFoundSelf;
    public static Map<Integer, Integer> columnSize2totalTimeMilliseconds = new HashMap<>();
    public static Map<Integer, Integer> columnSize2TotalQueriesNumber = new HashMap<>();
    public static Map<Integer, Double> columnSize2averageQueryTimeSeconds = new HashMap<>();

    public static void processExpansionStats(QueryResults queryResults, int queryTimeMilliseconds) {
        queriesNumber += 1;
        totalTimeMilliseconds += queryTimeMilliseconds;

        int queryColumnSize = queryResults.getQueryColumn().getFields().size();
        int totalTimeThisColumnSize = columnSize2totalTimeMilliseconds.getOrDefault(queryColumnSize, 0);
        columnSize2totalTimeMilliseconds.put(queryColumnSize, totalTimeThisColumnSize + queryTimeMilliseconds);
        int totalQueriesThisColumnSize = columnSize2TotalQueriesNumber.getOrDefault(queryColumnSize, 0);
        columnSize2TotalQueriesNumber.put(queryColumnSize, totalQueriesThisColumnSize + 1);

        if (!queryResults.getResults().isEmpty()) {
            QueryResults.Result bestResult = queryResults.getBestResult();
            if (bestResult != null) {
                goodScores.add(bestResult.queryScore);
                totalGoodScores += bestResult.queryScore;
            }
            float maxScore = queryResults.getResults().get(0).queryScore;
            if (maxScore != 1.0)
                counterNotFoundSelf += 1;
        }
    }
    public static void finishedExpandingColumns() {
        totalTimeSeconds = totalTimeMilliseconds / 1000.0;
        averageQueryTime = totalTimeSeconds / queriesNumber;
        averageGoodScore = totalGoodScores / goodScores.size();
        variance = 0;
        for (float score : goodScores)
            variance += ((averageGoodScore - score) * (averageGoodScore - score));
        variance /= (goodScores.size()-1);
        standardDeviation = Math.sqrt(variance);
        for (int columnSize : columnSize2TotalQueriesNumber.keySet()) {
            double averageTimeSeconds = (columnSize2totalTimeMilliseconds.get(columnSize) / (double)columnSize2TotalQueriesNumber.get(columnSize))/1000.0;
            columnSize2averageQueryTimeSeconds.put(columnSize, averageTimeSeconds);
        }
    }
    public static void saveStats(String folderPath) {
        try {
            Files.createDirectories(Paths.get(folderPath));
            FileWriter myWriter = new FileWriter(folderPath+"/querySampleStats.csv");
            String line = "totalTime," + totalTimeSeconds + "\n";
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
            IndexCreationStatistics.saveMapStats(columnSize2averageQueryTimeSeconds, "/columnSize2averageQueryTimeSeconds", folderPath);
        }
        catch (IOException ignored) { }
    }
    public static void printStats() {
        System.out.println("totalTime -> " + totalTimeSeconds);
        System.out.println("queriesNumber -> " + queriesNumber);
        System.out.println("totalGoodQueriesNumber -> " + goodScores.size());
        System.out.println("averageQueryTime -> " + averageQueryTime);
        System.out.println("totalGoodScores -> " + totalGoodScores);
        System.out.println("averageGoodScore -> " + averageGoodScore);
        System.out.println("variance -> " + variance);
        System.out.println("standardDeviation -> " + standardDeviation);
        System.out.println("averageSimilarityRandomColumns -> " + averageSimilarityRandomColumns);
        System.out.println("counterNotFoundSelf -> " + counterNotFoundSelf);
        System.out.println("columnSize2averageQueryTimeSeconds -> " + columnSize2averageQueryTimeSeconds);
    }
    public static void setAverageSimilarityRandomColumns(double averageSimilarityRandomColumns) {
        TableExpansionStatistics.averageSimilarityRandomColumns = averageSimilarityRandomColumns;
    }
}