package ingegneria_dei_dati.table;

import ingegneria_dei_dati.documents.ColumnRepresentation;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class TablesStatistics {
    public int totalTables = 0;
    public int totalRows = 0;
    public int totalColumns = 0;
    public int emptyCells = 0;
    public Map<Integer, Integer> rowsNumber2tablesQuantity = new HashMap<>();
    public Map<Integer, Integer> columnsNumber2tablesQuantity = new HashMap<>();
    public Map<Integer, Integer> distinctValuesNumber2columnsQuantity = new HashMap<>();

    public void processTableStats(Table table, List<ColumnRepresentation> tableRepresentation) {
        // update of basic counters
        this.totalTables += 1;
        this.totalColumns += table.maxDimensions.column;
        this.totalRows += table.maxDimensions.row;
        // update of emptyCells
        for (Cell cell: table.cells) {
            if (cell.cleanedText==null) this.emptyCells+=1;
            else if (cell.cleanedText.isBlank()) this.emptyCells += 1;
        }
        // update rowsNumber2tablesQuantity
        if (this.rowsNumber2tablesQuantity.containsKey(table.maxDimensions.row)) {
            int newValue = this.rowsNumber2tablesQuantity.get(table.maxDimensions.row)+1;
            this.rowsNumber2tablesQuantity.put(table.maxDimensions.row, newValue);
        }
        else rowsNumber2tablesQuantity.put(table.maxDimensions.row, 1);
        // update columnsNumber2tablesQuantity
        if (this.columnsNumber2tablesQuantity.containsKey(table.maxDimensions.column)) {
            int newValue = this.columnsNumber2tablesQuantity.get(table.maxDimensions.column)+1;
            this.columnsNumber2tablesQuantity.put(table.maxDimensions.column, newValue);
        }
        else columnsNumber2tablesQuantity.put(table.maxDimensions.column, 1);
        // update distinctValuesNumber2columnsQuantity
        for (ColumnRepresentation column: tableRepresentation) {
            int distinctValues = (int) column.getFields().stream().distinct().count();
            if (this.distinctValuesNumber2columnsQuantity.containsKey(distinctValues)) {
                int newValue = this.distinctValuesNumber2columnsQuantity.get(distinctValues)+1;
                this.distinctValuesNumber2columnsQuantity.put(distinctValues, newValue);
            }
            else this.distinctValuesNumber2columnsQuantity.put(distinctValues, 1);
        }
    }
    public void printStats() {
        System.out.print("totalTables -> ");
        System.out.println(this.totalTables);
        System.out.print("totalRows -> ");
        System.out.println(this.totalRows);
        System.out.print("totalColumns -> ");
        System.out.println(this.totalColumns);
        System.out.print("emptyCells -> ");
        System.out.println(this.emptyCells);
        System.out.print("rowsNumber2tablesQuantity -> ");
        System.out.println(this.rowsNumber2tablesQuantity);
        System.out.print("columnsNumber2tablesQuantity -> ");
        System.out.println(this.columnsNumber2tablesQuantity);
        System.out.print("distinctValuesNumber2columnsQuantity -> ");
        System.out.println(this.distinctValuesNumber2columnsQuantity);
    }

    public void saveStats() {
        saveBasicStats();
        saveMapStats(this.rowsNumber2tablesQuantity, "rowsNumber2tablesQuantity");
        saveMapStats(this.columnsNumber2tablesQuantity, "columnsNumber2tablesQuantity");
        saveMapStats(this.distinctValuesNumber2columnsQuantity, "distinctValuesNumber2columnsQuantity");
    }

    private <A,B> void saveMapStats(Map<A, B> map, String mapName) {
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
    private void saveBasicStats() {
        String path = "stats/";
        try {
            Files.createDirectories(Paths.get(path));
            FileWriter myWriter = new FileWriter(path+"basicStats.csv");
            String line = "totalTables," + this.totalTables + "\n";
            line += "totalRows," + this.totalRows + "\n";
            line += "totalColumns," + this.totalColumns + "\n";
            line += "emptyCells," + this.emptyCells + "\n";
            myWriter.write(line);
            myWriter.close();
        }
        catch (IOException ignored) { }
    }
    public void runPythonScriptHistograms() {
        saveStats();
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
