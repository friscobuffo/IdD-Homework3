package ingegneria_dei_dati.index;

import ingegneria_dei_dati.table.Column;

import java.util.ArrayList;
import java.util.List;

public class QueryResults {
    public static class Result {
        public String tableName;
        public String columnName;
        public float queryScore;
        private void normalize(int columnLen) {
            this.queryScore /= columnLen;
        }
        @Override
        public String toString() {
            return "- " + orangeColor + "TableId: " + resetColor + tableName +
                    " ColumnHeader: " + blueColor + columnName + resetColor +
                    " --> Score: " + greenColor + italic + queryScore + reset + resetColor;
        }
    }
    private final long totalHits;
    private final List<Result> results;
    private Column queryColumn;
    private static final String bold = "\u001B[1m";
    private static final String greenColor = "\u001B[32m";
    private static final String resetColor = "\u001B[0m";
    private static final String reset = "\u001B[0m";
    @SuppressWarnings(value = "unused")
    private static final String redColor = "\u001B[31m";
    private static final String blueColor = "\u001B[34m";
    private static final String orangeColor = "\u001B[33m";
    private static final String italic = "\u001B[3m";
    public QueryResults(long totalHits) {
        this.totalHits = totalHits;
        this.results = new ArrayList<>();
    }
    public void addResult(String tableName, String columnName, float queryScore) {
        Result result = new Result();
        result.tableName = tableName;
        result.columnName = columnName;
        result.queryScore = queryScore;
        results.add(result);
    }
    public void normalize(int columnLen){
        for(Result result : this.results)
            result.normalize(columnLen);
    }
    public List<Result> getResults() {
        return results;
    }
    public void setQueryColumn(Column queryColumn) {
        this.queryColumn = queryColumn;
    }
    public Result getBestResult() {
        for (Result result : this.results)
            if (!result.tableName.equals(this.queryColumn.getTableName()))
                return result;
        return null;
    }
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(bold)
                .append("TotHits: ")
                .append(greenColor)
                .append(this.totalHits)
                .append(resetColor)
                .append(reset)
                .append('\n');
        for(Result result : this.results)
            stringBuilder.append(result.toString()).append('\n');
        return stringBuilder.toString();
    }
}
