package ingegneria_dei_dati;

import ingegneria_dei_dati.index.IndexHandler;
import ingegneria_dei_dati.json.JsonHandler;
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
        Path path = Paths.get("index/index0");
        Directory directory = FSDirectory.open(path);
        IndexHandler indexHandler = new IndexHandler(directory);

        JsonHandler jsonHandler = new JsonHandler("tables.json", Table.class);
        int i=0;
        while (jsonHandler.hasNextDocument()) {
            i+=1;
            Triple<String, String, List<String>> triple = jsonHandler.readNextDocument();
            // prima stringa della tripla   = identificatore della tabella
            // seconda stringa della tripla = identificatore della colonna
            // terzo valore della tripla    = lista di stringhe della colonna (lista dei valori della colonna)

            //indexHandler.add2Index(triple);

            //System.out.println(triple);
            System.out.print("\rindexed columns: "+i);
            if (i==10000) break;
        }
        System.out.println("\rFinished indexing tables    ");
        Table.tablesStatistics.printStats();
        Table.tablesStatistics.saveStats();
    }
}