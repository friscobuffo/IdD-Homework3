package ingegneria_dei_dati;

import ingegneria_dei_dati.index.IndexHandler;
import ingegneria_dei_dati.json.JsonHandler;
import ingegneria_dei_dati.json.JsonHandlerInterface;
import ingegneria_dei_dati.table.Table;
import ingegneria_dei_dati.utils.Triple;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        String datasetPath = "tables.json";
        String indexPath = "index";

        JsonHandlerInterface jsonHandler = new JsonHandler(datasetPath, Table.class);
        /* Al momento glielo passo come parametro di Input poi faremo le dovute scelte a riguardo,
         *   avevo pensato di creare l'istanza direttamente nel mentodo ma così, sempre e comunque diamo già per scontato
         *   che nella creazione dell'indice utilizzaimo sempre un file Json */

        IndexHandler indexHandler = new IndexHandler(indexPath);
        indexHandler.createIndex(datasetPath, (JsonHandler) jsonHandler); // Lo so è grezzo ma è temporaneo per non avere errori sul main

        System.out.println("\rFinished indexing tables    ");
        Table.tablesStatistics.printStats();
        Table.tablesStatistics.runPythonScriptHistograms();
    }
}