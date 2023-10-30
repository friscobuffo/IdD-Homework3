package ingegneria_dei_dati.index;

import ingegneria_dei_dati.utils.Triple;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;

import java.awt.*;
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

    @Override
    public void add2Index(Triple triple) throws IOException {
        String tableId = (String) triple.first;
        String columnId = (String) triple.second;
        List<String> text = (List<String>) triple.third;

        Document doc = new Document();
        doc.add(new TextField("table_id", tableId, Field.Store.NO));
        doc.add(new TextField("column_id", columnId, Field.Store.NO));
        for (String value : text){
            doc.add(new TextField("text", value, Field.Store.NO));
        }
        writer.addDocument(doc);

        writer.commit();
        writer.close();
    }


}
