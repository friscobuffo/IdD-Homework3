package ingegneria_dei_dati.json;

import com.google.gson.Gson;

import java.io.*;

public class JsonHandler {
    private final Class<?> c;
    private String nextLine;
    private final BufferedReader reader;
    private final Gson gson;
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
