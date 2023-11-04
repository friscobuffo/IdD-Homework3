package ingegneria_dei_dati.table;

import java.util.List;

public class Column {
    private String tableName;
    private String columnName;
    private List<String> fields;
    private static int idForNullHeader = 0; // useful to give a name to columns that haven't a header

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        if (this.tableName != null) output.append("table: ").append(this.tableName).append("\n");
        if (this.columnName != null) output.append("column: ").append(this.columnName).append("\n");
        if (this.fields != null) output.append("fields: ").append(this.fields).append("\n");
        return output.toString();
    }
    public String fieldsStringRepresentation() {
        StringBuilder fieldsRepresentation = new StringBuilder();
        for (String field: this.fields) {
            fieldsRepresentation.append(field).append(" ");
        }
        return fieldsRepresentation.toString();
    }
    public String getTableName() {
        return tableName;
    }
    public Column setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }
    public String getColumnName() {
        if (columnName==null)
            return String.valueOf(Column.idForNullHeader++);
        return columnName;
    }
    public Column setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }
    public List<String> getFields() {
        return fields;
    }
    public Column setFields(List<String> fields) {
        this.fields = fields;
        return this;
    }
}
