package ingegneria_dei_dati.index;

import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

public class IntersectionSimilarity extends SimilarityBase {
    @Override
    protected double score(BasicStats basicStats, double freq, double docLen) {
        int score = (freq > 0) ? 1 : 0;
        return score * basicStats.getBoost();
    }
    @Override
    public String toString() {
        return null;
    }
}
