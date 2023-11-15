package ingegneria_dei_dati.tableUtilities;

import ingegneria_dei_dati.sample.SamplesHandler;
import ingegneria_dei_dati.table.Column;

import java.io.IOException;
import java.util.List;

public class MainMergeList {

    public static void main(String[] args) throws IOException {
        String indexPath = "index";
        TableExpander tableExpander = new TableExpander(indexPath);
        SamplesHandler samplesHandler = new SamplesHandler();
        List<Column> samples = samplesHandler.readSample("samples");

        tableExpander.mergeList(samples.get(0));
    }

}
