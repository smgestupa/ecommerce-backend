package com.nwahs.backend.models;

public class Table {

    private final String name;
    private final String description;

    public Table( String name, String description ) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
