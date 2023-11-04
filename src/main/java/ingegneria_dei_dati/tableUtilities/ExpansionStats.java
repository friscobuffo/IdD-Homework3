package ingegneria_dei_dati.tableUtilities;

import java.util.ArrayList;
import java.util.List;

public class ExpansionStats {

    private final long hits;

    private final List<ColumnStats> columnsStats;

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
        stringBuilder.append("TotHits: ")
        .append(this.hits)
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
}
