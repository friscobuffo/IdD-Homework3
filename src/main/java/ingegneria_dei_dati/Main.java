package ingegneria_dei_dati;

import ingegneria_dei_dati.index.IndexHandler;
import ingegneria_dei_dati.reader.JsonColumnsReader;
import ingegneria_dei_dati.reader.ColumnsReader;
import ingegneria_dei_dati.statistics.Statistics;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String datasetPath = "tables.json";
        String indexPath = "index";

        //ColumnsReader columnsReader = new JsonColumnsReader(datasetPath);
        //IndexHandler indexHandler = new IndexHandler(indexPath);
        //indexHandler.createIndex(datasetPath, columnsReader);
        Statistics.printStats();
        Statistics.saveStatsMakeHistograms();
    }
}