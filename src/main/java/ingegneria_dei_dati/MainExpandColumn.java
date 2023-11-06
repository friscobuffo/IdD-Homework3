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

public class MainExpandColumn {
    public static void main(String[] args) throws IOException {
        String indexPath = "index";
        TableExpander tableExpander = new TableExpander(indexPath);
        SamplesHandler samplesHandler = new SamplesHandler();
        List<Column> samples = samplesHandler.readSample("samples");
        for (Column sample : samples) {
            QueryResults queryResults = tableExpander.searchForColumnExpansion(sample);
            TableExpansionStatistics.processExpansionStats(queryResults);
        }
        TableExpansionStatistics.finishedExpandingColumns();
        // calculating average similarity between 2 random columns
        long totalComparisons = 0;
        double totalSimilarity = 0.0;
        for (Column outerSample : samples) {
            Map<String, Integer> outerSampleTermFrequencies = tableExpander.getParsedTermFrequencies(outerSample);
            int outerSampleTermCount = outerSampleTermFrequencies.values().stream().reduce(0, Integer::sum);
            if (outerSampleTermCount==0) continue;
            for (Column innerSample : samples) {
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
        TableExpansionStatistics.setAverageSimilarityRandomColumns(totalSimilarity / totalComparisons);
        TableExpansionStatistics.saveStats("stats");
        TableExpansionStatistics.printStats();
    }
}