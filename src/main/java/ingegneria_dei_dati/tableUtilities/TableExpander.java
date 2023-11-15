package ingegneria_dei_dati.tableUtilities;

import ingegneria_dei_dati.index.IndexHandler;
import ingegneria_dei_dati.index.IndexHandlerInterface;
import ingegneria_dei_dati.index.QueryResults;
import ingegneria_dei_dati.table.Column;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

    public Map<String, Float> mergeList(Column column) throws IOException {
        Column parsedColumn = this.indexHandler.parseColumn(column);
        Map<String, Float> set2count = new HashMap<>();
        for(String term : parsedColumn.getFields()){
            Query baseQuery = new TermQuery(new Term(FIELD, term));
            QueryResults queryResults = this.indexHandler.search(baseQuery, (int)Double.POSITIVE_INFINITY);
            for(QueryResults.Result result : queryResults.getResults()){
                String tableColumnId = result.getTableName() + "@-----@" + result.getColumnName();
                float count = set2count.getOrDefault(tableColumnId, 0f);
                if(count != 0) System.out.println("match");
                set2count.put(tableColumnId, count + 1f);
            }

        }

        int termCount = column.getFields().size();
        set2count.replaceAll((key, value) -> value/termCount);

        System.out.println(termCount + " " + Collections.max(set2count.values()));

        return set2count;
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