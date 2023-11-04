package ingegneria_dei_dati.index;

import ingegneria_dei_dati.reader.ColumnsReader;
import ingegneria_dei_dati.reader.JsonColumnsReader;
import ingegneria_dei_dati.sample.SamplesHandler;
import ingegneria_dei_dati.statistics.Statistics;
import ingegneria_dei_dati.table.Column;
import ingegneria_dei_dati.tableUtilities.ExpansionStats;
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
    private final Analyzer analyzer;
    private int indexedTables;
    private String lastTableName;

    public IndexHandler(String path) throws IOException {
        Path path_ = Paths.get(path);
        this.directory = FSDirectory.open(path_);
        this.analyzer = new StandardAnalyzer();
    }
    private void add2Index(Column column, IndexWriter writer) throws IOException {
        String tableId = column.getTableName();
        String columnId = column.getColumnName();
        List<String> text = column.getFields();

        Document doc = new Document();
        doc.add(new TextField("table_id", tableId, Field.Store.YES));
        doc.add(new TextField("column_id", columnId, Field.Store.YES));
        for (String value : text){
            doc.add(new TextField("text", value, Field.Store.NO));
        }
        writer.addDocument(doc);
    }
    @Override
    public void createIndex(String datasetPath, ColumnsReader columnsReader) throws IOException {
        this.indexedTables = 0;
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
    public ExpansionStats search(Query query, int maxHits) throws IOException {
        try (IndexReader reader = DirectoryReader.open(this.directory)){
            IndexSearcher searcher = new IndexSearcher(reader);
            searcher.setSimilarity(new IntersectionSimilarity());
            TopDocs hits = searcher.search(query, maxHits);

            ExpansionStats expansionStats = new ExpansionStats(hits.totalHits.value);


            //Print names and scores of matching documents.
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                expansionStats.addColumnStat(doc.get("table_id"),doc.get("column_id"),scoreDoc.score);
            }

            return expansionStats;
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

    public static void main(String[] args) throws IOException {
        String datasetPath = "tables.json";
        String indexPath = "index";

        ColumnsReader columnsReader = new JsonColumnsReader(datasetPath);
        IndexHandler indexHandler = new IndexHandler(indexPath);
        indexHandler.createIndex(datasetPath, columnsReader);
        Statistics.printStats();
        Statistics.saveStatsMakeHistograms();
    }
}
