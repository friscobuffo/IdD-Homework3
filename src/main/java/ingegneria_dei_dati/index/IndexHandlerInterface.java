package ingegneria_dei_dati.index;

import ingegneria_dei_dati.json.JsonHandler;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import ingegneria_dei_dati.utils.Triple;

import java.io.IOException;

public interface IndexHandlerInterface {
    public void createIndex(String datasetPath, JsonHandler jsonHandler) throws IOException;

    public void search(Query query) throws IOException;

    public Analyzer getAnalyzer();

}
