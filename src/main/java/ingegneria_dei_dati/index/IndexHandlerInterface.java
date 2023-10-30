package ingegneria_dei_dati.index;

import org.apache.lucene.store.Directory;
import ingegneria_dei_dati.utils.Triple;

import java.io.IOException;

public interface IndexHandlerInterface {

    public void add2Index(Triple triple) throws IOException;

}
