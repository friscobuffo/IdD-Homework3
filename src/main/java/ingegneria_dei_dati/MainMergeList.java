package ingegneria_dei_dati;

import ingegneria_dei_dati.index.QueryResults;
import ingegneria_dei_dati.sample.SamplesHandler;
import ingegneria_dei_dati.statistics.TableExpansionStatistics;
import ingegneria_dei_dati.table.Column;
import ingegneria_dei_dati.tableUtilities.TableExpander;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainMergeList {

    public static void main(String[] args) throws IOException {
        String indexPath = "index";
        TableExpander tableExpander = new TableExpander(indexPath);
        SamplesHandler samplesHandler = new SamplesHandler();
        List<Column> samples = samplesHandler.readSample("samples");
        int i=0;
        // expanding columns and making stats
        for (List<Column> sample : samplesHandler.divideSample(samples, 100)) {
            System.out.println("expanding sample number: "+(++i)+" - size of sample: " + sample.size());
            int j=0;
            for (Column column : sample) {
                System.out.print("\rcolumn number: "+(++j));
                QueryResults queryResults = tableExpander.mergeList(column);
                TableExpansionStatistics.processExpansionStats(queryResults);
            }
            System.out.println("\nexpanded sample number: "+i);
            break;
        }
        System.out.println();
        // calculating average similarity between 2 random columns
        long totalComparisons = 0;
        double totalSimilarity = 0.0;
        i=0;
        for (List<Column> sample : samplesHandler.divideSample(samples, 100)) {
            System.out.println("calculating average similarity in sample: " + (++i));
            int j=0;
            for (Column column : sample) {
                System.out.print("\rcolumn number: "+(++j));
                Map<String, Integer> outerSampleTermFrequencies = tableExpander.getParsedTermFrequencies(column);
                int outerSampleTermCount = outerSampleTermFrequencies.values().stream().reduce(0, Integer::sum);
                if (outerSampleTermCount==0) continue;
                for (Column innerSample : sample) {
                    Map<String, Integer> innerSampleTermFrequencies = tableExpander.getParsedTermFrequencies(innerSample);
                    Set<String> innerSampleTermFrequenciesKeySet = innerSampleTermFrequencies.keySet();
                    if (innerSampleTermFrequenciesKeySet.isEmpty()) continue;
                    totalComparisons += 1;
                    double totalIntersections = 0.0;
                    for (String value : innerSampleTermFrequenciesKeySet)
                        totalIntersections += outerSampleTermFrequencies.getOrDefault(value, 0);
                    totalSimilarity += (totalIntersections / outerSampleTermCount);
                }
            }
            System.out.println();
            break;
        }
        TableExpansionStatistics.finishedExpandingColumns();
        TableExpansionStatistics.setAverageSimilarityRandomColumns(totalSimilarity / totalComparisons);
        TableExpansionStatistics.saveStats("stats/merge-list");
        TableExpansionStatistics.printStats();
    }

}
