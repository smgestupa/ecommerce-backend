package com.nwahs.backend.models;

public class Table {

    private String table;
    private String description;

    public Table() {}

    public Table(String table, String description ) {
        this.table = table;
        this.description = description;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
