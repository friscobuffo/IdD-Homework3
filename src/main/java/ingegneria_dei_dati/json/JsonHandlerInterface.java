package ingegneria_dei_dati.json;

import ingegneria_dei_dati.utils.Triple;

import java.util.List;

public interface JsonHandlerInterface {
    public boolean hasNextDocument();
    public Triple<String, String, List<String>>  readNextDocument();
}
