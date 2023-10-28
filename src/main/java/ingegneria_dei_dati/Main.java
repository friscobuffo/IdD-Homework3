package ingegneria_dei_dati;

import ingegneria_dei_dati.json.JsonHandler;
import ingegneria_dei_dati.table.Table;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {
        JsonHandler handler = new JsonHandler("/home/giordy/IdeaProjects/IdD-Homework3/tables.json", Table.class);
        System.out.println("Hello world!");
        int i=0;
        while (handler.hasNext()) {
            i+=1;
            if (i==5) break;
            Table t = (Table) handler.readNext();
            handler.readNext();
            System.out.println(Arrays.toString(t.headersCleaned));
            System.out.println(t.maxDimensions.row);

        }
    }

}