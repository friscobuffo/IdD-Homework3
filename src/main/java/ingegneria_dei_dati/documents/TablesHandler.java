package ingegneria_dei_dati.documents;

import ingegneria_dei_dati.table.Column;

public interface TablesHandler {
    boolean hasNextColumn();
    Column readNextColumn();
}
