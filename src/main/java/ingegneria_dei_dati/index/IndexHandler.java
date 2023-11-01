package ingegneria_dei_dati.index;

import ingegneria_dei_dati.documents.TablesHandler;
import ingegneria_dei_dati.documents.ColumnRepresentation;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class IndexHandler implements IndexHandlerInterface{
    private IndexWriter writer;
    private IndexSearcher searcher;
    private final Directory directory;
    private Analyzer analyzer;

    public IndexHandler(String path) throws IOException {
        Path path_ = Paths.get(path);
        this.directory = FSDirectory.open(path_);
    }
    public void add2Index(ColumnRepresentation column) throws IOException {
        String tableId = column.getTableName();
        String columnId = column.getColumnName();
        List<String> text = column.getFields();

        Document doc = new Document();
        doc.add(new TextField("table_id", tableId, Field.Store.NO));
        doc.add(new TextField("column_id", columnId, Field.Store.NO));
        for (String value : text){
            doc.add(new TextField("text", value, Field.Store.NO));
        }
        this.writer.addDocument(doc);
    }
    @Override
    public void createIndex(String datasetPath, TablesHandler tablesHandler) throws IOException {
        this.analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(this.analyzer);
        this.writer = new IndexWriter(directory, config);
        this.writer.deleteAll();
        int i=0;
        while (tablesHandler.hasNextColumn()) {
            i += 1;
            ColumnRepresentation column = tablesHandler.readNextColumn();
            this.add2Index(column);
            if(i%1000 == 0) this.writer.commit();
            System.out.print("\rindexed columns: "+i);
        }
        this.writer.commit();
        this.writer.close();
        System.out.println("\rfinished indexing columns      ");
    }
    @Override
    public void search(Query query) throws IOException {
        try (IndexReader reader = DirectoryReader.open(this.directory)){
            this.searcher = new IndexSearcher(reader);
        }
    }
    @Override
    public Analyzer getAnalyzer() {
        return this.analyzer;
    }
}
