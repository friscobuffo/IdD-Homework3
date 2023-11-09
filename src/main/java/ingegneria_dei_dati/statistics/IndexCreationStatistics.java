package ingegneria_dei_dati.statistics;

import ingegneria_dei_dati.table.Cell;
import ingegneria_dei_dati.table.Column;
import ingegneria_dei_dati.table.Table;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class IndexCreationStatistics {
    public static int totalTables = 0;
    public static int totalRows = 0;
    public static int totalColumns = 0;
    public static int emptyCells = 0;
    public static int totalCells = 0;
    public static long startMilliseconds;
    public static double totalTime;
    public static Map<Integer, Integer> rowsNumber2tablesQuantity = new HashMap<>();
    public static Map<Integer, Integer> columnsNumber2tablesQuantity = new HashMap<>();
    public static Map<Integer, Integer> distinctValuesNumber2columnsQuantity = new HashMap<>();
    public static Map<Float, Integer> percentageRepeatedValues2columnsQuantity = new HashMap<>();
    public static Map<Integer, Integer> tokensNumber2cellsQuantity = new HashMap<>();

    public static void finishedIndexing() {
        totalTime = (System.currentTimeMillis() - startMilliseconds) / 1000.0;
    }
    public static void processTableStats(Table table) {
        if (totalTables==0) startMilliseconds = System.currentTimeMillis();
        // update of basic counters
        totalTables += 1;
        totalColumns += table.maxDimensions.column;
        totalRows += table.maxDimensions.row;
        int newValue;
        for (Cell cell: table.cells) {
            totalCells += 1;
            // update of emptyCells
            if (cell.cleanedText==null) {
                emptyCells += 1;
                continue;
            }
            else if (cell.cleanedText.isBlank()) emptyCells += 1;
            // update of tokensNumber2cellsQuantity
            int tokensNumber = List.of(cell.cleanedText.split(" ")).size();
            newValue = tokensNumber2cellsQuantity.getOrDefault(tokensNumber, 0)+1;
            tokensNumber2cellsQuantity.put(tokensNumber, newValue);
        }
        // update rowsNumber2tablesQuantity
        newValue = rowsNumber2tablesQuantity.getOrDefault(table.maxDimensions.row, 0)+1;
        rowsNumber2tablesQuantity.put(table.maxDimensions.row, newValue);
        // update columnsNumber2tablesQuantity
        newValue = columnsNumber2tablesQuantity.getOrDefault(table.maxDimensions.column, 0)+1;
        columnsNumber2tablesQuantity.put(table.maxDimensions.column, newValue);
        // update distinctValuesNumber2columnsQuantity and percentageRepeatedValues2columnsQuantity
        for (Column column: table.columns) {
            int distinctValues = (int) column.getFields().stream().distinct().count();
            // distinctValuesNumber2columnsQuantity
            newValue = distinctValuesNumber2columnsQuantity.getOrDefault(distinctValues,0)+1;
            distinctValuesNumber2columnsQuantity.put(distinctValues, newValue);
            // percentageRepeatedValues2columnsQuantity
            float percentageRepeatedValues = 100*(column.getFields().size() - distinctValues) / (float)column.getFields().size();
            percentageRepeatedValues = Math.round(10*percentageRepeatedValues) / (float)10;
            newValue = percentageRepeatedValues2columnsQuantity.getOrDefault(percentageRepeatedValues, 0)+1;
            percentageRepeatedValues2columnsQuantity.put(percentageRepeatedValues, newValue);
        }
    }
    public static void printStats() {
        System.out.print("totalTime ->");
        System.out.println(totalTime);
        System.out.print("totalTables -> ");
        System.out.println(totalTables);
        System.out.print("totalRows -> ");
        System.out.println(totalRows);
        System.out.print("totalColumns -> ");
        System.out.println(totalColumns);
        System.out.print("totalCells -> ");
        System.out.println(totalCells);
        System.out.print("emptyCells -> ");
        System.out.println(emptyCells);
        System.out.print("totalIndexCreationTime -> ");
        System.out.println(totalTime);
        System.out.print("rowsNumber2tablesQuantity -> ");
        System.out.println(rowsNumber2tablesQuantity);
        System.out.print("columnsNumber2tablesQuantity -> ");
        System.out.println(columnsNumber2tablesQuantity);
        System.out.print("distinctValuesNumber2columnsQuantity -> ");
        System.out.println(distinctValuesNumber2columnsQuantity);
        System.out.print("percentageRepeatedValues2columnsQuantity -> ");
        System.out.println(percentageRepeatedValues2columnsQuantity);
        System.out.print("tokensNumber2cellsQuantity -> ");
        System.out.println(tokensNumber2cellsQuantity);
    }
    public static void saveStats(String folderPath) {
        folderPath += "/";
        saveBasicStats(folderPath);
        saveMapStats(rowsNumber2tablesQuantity, "rowsNumber2tablesQuantity", folderPath);
        saveMapStats(columnsNumber2tablesQuantity, "columnsNumber2tablesQuantity", folderPath);
        saveMapStats(distinctValuesNumber2columnsQuantity, "distinctValuesNumber2columnsQuantity", folderPath);
        saveMapStats(percentageRepeatedValues2columnsQuantity, "percentageRepeatedValues2columnsQuantity", folderPath);
        saveMapStats(tokensNumber2cellsQuantity, "tokensNumber2cellsQuantity", folderPath);
    }
    private static <A,B> void saveMapStats(Map<A, B> map, String mapName, String folderPath) {
        try {
            Files.createDirectories(Paths.get(folderPath));
            FileWriter myWriter = new FileWriter(folderPath+mapName+".csv");
            for (A key : map.keySet()) {
                B value = map.get(key);
                String line = key.toString() + "," + value.toString() + "\n";
                myWriter.write(line);
            }
            myWriter.close();
        }
        catch (IOException ignored) { }
    }
    private static void saveBasicStats(String folderPath) {
        try {
            Files.createDirectories(Paths.get(folderPath));
            FileWriter myWriter = new FileWriter(folderPath+"basicStats.csv");
            String line = "totalTables," + totalTables + "\n";
            line += "totalRows," + totalRows + "\n";
            line += "totalColumns," + totalColumns + "\n";
            line += "emptyCells," + emptyCells + "\n";
            line += "totalIndexCreationTime," + totalTime + "\n";
            line += "totalCells," + totalCells + "\n";
            myWriter.write(line);
            myWriter.close();
        }
        catch (IOException ignored) { }
    }
    public static void saveStatsMakeHistograms(String folderPath) {
        IndexCreationStatistics.saveStats(folderPath);
        String path = System.getProperty("user.dir") + "/createHistograms.py";
        try {
            Runtime rt = Runtime.getRuntime();
            String command = "pip install matplotlib";
            Process pr = rt.exec(command);
            pr.waitFor();

            command = "python " + path;
            pr = rt.exec(command);
            pr.waitFor();

            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(pr.getErrorStream()));

            String line;
            while ((line = input.readLine()) != null)
                System.out.println(line);
            while ((line = error.readLine()) != null)
                System.out.println(line);
        } catch (Exception ignored) {
        }
    }
}
