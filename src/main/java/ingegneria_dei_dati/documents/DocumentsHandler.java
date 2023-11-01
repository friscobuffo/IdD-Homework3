package ingegneria_dei_dati.documents;

import ingegneria_dei_dati.utils.Triple;

import java.util.List;

public interface DocumentsHandler {
    boolean hasNextDocument();
    Triple<String, String, List<String>>  readNextDocument();
}
