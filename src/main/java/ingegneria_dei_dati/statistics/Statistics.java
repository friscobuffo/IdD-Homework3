package ingegneria_dei_dati.statistics;

import ingegneria_dei_dati.table.Cell;
import ingegneria_dei_dati.table.Column;
import ingegneria_dei_dati.table.Table;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Statistics {
    public static int totalTables = 0;
    public static int totalRows = 0;
    public static int totalColumns = 0;
    public static int emptyCells = 0;
    public static Map<Integer, Integer> rowsNumber2tablesQuantity = new HashMap<>();
    public static Map<Integer, Integer> columnsNumber2tablesQuantity = new HashMap<>();
    public static Map<Integer, Integer> distinctValuesNumber2columnsQuantity = new HashMap<>();

    public static void processTableStats(Table table) {
        // update of basic counters
        Statistics.totalTables += 1;
        Statistics.totalColumns += table.maxDimensions.column;
        Statistics.totalRows += table.maxDimensions.row;
        // update of emptyCells
        for (Cell cell: table.cells) {
            if (cell.cleanedText==null) Statistics.emptyCells+=1;
            else if (cell.cleanedText.isBlank()) Statistics.emptyCells += 1;
        }
        // update rowsNumber2tablesQuantity
        if (Statistics.rowsNumber2tablesQuantity.containsKey(table.maxDimensions.row)) {
            int newValue = Statistics.rowsNumber2tablesQuantity.get(table.maxDimensions.row)+1;
            Statistics.rowsNumber2tablesQuantity.put(table.maxDimensions.row, newValue);
        }
        else rowsNumber2tablesQuantity.put(table.maxDimensions.row, 1);
        // update columnsNumber2tablesQuantity
        if (Statistics.columnsNumber2tablesQuantity.containsKey(table.maxDimensions.column)) {
            int newValue = Statistics.columnsNumber2tablesQuantity.get(table.maxDimensions.column)+1;
            Statistics.columnsNumber2tablesQuantity.put(table.maxDimensions.column, newValue);
        }
        else columnsNumber2tablesQuantity.put(table.maxDimensions.column, 1);
        // update distinctValuesNumber2columnsQuantity
        for (Column column: table.columns) {
            int distinctValues = (int) column.getFields().stream().distinct().count();
            if (Statistics.distinctValuesNumber2columnsQuantity.containsKey(distinctValues)) {
                int newValue = Statistics.distinctValuesNumber2columnsQuantity.get(distinctValues)+1;
                Statistics.distinctValuesNumber2columnsQuantity.put(distinctValues, newValue);
            }
            else Statistics.distinctValuesNumber2columnsQuantity.put(distinctValues, 1);
        }
    }
    public static void printStats() {
        System.out.print("totalTables -> ");
        System.out.println(Statistics.totalTables);
        System.out.print("totalRows -> ");
        System.out.println(Statistics.totalRows);
        System.out.print("totalColumns -> ");
        System.out.println(Statistics.totalColumns);
        System.out.print("emptyCells -> ");
        System.out.println(Statistics.emptyCells);
        System.out.print("rowsNumber2tablesQuantity -> ");
        System.out.println(Statistics.rowsNumber2tablesQuantity);
        System.out.print("columnsNumber2tablesQuantity -> ");
        System.out.println(Statistics.columnsNumber2tablesQuantity);
        System.out.print("distinctValuesNumber2columnsQuantity -> ");
        System.out.println(Statistics.distinctValuesNumber2columnsQuantity);
    }

    public static void saveStats() {
        saveBasicStats();
        saveMapStats(Statistics.rowsNumber2tablesQuantity, "rowsNumber2tablesQuantity");
        saveMapStats(Statistics.columnsNumber2tablesQuantity, "columnsNumber2tablesQuantity");
        saveMapStats(Statistics.distinctValuesNumber2columnsQuantity, "distinctValuesNumber2columnsQuantity");
    }

    private static <A,B> void saveMapStats(Map<A, B> map, String mapName) {
        String path = "stats/";
        try {
            Files.createDirectories(Paths.get(path));
            FileWriter myWriter = new FileWriter(path+mapName+".csv");
            for (A key : map.keySet()) {
                B value = map.get(key);
                String line = key.toString() + "," + value.toString() + "\n";
                myWriter.write(line);
            }
            myWriter.close();
        }
        catch (IOException ignored) { }
    }
    private static void saveBasicStats() {
        String path = "stats/";
        try {
            Files.createDirectories(Paths.get(path));
            FileWriter myWriter = new FileWriter(path+"basicStats.csv");
            String line = "totalTables," + Statistics.totalTables + "\n";
            line += "totalRows," + Statistics.totalRows + "\n";
            line += "totalColumns," + Statistics.totalColumns + "\n";
            line += "emptyCells," + Statistics.emptyCells + "\n";
            myWriter.write(line);
            myWriter.close();
        }
        catch (IOException ignored) { }
    }
    public static void saveStatsMakeHistograms() {
        Statistics.saveStats();
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
            while((line=input.readLine()) != null)
                System.out.println(line);
            while((line=error.readLine()) != null)
                System.out.println(line);
        }
        catch (Exception ignored) { }
    }
}
