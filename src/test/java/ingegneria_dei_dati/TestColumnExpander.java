package ingegneria_dei_dati;

import ingegneria_dei_dati.index.IndexHandler;
import ingegneria_dei_dati.index.QueryResults;
import ingegneria_dei_dati.sample.SamplesHandler;
import ingegneria_dei_dati.table.Column;
import ingegneria_dei_dati.tableUtilities.TableExpander;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestColumnExpander {
    private static final String testIndexPath = "test-index";
    private static List<Column> columns;
    private static TableExpander tableExpander;

    @BeforeClass
    public static void InitializeColumns() throws IOException {
        columns = new ArrayList<>();
        // auto
        columns.add(new Column().setTableName("auto").setColumnName("marca").setFields(List.of(new String[]{"audi", "bmw", "mercedes", "fiat", "seat",
                "opel", "renault", "ferrari", "porsche", "lamborghini"})));
        columns.add(new Column().setTableName("fiat").setColumnName("auto").setFields(List.of(new String[]{"fiat", "panda", "punto", "500"})));
        columns.add(new Column().setTableName("auto").setColumnName("audi").setFields(List.of(new String[]{"audi", "audi", "audi", "audi", "audi", "audi"})));
        columns.add(new Column().setTableName("auto costose").setColumnName("marche").setFields(List.of(new String[]{"audi", "bmw", "mercedes",
                "ferrari", "lamborghini"})));
        // bibite
        columns.add(new Column().setTableName("bibite").setColumnName("marche").setFields(List.of(new String[]{"cocacola", "fanta", "fanta", "sprite",
                "7up", "pepsi"})));
        columns.add(new Column().setTableName("cocacola").setColumnName("nome").setFields(List.of(new String[]{"cocacola", "cocacola", "cocacola"})));
        columns.add(new Column().setTableName("bibite cocacola").setColumnName("marche").setFields(List.of(new String[]{"cocacola", "fanta", "sprite"})));
        columns.add(new Column().setTableName("bibite pepsi").setColumnName("marche").setFields(List.of(new String[]{"7up", "pepsi"})));
        IndexHandler indexHandler = new IndexHandler(testIndexPath);
        indexHandler.createIndex(columns);
        tableExpander = new TableExpander(testIndexPath);
    }
    @Test
    public void testAuto() throws IOException {
        QueryResults results = tableExpander.searchForColumnExpansion(columns.get(0));
        QueryResults.Result selfResult = results.getResults().getFirst();
        assertEquals(selfResult.tableName, columns.get(0).getTableName());
        assertEquals(selfResult.columnName, columns.get(0).getColumnName());
        assertEquals(1, selfResult.queryScore, 0.0);

        QueryResults.Result bestResult = results.getBestResult();
        assertEquals(bestResult.tableName, "auto costose");
        assertEquals(bestResult.columnName, "marche");
        assertEquals(0.5, bestResult.queryScore, 0.0);
    }
    @Test
    public void testBibite() throws IOException {
        QueryResults results = tableExpander.searchForColumnExpansion(columns.get(4));
        QueryResults.Result selfResult = results.getResults().getFirst();
        assertEquals(selfResult.tableName, columns.get(4).getTableName());
        assertEquals(selfResult.columnName, columns.get(4).getColumnName());
        assertEquals(1, selfResult.queryScore, 0.0);

        QueryResults.Result bestResult = results.getBestResult();
        assertEquals(bestResult.tableName, "bibite cocacola");
        assertEquals(bestResult.columnName, "marche");
        assertEquals(0.66, bestResult.queryScore, 0.01);
        System.out.println(bestResult.columnName + " " + bestResult.tableName + " ");
    }
    @Test
    public void testSample() throws IOException {
        List<Column> columns = new SamplesHandler().readSample("samples");
        assertEquals(columns.size(), 10000);
        String indexPath = "index";
        TableExpander tableExpander = new TableExpander(indexPath);
        for(Column column : columns){
            QueryResults results = tableExpander.searchForColumnExpansion(column);
            if (results.getResults().isEmpty()) {
                System.out.println(column);
                continue;
            }
            QueryResults.Result selfResult = results.getResults().getFirst();
            assertEquals(1, selfResult.queryScore, 0.0);
        }
    }
}