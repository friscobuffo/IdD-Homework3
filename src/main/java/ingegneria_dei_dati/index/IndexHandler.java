package ingegneria_dei_dati.index;

import ingegneria_dei_dati.reader.ColumnsReader;
import ingegneria_dei_dati.sample.SamplesHandler;
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
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class IndexHandler implements IndexHandlerInterface {
    private final Directory directory;
    private Analyzer analyzer;
    private int indexedTables;
    private String lastTableName;

    public IndexHandler(String path) throws IOException {
        Path path_ = Paths.get(path);
        this.directory = FSDirectory.open(path_);
    }
    private void add2Index(Column column, IndexWriter writer) throws IOException {
        String tableId = column.getTableName();
        String columnId = column.getColumnName();
        List<String> text = column.getFields();

        Document doc = new Document();
        doc.add(new TextField("table_id", tableId, Field.Store.NO));
        doc.add(new TextField("column_id", columnId, Field.Store.NO));
        for (String value : text){
            doc.add(new TextField("text", value, Field.Store.NO));
        }
        writer.addDocument(doc);
    }
    @Override
    public void createIndex(String datasetPath, ColumnsReader columnsReader) throws IOException {
        this.indexedTables = 0;
        this.analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(this.analyzer);
        IndexWriter writer = new IndexWriter(directory, config);
        writer.deleteAll();
        SamplesHandler samplesHandler = new SamplesHandler();
        samplesHandler.makeSample(1000);
        int i=0;
        while (columnsReader.hasNextColumn()) {
            i += 1;
            Column column = columnsReader.readNextColumn();
            samplesHandler.addToSampleProbabilistic(column);
            this.add2Index(column, writer);
            if(i%1000 == 0) writer.commit();
            this.prints(i, column.getTableName());
        }
        writer.commit();
        writer.close();
        samplesHandler.saveSample();
        System.out.println("\nfinished indexing columns\n");
    }
    @Override
    public void search(Query query) throws IOException {
        try (IndexReader reader = DirectoryReader.open(this.directory)){
            IndexSearcher searcher = new IndexSearcher(reader);
            searcher.setSimilarity(new IntersectionSimilarity());
            TopDocs hits = searcher.search(query, 10);

            //Print the count of matching documents.
            //just for debug purposes
            System.out.println("Found " + hits.totalHits.toString() + "!");

            //Print names and scores of matching documents.
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                System.out.println("Name: " + doc.get("name") + " --> Score: " + scoreDoc.score);
            }
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
