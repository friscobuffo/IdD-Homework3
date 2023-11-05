package ingegneria_dei_dati;

import ingegneria_dei_dati.sample.SamplesHandler;
import ingegneria_dei_dati.table.Column;
import ingegneria_dei_dati.tableUtilities.TableExpander;

import java.io.IOException;
import java.util.List;

public class MainExpandColumn {
    public static void main(String[] args) throws IOException {
        String indexPath = "index";
        TableExpander tableExpander = new TableExpander(indexPath);
        SamplesHandler samplesHandler = new SamplesHandler();
        List<Column> samples = samplesHandler.readSample();
        for(Column sample : samples.subList(0,2)){
            System.out.println(tableExpander.searchForColumnExpansion(sample).toString());
        }
    }
}