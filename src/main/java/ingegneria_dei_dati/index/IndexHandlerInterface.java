package ingegneria_dei_dati.index;

import ingegneria_dei_dati.reader.ColumnsReader;
import ingegneria_dei_dati.tableUtilities.ExpansionStats;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;

import java.io.IOException;

public interface IndexHandlerInterface {
    void createIndex(String datasetPath, ColumnsReader documentsHandler) throws IOException;

    ExpansionStats search(Query query, int maxHits) throws IOException;

    Analyzer getAnalyzer();
}
