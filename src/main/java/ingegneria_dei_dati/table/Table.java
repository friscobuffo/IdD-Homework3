package ingegneria_dei_dati.table;

import ingegneria_dei_dati.documents.ColumnRepresentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
    public String id;
    public Cell[] cells;
    public Coordinates maxDimensions;
    public static TablesStatistics tablesStatistics;

    public Table() {
        if (Table.tablesStatistics == null) Table.tablesStatistics = new TablesStatistics();
    }
    public List<ColumnRepresentation> getColumns() {
        Map<Integer, String> headerIndex2Name = new HashMap<>();
        Map<Integer, List<String>> columnIndex2elements = new HashMap<>();
        for (Cell cell: cells) {
            if (cell.isHeader) {
                headerIndex2Name.put(cell.Coordinates.column, cell.cleanedText);
                continue;
            }
            if (columnIndex2elements.containsKey(cell.Coordinates.column))
                columnIndex2elements.get(cell.Coordinates.column).add(cell.cleanedText);
            else {
                List<String> elements = new ArrayList<>();
                elements.add(cell.cleanedText);
                columnIndex2elements.put(cell.Coordinates.column, elements);
            }
        }
        List<ColumnRepresentation> columns = new ArrayList<>();
        for (Integer i: columnIndex2elements.keySet()) {
            ColumnRepresentation column = new ColumnRepresentation();
            column.setTableName(this.id);
            column.setColumnName(headerIndex2Name.get(i));
            column.setFields(columnIndex2elements.get(i));
            columns.add(column);
        }
        Table.tablesStatistics.processTableStats(this, columns);
        return columns;
    }
}