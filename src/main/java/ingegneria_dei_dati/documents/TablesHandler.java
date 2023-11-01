package ingegneria_dei_dati.documents;

public interface TablesHandler {
    boolean hasNextColumn();
    ColumnRepresentation readNextColumn();
}
