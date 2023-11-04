package ingegneria_dei_dati.tableUtilities;

public class ColumnStats {
    private final String tableId;
    private final String columnId;
    private float columnScore;

    public ColumnStats(String tableId, String columnId, float columnScore) {
        this.tableId = tableId;
        this.columnId = columnId;
        this.columnScore = columnScore;
    }
    @Override
    public String toString() {
        return "- TableId: " + tableId + " ColumnHeader: " + columnId + " --> Score: " + columnScore;
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
