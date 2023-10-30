package ingegneria_dei_dati.table;

import ingegneria_dei_dati.json.DocumentsRepresentable;
import ingegneria_dei_dati.utils.Triple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table extends DocumentsRepresentable {
    public String id;
    public Cell[] cells;

    @Override
    public List<Triple<String, String, List<String>>> tableDocumentsRepresentation() {
        Map<Integer, String> headerIndex2Name = new HashMap<>();
        Map<Integer, List<String>> columnIndex2elements = new HashMap<>();
        for (Cell cell: cells) {
            if (cell.isHeader) {
                headerIndex2Name.put(cell.Coordinates.column, cell.cleanedText);
                continue;
            }
            if (columnIndex2elements.containsKey(cell.Coordinates.column)) {
                columnIndex2elements.get(cell.Coordinates.column).add(cell.cleanedText);
            }
            else {
                List<String> elements = new ArrayList<>();
                elements.add(cell.cleanedText);
                columnIndex2elements.put(cell.Coordinates.column, elements);
            }
        }
        List<Triple<String, String, List<String>>> tableDocument = new ArrayList<>();
        for (Integer i: columnIndex2elements.keySet()) {
            Triple<String, String, List<String>> column = new Triple<>();
            column.first = this.id;
            column.second = headerIndex2Name.get(i);
            column.third = columnIndex2elements.get(i);
            tableDocument.add(column);
        }
        return tableDocument;
    }
}