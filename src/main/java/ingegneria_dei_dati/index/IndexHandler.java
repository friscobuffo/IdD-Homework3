package ingegneria_dei_dati.index;

import ingegneria_dei_dati.utils.Triple;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;

import java.io.IOException;
import java.util.List;

public class IndexHandler implements IndexHandlerInterface{

    private final IndexWriter writer;

    public IndexHandler(Directory dir) throws IOException {
        //Analyzer defaultAnalyzer = new StandardAnalyzer();
        //IndexWriterConfig config = new IndexWriterConfig(defaultAnalyzer);

        //IndexWriter writer = new IndexWriter(directory, config);
        this.writer = new IndexWriter(dir, new IndexWriterConfig());
    }

    public void add2Index(Triple<String, String, List<String>> triple) throws IOException {
        String tableId = triple.first;
        String columnId = triple.second;
        List<String> text = triple.third;

        Document doc = new Document();
        doc.add(new TextField("table_id", tableId, Field.Store.NO));
        doc.add(new TextField("column_id", columnId, Field.Store.NO));
        for (String value : text){
            doc.add(new TextField("text", value, Field.Store.NO));
        }
        writer.addDocument(doc);

        writer.commit();
        //writer.close();
    }


    @Override
    public void createIndex(String datasetPath) {

    }

    @Override
    public void search(Query query) {

    }

    @Override
    public Analyzer getAnalyzer() {
        return null;
    }
}
