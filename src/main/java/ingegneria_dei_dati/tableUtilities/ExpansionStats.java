package ingegneria_dei_dati.tableUtilities;

import java.util.ArrayList;
import java.util.List;

public class ExpansionStats {
    private final long hits;
    private final List<ColumnStats> columnsStats;
    private final String bold = "\u001B[1m";
    private final String greenColor = "\u001B[32m";
    private final String resetColor = "\u001B[0m";
    private final String reset = "\u001B[0m";

    public ExpansionStats(long hits) {
        this.hits = hits;
        this.columnsStats = new ArrayList<ColumnStats>();
    }
    public void addColumnStat(String tableId, String columnId, float columnScore){
        this.columnsStats.add(new ColumnStats(tableId, columnId, columnScore));
    }
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(bold)
                .append("TotHits: ")
                .append(greenColor)
                .append(this.hits)
                .append(resetColor)
                .append(reset)
                .append('\n');
        for(ColumnStats columnStats : this.columnsStats){
            stringBuilder.append(columnStats.toString()).append('\n');
        }
        return stringBuilder.toString();
    }
    public void normalize(int columnLen){
        for(ColumnStats columnStats : this.columnsStats){
            columnStats.normalize(columnLen);
        }
    }
    public List<ColumnStats> getColumnsStats() {
        return this.columnsStats;
    }
}
