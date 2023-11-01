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

        ColumnsReader columnsReader = new JsonColumnsReader(datasetPath);
        /* Al momento glielo passo come parametro di Input poi faremo le dovute scelte a riguardo,
         *   avevo pensato di creare l'istanza direttamente nel mentodo ma così, sempre e comunque diamo già per scontato
         *   che nella creazione dell'indice utilizzaimo sempre un file Json */
        IndexHandler indexHandler = new IndexHandler(indexPath);
        indexHandler.createIndex(datasetPath, columnsReader); // Lo so è grezzo ma è temporaneo per non avere errori sul main

        Statistics.printStats();
        Statistics.saveStatsMakeHistograms();
    }
}