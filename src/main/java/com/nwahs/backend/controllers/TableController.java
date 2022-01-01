package com.nwahs.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwahs.backend.database.Database;
import com.nwahs.backend.util.Strings;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.json.GsonJsonParser;
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
    public Object getTablesController() throws Exception {

        return getTables();
    }

    // This will return all rows
    // that exist from the provided
    // {table} parameter
    // e.g. GET http://localhost:8093/api/v1/tables/users
    @GetMapping( path = "/tables/{table}",
                 produces = "application/json" )
    public Object getTableRowsController( @PathVariable( "table" ) String tableName ) throws Exception {

        return getTableRows( tableName );
    }

    // This will be used to add
    // rows into a specified table,
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
        final String[] selectedColumns = body.getJSONObject( 0 ).get( "columns" )
                                        .toString().replaceAll( "(\\[)|(])|(\")", "" ).split( "," );

        return addTableRows( tableName, selectedColumns, body.getJSONArray( 1 ) );
    }

    // This is important, since this fixes the
    // CORS-related error(s) when dealing with
    // the PUT/DELETE request; explicitly
    // declaring the CORS origin will fix the
    // problem
    @CrossOrigin( Strings.svelteBackend )
    //
    @PutMapping( path = "/tables/{table}",
                 consumes = "application/json" )
    @ResponseBody
    public void editTableRowController( @PathVariable( "table" ) String tableName,
                                        @RequestBody String requestBody ) throws Exception {
        // Convert the request body into a JSON
        // array and pass it into a new variable,
        // for convenience
        final JSONArray body = new JSONArray( requestBody );
        // We'll be using LinkedHashMap object, since we
        // don't want to mess with the order of the columns.
        // We'll be parsing the strings that we get from the
        // [body] variable, into LinkedHashMap objects
        final LinkedHashMap<?, ?> newRow = new ObjectMapper().readValue( body.getString( 1 ), LinkedHashMap.class);
        final LinkedHashMap<?, ?> oldRow = new ObjectMapper().readValue( body.getString( 0 ), LinkedHashMap.class);

        editTableRow( tableName, newRow, oldRow );
    }

    @CrossOrigin( Strings.svelteBackend )
    // This will be used to delete a
    // specific row from the selected
    // table, by getting selected row
    // data through the response body
    // e.g. DELETE http://localhost:8093/api/v1/tables/users
    @DeleteMapping( path = "/tables/{table}",
                    consumes = "application/json" )
    public Object deleteTableController( @PathVariable( "table" ) String tableName,
                                         @RequestBody( required = false ) String requestBody ) throws Exception {
        // If the request body is empty,
        // or null in that case, then
        // it means that we just want to
        // delete the specified table
        if ( requestBody == null ) return deleteTable( tableName );

        // If request body is not empty,
        // then we are going to give the
        // table name, as well as the Map
        // object by parsing the response
        // body, as parameters
        return deleteTableRow( tableName, new GsonJsonParser().parseMap( requestBody ) );
    }
}
