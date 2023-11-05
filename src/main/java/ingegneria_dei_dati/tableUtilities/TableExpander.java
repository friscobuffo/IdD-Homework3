package ingegneria_dei_dati.tableUtilities;

import ingegneria_dei_dati.index.IndexHandler;
import ingegneria_dei_dati.index.IndexHandlerInterface;
import ingegneria_dei_dati.index.QueryResults;
import ingegneria_dei_dati.table.Column;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class TableExpander {
    private final String FIELD = "text";
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
    private Map<String, Integer> getParsedTermFrequencies(Column column) throws IOException {
        String columnRepresentation = column.getFieldsStringRepresentation();
        try(TokenStream stream  = this.indexHandler.getAnalyzer().tokenStream(this.FIELD, new StringReader(columnRepresentation))) {
            stream.reset();
            Map<String, Integer> termFrequencies = new HashMap<>();
            while (stream.incrementToken()) {
                String token = stream.getAttribute(CharTermAttribute.class).toString();
                int frequency = termFrequencies.getOrDefault(token, 0);
                termFrequencies.put(token, frequency + 1);
            }
            return termFrequencies;
        }
    }
    private BooleanQuery buildQuery(Map<String, Integer> termFrequencies) {
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder().setMinimumNumberShouldMatch(1);
        for(String term : termFrequencies.keySet()){
            Query baseQuery = new TermQuery(new Term(this.FIELD, term));
            if(termFrequencies.get(term) > 1){
                baseQuery = new BoostQuery(baseQuery, termFrequencies.get(term));
            }
            booleanQueryBuilder.add(new BooleanClause(baseQuery, BooleanClause.Occur.SHOULD));
        }
        return booleanQueryBuilder.build();
    }
}