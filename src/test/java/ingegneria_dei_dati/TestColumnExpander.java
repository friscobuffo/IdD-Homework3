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
        columns.add(new Column().setTableName("bibite").setColumnName("marche").setFields(List.of(new String[]{"cocacola", "fanta", "fanta", "sprite",
                "7up", "pepsi"})));
        columns.add(new Column().setTableName("cocacola").setColumnName("nome").setFields(List.of(new String[]{"cocacola", "cocacola", "cocacola"})));
        columns.add(new Column().setTableName("bibite cocacola").setColumnName("marche").setFields(List.of(new String[]{"cocacola", "fanta", "sprite"})));
        columns.add(new Column().setTableName("bibite pepsi").setColumnName("marche").setFields(List.of(new String[]{"7up", "pepsi"})));
        IndexHandler indexHandler = new IndexHandler(this.testIndexPath);
        indexHandler.createIndex(columns);
    }
    @Test
    public void testMyColumns() throws IOException {
        TableExpander tableExpander = new TableExpander(testIndexPath);
        // auto
        List<ColumnStats> columnStats = tableExpander.searchForColumnExpansion(columns.get(0).fieldsStringRepresentation(),1).getColumnsStats();
        ColumnStats column0 = columnStats.get(0);
        assertEquals(column0.getTableId(), columns.get(0).getTableName());
        assertEquals(column0.getColumnId(), columns.get(0).getColumnName());
        assertEquals(1, column0.getColumnScore(), 0.0);
        ColumnStats column1 = columnStats.get(1);
        assertEquals(column1.getTableId(), "auto costose");
        assertEquals(column1.getColumnId(), "marche");
        assertEquals(0.5, column1.getColumnScore(), 0.0);
        // bibite
        System.out.println(tableExpander.searchForColumnExpansion(columns.get(4).fieldsStringRepresentation(),1));
        columnStats = tableExpander.searchForColumnExpansion(columns.get(4).fieldsStringRepresentation(),1).getColumnsStats();
        column0 = columnStats.get(0);
        assertEquals(column0.getTableId(), columns.get(4).getTableName());
        assertEquals(column0.getColumnId(), columns.get(4).getColumnName());
        assertEquals(1, column0.getColumnScore(), 0.0);
        column1 = columnStats.get(1);
        assertEquals(column1.getTableId(), "bibite cocacola");
        assertEquals(column1.getColumnId(), "marche");
        assertEquals(0.66, column1.getColumnScore(), 0.01);
    }
    @Test
    public void testSample() throws IOException {
        List<Column> columns = new SamplesHandler().readSample();
        assertEquals(columns.size(), 1000);
        String indexPath = "index";
        TableExpander tableExpander = new TableExpander(indexPath);
        for(Column column : columns.subList(0,2)){
            System.out.println(tableExpander.searchForColumnExpansion(column.fieldsStringRepresentation(),1).toString());
        }
    }
}