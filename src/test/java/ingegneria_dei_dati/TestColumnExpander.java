package ingegneria_dei_dati;

import ingegneria_dei_dati.index.IndexHandler;
import ingegneria_dei_dati.sample.SamplesHandler;
import ingegneria_dei_dati.table.Column;
import ingegneria_dei_dati.tableUtilities.ColumnStats;
import ingegneria_dei_dati.tableUtilities.TableExpander;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestColumnExpander {
    private final String testIndexPath = "test-index";
    private List<Column> columns;
    @Before
    public void InitializeColumns() throws IOException {
        columns = new ArrayList<>();
        columns.add(new Column().setTableName("auto").setColumnName("marca").setFields(List.of(new String[]{"audi", "bmw", "mercedes", "fiat", "seat",
                "opel", "renault", "ferrari", "porsche", "lamborghini"})));
        columns.add(new Column().setTableName("fiat").setColumnName("auto").setFields(List.of(new String[]{"fiat", "panda", "punto", "500"})));
        columns.add(new Column().setTableName("auto").setColumnName("audi").setFields(List.of(new String[]{"audi", "audi", "audi", "audi", "audi", "audi"})));
        columns.add(new Column().setTableName("auto costose").setColumnName("marche").setFields(List.of(new String[]{"audi", "bmw", "mercedes",
                "ferrari", "lamborghini"})));
        columns.add(new Column().setTableName("bibite").setColumnName("marche").setFields(List.of(new String[]{"cocacola", "fanta", "sprite", "7up", "pepsi"})));
        columns.add(new Column().setTableName("cocacola").setColumnName("nome").setFields(List.of(new String[]{"cocacola", "cocacola", "cocacola"})));
        columns.add(new Column().setTableName("bibite cocacola").setColumnName("marche").setFields(List.of(new String[]{"cocacola", "fanta", "sprite"})));
        columns.add(new Column().setTableName("bibite pepsi").setColumnName("marche").setFields(List.of(new String[]{"7up", "pepsi"})));
        IndexHandler indexHandler = new IndexHandler(this.testIndexPath);
        indexHandler.createIndex(columns);
    }
    @Test
    public void testMyColumns() throws IOException {
        TableExpander tableExpander = new TableExpander(testIndexPath);
        ColumnStats column = tableExpander.searchForColumnExpansion(columns.get(0).fieldsStringRepresentation()).getColumnsStats().get(1);
        assertEquals(column.getTableId(), "auto costose");
        assertEquals(column.getColumnId(), "marche");
        assertEquals(0.5, column.getColumnScore(), 0.0);
    }
    @Test
    public void testSample() throws IOException {
        List<Column> columns = new SamplesHandler().readSample();
    }
}