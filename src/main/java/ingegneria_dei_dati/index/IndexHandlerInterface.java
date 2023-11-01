package ingegneria_dei_dati.index;

import ingegneria_dei_dati.documents.TablesHandler;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;

import java.io.IOException;

public interface IndexHandlerInterface {
    void createIndex(String datasetPath, TablesHandler documentsHandler) throws IOException;
    void search(Query query) throws IOException;
    Analyzer getAnalyzer();
}
