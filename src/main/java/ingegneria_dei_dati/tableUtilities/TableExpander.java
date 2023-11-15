package ingegneria_dei_dati.tableUtilities;

import ingegneria_dei_dati.index.IndexHandler;
import ingegneria_dei_dati.index.IndexHandlerInterface;
import ingegneria_dei_dati.index.QueryResults;
import ingegneria_dei_dati.table.Column;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.util.*;

public class TableExpander {
    private static final String FIELD = "text";
    private final IndexHandlerInterface indexHandler;

    public TableExpander(String indexPath) throws IOException {
        this.indexHandler = new IndexHandler(indexPath);
    }
    public QueryResults searchForColumnExpansion(Column column) throws IOException {
        Map<String, Integer> termFrequencies = getParsedTermFrequencies(column);
        BooleanQuery booleanQuery = buildQuery(termFrequencies);
        QueryResults queryResults = this.indexHandler.search(booleanQuery, 10);
        int termCount = termFrequencies.values().stream().reduce(0, Integer::sum);
        queryResults.normalize(termCount);
        queryResults.setQueryColumn(column);
        return queryResults;
    }
    public QueryResults mergeList(Column column) throws IOException {
        String separator = "@--------@";
        Column parsedColumn = this.indexHandler.parseColumn(column);
        Map<String, Integer> set2count = new HashMap<>();
        for(String term : parsedColumn.getFields()){
            Query baseQuery = new TermQuery(new Term(FIELD, term));
            QueryResults queryResults = this.indexHandler.search(baseQuery, (int)Double.POSITIVE_INFINITY);
            Set<String> allTableColumnIds = new HashSet<>();
            for(QueryResults.Result result : queryResults.getResults()) {
                String tableColumnId = result.getTableName() + separator + result.getColumnName();
                allTableColumnIds.add(tableColumnId);
            }
            for (String tableColumnId : allTableColumnIds) {
                int count = set2count.getOrDefault(tableColumnId, 0);
                set2count.put(tableColumnId, count + 1);
            }
        }
        int termCount = parsedColumn.getFields().size();
        float bestScore = 0;
        String bestTableColumnId = "";
        float secondBestScore = 0;
        String secondBestTableColumnId = "";
        for (String key : set2count.keySet()) {
            float value = set2count.get(key) / (float)termCount;
            if (value > bestScore) {
                secondBestScore = bestScore;
                secondBestTableColumnId = bestTableColumnId;
                bestScore = value;
                bestTableColumnId = key;
            } else if (value > secondBestScore) {
                secondBestScore = value;
                secondBestTableColumnId = key;
            }
        }
        QueryResults queryResults = new QueryResults(set2count.size());
        if (secondBestScore > 0) {
            String[] separated = secondBestTableColumnId.split(separator);
            queryResults.addResult(separated[0], separated[1], secondBestScore);
        }
        if (bestScore > 0) {
            String[] separated = bestTableColumnId.split(separator);
            queryResults.addResult(separated[0], separated[1], bestScore);
        }
        queryResults.setQueryColumn(column);
        return queryResults;
    }
    public Map<String, Integer> getParsedTermFrequencies(Column column) throws IOException {
        Column parsedColumn = this.indexHandler.parseColumn(column);
        Map<String, Integer> termFrequencies = new HashMap<>();
        for(String cell : parsedColumn.getFields()){
            int frequency = termFrequencies.getOrDefault(cell, 0);
            termFrequencies.put(cell, frequency + 1);
        }
        return termFrequencies;
    }
    private BooleanQuery buildQuery(Map<String, Integer> termFrequencies) {
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder().setMinimumNumberShouldMatch(1);
        for(String term : termFrequencies.keySet()){
            Query baseQuery = new TermQuery(new Term(FIELD, term));
            if(termFrequencies.get(term) > 1){
                baseQuery = new BoostQuery(baseQuery, termFrequencies.get(term));
            }
            booleanQueryBuilder.add(new BooleanClause(baseQuery, BooleanClause.Occur.SHOULD));
        }
        return booleanQueryBuilder.build();
    }
}