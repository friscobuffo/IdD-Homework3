package ingegneria_dei_dati.json;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import ingegneria_dei_dati.table.Table;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonHandler {
    public void readLargeJson(String path) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Path.of(path));
             JsonReader reader = new JsonReader(new InputStreamReader(inputStream)) )
        {
            reader.beginArray();
            while (reader.hasNext()) {
                Table person = new Gson().fromJson(reader, Table.class);
                System.out.println(person.name);
                //System.out.println(Person);
            }
            reader.endArray();
        }
    }
}
