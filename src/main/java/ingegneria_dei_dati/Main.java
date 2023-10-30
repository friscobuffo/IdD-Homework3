package ingegneria_dei_dati;

import ingegneria_dei_dati.json.JsonHandler;
import ingegneria_dei_dati.table.Table;
import ingegneria_dei_dati.utils.Triple;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        JsonHandler jsonHandler = new JsonHandler("tables.json", Table.class);
        int i=0;
        while (jsonHandler.hasNextDocument()) {
            i+=1;
            if (i==5) break;
            Triple<String, String, List<String>> triple = jsonHandler.readNextDocument();
            //prima stringa della tripla   = identificatore della tabella
            //seconda stringa della tripla = identificatore della colonna
            //terzo valore della tripla    = lista di stringhe della colonna (lista dei valori
            //                               della colonna)
        }
    }
}