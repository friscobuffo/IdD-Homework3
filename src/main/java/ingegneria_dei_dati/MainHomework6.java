package ingegneria_dei_dati;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ingegneria_dei_dati.index.QueryResults;
import ingegneria_dei_dati.table.Column;
import ingegneria_dei_dati.table.Table;
import ingegneria_dei_dati.tableUtilities.TableExpander;

import java.io.*;
import java.util.*;

public class MainHomework6 {
    private static class Company {
        public String company_name;
    }
    public static List<Column> jsonToColumns(String jsonPath) throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(jsonPath);
        Company[] companies = gson.fromJson(reader, Company[].class);
        List<Column> columns = new ArrayList<>();
        int i = 0;
        List<String> fields = new ArrayList<>();
        int fieldsNumber = 50;
        for (Company company : companies) {
            i++;
            fields.add(company.company_name);
            if (i%fieldsNumber==0 || i==companies.length) {
                Column column = new Column();
                column.setTableName("United States");
                column.setColumnName(company.company_name);
                column.setFields(fields);
                fields = new ArrayList<>();
                columns.add((column));
            }
        }
        System.out.println(columns.size());
        return columns;
    }
    public static List<Table> getTables(Set<String> tablesNames) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("tables.json"));
        Gson gson = new Gson();
        String nextLine = reader.readLine();
        List<Table> result = new ArrayList<>();
        while (nextLine != null) {
            Table table = gson.fromJson(nextLine, Table.class);
            table.makeColumns();
            if (tablesNames.contains(table.id)) {
                result.add(table);
            }
            nextLine = reader.readLine();
        }
        reader.close();
        return result;
    }
    public static void main(String[] args) throws IOException {
        String indexPath = "index";
        TableExpander tableExpander = new TableExpander(indexPath);
        List<Column> columns = jsonToColumns("United States.json");
        Map<List<String>, Integer> tableColumn2occurrences = new HashMap<>();
        Map<List<String>, Float> tableColumn2totalScores = new HashMap<>();
        Set<String> tablesNames = new HashSet<>();
        int i=0;
        for (Column column : columns) {
            System.out.print("\rquery number: "+(++i));
            QueryResults queryResults = tableExpander.searchForColumnExpansion(column);
            for (QueryResults.Result result : queryResults.getResults()) {
                String tableName = result.tableName;
                String columnName = result.columnName;
                float queryScore = result.queryScore;
                if (queryScore<0.1) continue;
                tablesNames.add(tableName);
                List<String> key = new ArrayList<>();
                key.add(tableName);
                key.add(columnName);
                if (tableColumn2occurrences.containsKey(key)) {
                    tableColumn2occurrences.put(key, tableColumn2occurrences.get(key)+1);
                    tableColumn2totalScores.put(key, tableColumn2totalScores.get(key)+queryScore);
                }
                else {
                    tableColumn2occurrences.put(key, 1);
                    tableColumn2totalScores.put(key, queryScore);
                }
            }
        }
        System.out.println();
        System.out.println(tableColumn2occurrences.keySet().size());

        System.out.println("\nresults:");
        for (List<String> key : tableColumn2occurrences.keySet()) {
            System.out.println(key + " : " + (tableColumn2totalScores.get(key) / tableColumn2occurrences.get(key)));
        }

        List<Table> tables = getTables(tablesNames);
        System.out.println(tables.size());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        i=0;
        for (Table table: tables) {
            String jsonString = gson.toJson(table);
            String filePath = "prova/"+i+".json";
            i++;
            FileWriter writer = new FileWriter(filePath);
            writer.write(jsonString);
            writer.close();
        }
    }
}