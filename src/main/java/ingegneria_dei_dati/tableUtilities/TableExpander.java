package ingegneria_dei_dati.tableUtilities;

import ingegneria_dei_dati.index.IndexHandler;
import ingegneria_dei_dati.index.IndexHandlerInterface;
import ingegneria_dei_dati.sample.SamplesHandler;
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
    public ExpansionStats searchForColumnExpansion(String query, int minimumMatch) throws IOException {
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder().setMinimumNumberShouldMatch(minimumMatch);
        Map<String, Integer> termFrequencies = this.tokenizeString(query);

        for(String term : termFrequencies.keySet()){
            Query baseQuery = new TermQuery(new Term(this.FIELD, term));
            if(termFrequencies.get(term) > 1){
                baseQuery = new BoostQuery(baseQuery, termFrequencies.get(term));
            }
            booleanQueryBuilder.add(new BooleanClause(baseQuery, BooleanClause.Occur.SHOULD));
        }
        BooleanQuery booleanQuery = booleanQueryBuilder.build();

        ExpansionStats expansionStats = this.indexHandler.search(booleanQuery, 10);
        int termCount = termFrequencies.values().stream().reduce(0, Integer::sum);
        expansionStats.normalize(termCount);
        return expansionStats;
    }

    private Map<String, Integer> tokenizeString(String query) throws IOException {
        try(TokenStream stream  = this.indexHandler.getAnalyzer().tokenStream(this.FIELD, new StringReader(query))) {
            stream.reset();
            Map<String, Integer> termFrequencies = new HashMap<String, Integer>();
            while (stream.incrementToken()) {
                String token = stream.getAttribute(CharTermAttribute.class).toString();
                int frequency = termFrequencies.getOrDefault(token, 0);
                termFrequencies.put(token, frequency + 1);
            }
            return termFrequencies;
        }
    }

    public static void main(String[] args) throws IOException {
        String indexPath = "index";
        TableExpander tableExpander = new TableExpander(indexPath);
        SamplesHandler samplesHandler = new SamplesHandler();
        List<Column> samples = samplesHandler.readSample();
        for(Column sample : samples.subList(0,2)){
            System.out.println(tableExpander.searchForColumnExpansion(sample.fieldsStringRepresentation(),1).toString());
        }
    }

}
