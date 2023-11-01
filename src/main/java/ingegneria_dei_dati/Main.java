package ingegneria_dei_dati;

import ingegneria_dei_dati.index.IndexHandler;
import ingegneria_dei_dati.documents.JsonHandler;
import ingegneria_dei_dati.documents.TablesHandler;
import ingegneria_dei_dati.table.Table;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String datasetPath = "tables.json";
        String indexPath = "index";

        TablesHandler jsonHandler = new JsonHandler(datasetPath);
        /* Al momento glielo passo come parametro di Input poi faremo le dovute scelte a riguardo,
         *   avevo pensato di creare l'istanza direttamente nel mentodo ma così, sempre e comunque diamo già per scontato
         *   che nella creazione dell'indice utilizzaimo sempre un file Json */

        IndexHandler indexHandler = new IndexHandler(indexPath);
        indexHandler.createIndex(datasetPath, jsonHandler); // Lo so è grezzo ma è temporaneo per non avere errori sul main

        Table.tablesStatistics.printStats();
        Table.tablesStatistics.runPythonScriptHistograms();
    }
}