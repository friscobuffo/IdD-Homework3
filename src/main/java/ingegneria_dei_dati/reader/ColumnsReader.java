package ingegneria_dei_dati.reader;

import ingegneria_dei_dati.table.Column;

public interface ColumnsReader {
    boolean hasNextColumn();
    Column readNextColumn();
}
