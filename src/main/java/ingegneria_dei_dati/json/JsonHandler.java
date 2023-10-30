package ingegneria_dei_dati.json;

import com.google.gson.Gson;
import ingegneria_dei_dati.table.Table;
import ingegneria_dei_dati.utils.Triple;

import java.io.*;
import java.util.List;

public class JsonHandler implements JsonHandlerInterface{
    private List<Triple<String,String, List<String>>> documents;
    private final BufferedReader reader; // il file json contiene un elemento (ovvero una tabella)
    // per ogni riga, quindi possiamo scorrere il file con il BufferedReader e parsare una riga alla volta
    private final Gson gson; // serve a convertire l'elemento del file json (passandolo in input
    // come stringa) in un oggetto java
    private final Class<?> c;

    public JsonHandler(String path, Class<?> c) throws IOException {
        this.reader = new BufferedReader(new FileReader(path));
        this.gson = new Gson();
        this.c = c;
    }

    public boolean hasNextDocument() {
        if (documents != null) {
            return true;
        }
        try {
            String nextLine = reader.readLine();
            if (nextLine != null) {
                DocumentsRepresentable documents = (DocumentsRepresentable) gson.fromJson(nextLine, this.c);
                this.documents = documents.tableDocumentsRepresentation();
                return true;
            }
            this.reader.close();
            return false;
        }
        catch (IOException e) {
            System.out.println("Catturata eccezione IO");
            return false;
        }

    }
    @Override
    public Triple<String,String, List<String>> readNextDocument() {
        Triple<String,String, List<String>> document = this.documents.removeFirst();
        if (this.documents.isEmpty()) {
            this.documents = null;
        }
        return document;
    }
}
