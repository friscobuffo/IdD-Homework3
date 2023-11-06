package ingegneria_dei_dati;

import ingegneria_dei_dati.index.IndexHandler;
import ingegneria_dei_dati.reader.ColumnsReader;
import ingegneria_dei_dati.reader.JsonColumnsReader;
import ingegneria_dei_dati.statistics.IndexCreationStatistics;

import java.io.IOException;

public class MainCreateIndex {
    public static void main(String[] args) throws IOException {
        String datasetPath = "tables.json";
        String indexPath = "index";

        ColumnsReader columnsReader = new JsonColumnsReader(datasetPath);
        IndexHandler indexHandler = new IndexHandler(indexPath);
        indexHandler.createIndex(datasetPath, columnsReader);
        IndexCreationStatistics.printStats();
        String statsFolderPath = "stats";
        IndexCreationStatistics.saveStatsMakeHistograms(statsFolderPath);
    }
}
