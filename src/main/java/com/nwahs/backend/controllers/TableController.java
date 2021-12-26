package com.nwahs.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwahs.backend.database.Database;
import com.nwahs.backend.util.Strings;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping( Strings.apiPath )
public class TableController extends Database {

    // This will return all available
    // tables in your database by
    // accessing through "/tables/"
    // e.g. GET http://localhost:8093/api/v1/tables/
    @GetMapping( path = "/tables",
                 produces = "application/json" )
    public Object getTablesController() {

        return getTables();
    }

    // This will return all rows
    // that exist from the provided
    // {table} parameter
    // e.g. GET http://localhost:8093/api/v1/tables/users
    @GetMapping( path = "/tables/{table}",
                 produces = "application/json" )
    public Object getTableRowsController( @PathVariable( "table" ) String tableName ) {

        return getTableRows( tableName );
    }

    // This will be used to add
    // rows into a specified table
    // by reading the body from the
    // POST request
    // e.g. POST http://localhost:8093/api/v1/tables/users
    @PostMapping( path = "/tables/{table}",
                  consumes = "application/json" )
    @ResponseBody
    public Object addTableRowsController( @PathVariable( "table" ) String tableName,
                                          @RequestBody String requestBody ) throws Exception {
        final JSONArray body = new JSONArray( requestBody );
        // Get the first value from the JSONArray [body]
        // and get the provided columns through [.get()]
        // method, convert to String, remove the brackets
        // and double-quotes through regex, and lastly,
        // convert them to a String array using regex
        // again
        final String[] selectedColumns = body.getJSONObject( 0 ).get( "columns" )
                                        .toString().replaceAll( "(\\[)|(])|(\")", "" ).split( "," );

        return addTableRows( tableName, selectedColumns, body.getJSONArray( 1 ) );
    }

    @PutMapping( path = "/tables/{table}",
                 consumes = "application/json" )
    @ResponseBody
    public void editTableRowController( @PathVariable( "table" ) String tableName,
                                        @RequestBody String requestBody ) throws Exception {
        final JSONArray body = new JSONArray( requestBody );
        final LinkedHashMap<?, ?> newRow = new ObjectMapper().readValue( body.getString( 1 ), LinkedHashMap.class);
        final LinkedHashMap<?, ?> oldRow = new ObjectMapper().readValue( body.getString( 0 ), LinkedHashMap.class);

        editTableRow( tableName, newRow, oldRow );
    }


    // This will delete the table
    // that is the same as the
    // provided {table} parameter
    // e.g. DELETE http://localhost:8093/api/v1/tables/users
    @DeleteMapping( path = "/tables/{table}" )
    public Object deleteTableController( @PathVariable( "table" ) String tableName ) {

        return deleteTable( tableName );
    }
}
