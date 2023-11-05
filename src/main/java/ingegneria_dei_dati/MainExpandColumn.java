package ingegneria_dei_dati;

import ingegneria_dei_dati.index.QueryResults;
import ingegneria_dei_dati.sample.SamplesHandler;
import ingegneria_dei_dati.statistics.Statistics;
import ingegneria_dei_dati.table.Column;
import ingegneria_dei_dati.tableUtilities.TableExpander;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainExpandColumn {
    public static void main(String[] args) throws IOException {
        String indexPath = "index";
        TableExpander tableExpander = new TableExpander(indexPath);
        SamplesHandler samplesHandler = new SamplesHandler();
        List<Column> samples = samplesHandler.readSample("samples");
        float totalGoodScores = 0;
        List<Float> goodScores = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for(Column sample : samples){
            QueryResults queryResults = tableExpander.searchForColumnExpansion(sample);
            if (!queryResults.getResults().isEmpty()) {
                QueryResults.Result bestResult = queryResults.getBestResult();
                if (bestResult != null) {
                    goodScores.add(bestResult.queryScore);
                    totalGoodScores += bestResult.queryScore;
                }
            }
        }
        long totalTime = System.currentTimeMillis() - startTime;
        long averageQueryTime = totalTime / goodScores.size();
        Statistics.addCustomStat("sample size", samples.size());
        Statistics.addCustomStat("total time", totalTime/1000.0);
        Statistics.addCustomStat("average query time", averageQueryTime/1000.0);
        Statistics.addCustomStat("good results counter", goodScores.size());
        Statistics.addCustomStat("total good scores", totalGoodScores);
        double averageGoodScore = totalGoodScores / goodScores.size();
        Statistics.addCustomStat("average good score", averageGoodScore);
        double variance = 0;
        for (float score : goodScores)
            variance += ((averageGoodScore - score) * (averageGoodScore - score));
        variance /= (goodScores.size()-1);
        double standardDeviation = Math.sqrt(variance);
        Statistics.addCustomStat("variance good score", variance);
        Statistics.addCustomStat("standard deviation", standardDeviation);
        Statistics.saveCustomStats("stats", "querySampleStats");
        Statistics.printCustomStats();
    }
}