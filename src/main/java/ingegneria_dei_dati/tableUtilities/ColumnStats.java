package ingegneria_dei_dati.tableUtilities;

public class ColumnStats {
    private final String tableId;
    private final String columnId;
    private float columnScore;

    private final String redColor = "\u001B[31m";
    private final String greenColor = "\u001B[32m";
    private final String resetColor = "\u001B[0m";
    private final String blueColor = "\u001B[34m";
    private final String orangeColor = "\u001B[33m";
    private final String italic = "\u001B[3m";
    private final String reset = "\u001B[0m";

    public ColumnStats(String tableId, String columnId, float columnScore) {
        this.tableId = tableId;
        this.columnId = columnId;
        this.columnScore = columnScore;
    }
    @Override
    public String toString() {
        return "- " + orangeColor + "TableId: " + resetColor + tableId +
                " ColumnHeader: " + blueColor + columnId + resetColor +
                " --> Score: " + greenColor + italic + columnScore + reset + resetColor;
    }
    public void normalize(int columnLen) {
        this.columnScore /= columnLen;
    }
    public String getTableId() {
        return this.tableId;
    }
    public String getColumnId() {
        return this.columnId;
    }
    public float getColumnScore() {
        return columnScore;
    }
}
