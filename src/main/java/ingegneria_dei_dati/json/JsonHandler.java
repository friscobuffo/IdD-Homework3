package ingegneria_dei_dati.json;

import com.google.gson.Gson;

import java.io.*;

public class JsonHandler {
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

    public boolean hasNext() throws IOException {
        this.nextLine = reader.readLine();
        if (this.nextLine != null) return true;
        this.reader.close();
        return false;
    }
    public Object readNext() {
        return gson.fromJson(this.nextLine, this.c);
    }
}
