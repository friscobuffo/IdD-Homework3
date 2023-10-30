package ingegneria_dei_dati.json;

import com.google.gson.Gson;
import ingegneria_dei_dati.utils.Triple;

import java.io.*;
import java.util.List;

public class JsonHandler implements JsonHandlerInterface{
    private final Class<?> c; // gli elementi json vengono convertiti in istanze di questa classe
    private String nextLine;
    private final BufferedReader reader; // il file json contiene un elemento (ovvero una tabella)
    // per ogni riga, quindi possiamo scorrere il file con il BufferedReader e parsare una riga alla volta
    private final Gson gson; // serve a convertire l'elemento del file json (passandolo in input
    // come stringa) in un oggetto java

    public JsonHandler(String path, Class<?> c) throws IOException {
        this.c = c;
        this.reader = new BufferedReader(new FileReader(path));
        this.gson = new Gson();
    }

    public boolean hasNextDocument() {
        try {
            this.nextLine = reader.readLine();
            if (this.nextLine != null) return true;
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
        Object prova = gson.fromJson(this.nextLine, this.c);
        return null;
    }
}
