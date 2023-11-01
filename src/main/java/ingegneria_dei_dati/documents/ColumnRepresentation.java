package ingegneria_dei_dati.documents;

import java.util.List;

public class ColumnRepresentation {
    private String tableName;
    private String columnName;
    private List<String> fields;

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
        if (columnName==null) return "header";
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
