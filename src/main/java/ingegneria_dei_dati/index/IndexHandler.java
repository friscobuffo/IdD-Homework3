package ingegneria_dei_dati.index;

import ingegneria_dei_dati.reader.ColumnsReader;
import ingegneria_dei_dati.table.Column;
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
    private int indexedTables;
    private String lastTableName;

    public IndexHandler(String path) throws IOException {
        Path path_ = Paths.get(path);
        this.directory = FSDirectory.open(path_);
    }
    public void add2Index(Column column) throws IOException {
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
    public void createIndex(String datasetPath, ColumnsReader columnsReader) throws IOException {
        this.indexedTables = 0;
        this.analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(this.analyzer);
        this.writer = new IndexWriter(directory, config);
        this.writer.deleteAll();
        int i=0;
        while (columnsReader.hasNextColumn()) {
            i += 1;
            Column column = columnsReader.readNextColumn();
            this.add2Index(column);
            if(i%1000 == 0) this.writer.commit();
            this.prints(i, column.getTableName());
        }
        this.writer.commit();
        this.writer.close();
        System.out.println("\nfinished indexing columns\n");
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

    private void prints(int indexedColumns, String tableName) {
        if (!tableName.equals(this.lastTableName)) {
            this.indexedTables += 1;
            this.lastTableName = tableName;
        }
        System.out.print("\rindexed tables: "+this.indexedTables);
        System.out.print(" - ");
        System.out.print("indexed columns: "+indexedColumns);
    }
}
