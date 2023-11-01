package ingegneria_dei_dati.documents;

import com.google.gson.Gson;
import ingegneria_dei_dati.table.Table;

import java.io.*;
import java.util.List;

public class JsonHandler implements TablesHandler {
    private List<ColumnRepresentation> columns;
    private final BufferedReader reader; // il file json contiene un elemento (ovvero una tabella)
    // per ogni riga, quindi possiamo scorrere il file con il BufferedReader e parsare una riga alla volta
    private final Gson gson; // serve a convertire l'elemento del file json (passandolo in input
    // come stringa) in un oggetto java

    public JsonHandler(String path) throws IOException {
        this.reader = new BufferedReader(new FileReader(path));
        this.gson = new Gson();
    }
    public boolean hasNextColumn() {
        if (this.columns != null)
            if (!this.columns.isEmpty())
                return true;
        try {
            String nextLine = reader.readLine();
            if (nextLine != null) {
                Table table = gson.fromJson(nextLine, Table.class);
                this.columns = table.getColumns();
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
    public ColumnRepresentation readNextColumn() {
        return this.columns.removeFirst();
    }
}
