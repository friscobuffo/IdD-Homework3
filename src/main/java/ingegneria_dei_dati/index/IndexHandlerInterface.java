package ingegneria_dei_dati.index;

import ingegneria_dei_dati.reader.ColumnsReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;

import java.io.IOException;

public interface IndexHandlerInterface {
    void createIndex(String datasetPath, ColumnsReader documentsHandler) throws IOException;
    void search(Query query) throws IOException;
    Analyzer getAnalyzer();
}
