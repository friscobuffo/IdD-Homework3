package ingegneria_dei_dati.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import ingegneria_dei_dati.utils.Triple;

import java.io.IOException;

public interface IndexHandlerInterface {
    public void createIndex(String datasetPath);

    public void search(Query query);

    public Analyzer getAnalyzer();

}
