package ingegneria_dei_dati;

import ingegneria_dei_dati.index.QueryResults;
import ingegneria_dei_dati.sample.SamplesHandler;
import ingegneria_dei_dati.statistics.TableExpansionStatistics;
import ingegneria_dei_dati.table.Column;
import ingegneria_dei_dati.tableUtilities.TableExpander;

import java.io.IOException;
import java.util.List;

public class MainMergeList {
    public static void main(String[] args) throws IOException {
        String indexPath = "index";
        TableExpander tableExpander = new TableExpander(indexPath);
        SamplesHandler samplesHandler = new SamplesHandler();
        int sampleSize = 300;
        List<Column> sample = samplesHandler.readSample("samples").subList(0, sampleSize);
        System.out.println("sample size: " + sampleSize);
        int i=0;
        // expanding columns and making stats
        for (Column column : sample) {
            System.out.print("\rcolumn number: "+(++i));
            long startTime = System.currentTimeMillis();
            QueryResults queryResults = tableExpander.mergeList(column);
            int queryTime = (int)(System.currentTimeMillis() - startTime);
            TableExpansionStatistics.processExpansionStats(queryResults, queryTime);
        }
        System.out.println();
        TableExpansionStatistics.finishedExpandingColumns();
        TableExpansionStatistics.saveStats("stats/merge-list");
        TableExpansionStatistics.printStats();
    }
}