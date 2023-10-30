package ingegneria_dei_dati.json;

import ingegneria_dei_dati.utils.Triple;

import java.util.List;

public class DocumentsRepresentable {
    protected List<Triple<String, String, List<String>>> tableRepresentation;
    public List<Triple<String, String, List<String>>> getTableDocumentsRepresentation() {
        return this.tableRepresentation;
    }
}