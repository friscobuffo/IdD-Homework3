package ingegneria_dei_dati.json;

import ingegneria_dei_dati.utils.Triple;

import java.util.List;

public interface DocumentsRepresentable {
    List<Triple<String, String, List<String>>> getDocumentsRepresentation();
}