package ingegneria_dei_dati.table;

import java.util.List;

public class Column {
    private String tableName;
    private String columnName;
    private List<String> fields;
    private static int idForNullHeader = 0;

    @Override
    public String toString() {
        String output = "";
        if (this.tableName != null) output += ("table: " + this.tableName + "\n");
        if (this.columnName != null) output += ("column:" + this.columnName + "\n");
        if (this.fields != null) output += ("fields: " + this.fields + "\n");
        return output;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public String getColumnName() {
        if (columnName==null)
            return String.valueOf(Column.idForNullHeader++);
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public List<String> getFields() {
        return fields;
    }
    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
