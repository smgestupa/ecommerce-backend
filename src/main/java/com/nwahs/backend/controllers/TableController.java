package com.nwahs.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwahs.backend.database.Database;
import com.nwahs.backend.util.Strings;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping( Strings.apiPath )
public class TableController extends Database {

    /**
     * This will return all available
     * tables in your database
     * e.g. GET http://localhost:8093/api/v1/tables/
     *
     * @return A ResponseEntity with JSON body, or an error code 400 if something fails
     */
    @GetMapping( path = "/tables",
                 produces = "application/json" )
    public Object getTablesController() {
        return getTables();
    }

    /**
     * This will return only 11 rows
     * that exist from the specific
     * table
     * e.g. GET http://localhost:8093/api/v1/tables/users
     *
     * @param tableName The table name
     * @return A ResponseEntity with JSON body, or an error code 400 if something fails
     */
    @GetMapping( path = "/tables/{table}",
            produces = "application/json" )
    public Object getTableRowsController( @PathVariable( "table" ) String tableName ) {
        return getTableRows( tableName, 0 );
    }

    /**
     * This will return only 11 rows,
     * starting from an offset, from
     * the specific table
     * e.g. GET http://localhost:8093/api/v1/tables/users/2
     *
     * @param tableName The table name
     * @param page Table rows' starting offset
     * @return A ResponseEntity with JSON body, or an error code 400 if something fails
     */
    @GetMapping( path = "/tables/{table}/{page}",
                 produces = "application/json" )
    public Object getTableRowsController( @PathVariable( "table" ) String tableName,
                                          @PathVariable( "page" ) int page ) {
        return getTableRows( tableName, page );
    }

    /**
     * This will return specific rows
     * based on a keyword
     * e.g. GET http://localhost:8093/api/v1/tables/users/?column=[tableColumn]&search=[keyword]
     *
     * @param tableName The table name
     * @param tableColumn Specific table column name
     * @param keyword A string used as the basis for finding a specific result, using regexp
     * @return A ResponseEntity with JSON body, or an error code 400 if something fails
     */
    @GetMapping( path = "/tables/{table}/",
            produces = "application/json" )
    public Object searchTableRowController( @PathVariable( "table" ) String tableName,
                                            @RequestParam( "column" ) String tableColumn,
                                            @RequestParam( "search" ) String keyword ) {
        return searchTableRow( tableName, tableColumn, keyword );
    }

    /**
     * This will be used to add
     * rows into a specific table,
     * using the request body
     * e.g. POST http://localhost:8093/api/v1/tables/users
     *
     * @param tableName The table name
     * @param requestBody The response body received from communication to this endpoint
     * @return A ResponseEntity with a code 200, or an error code 400 if something fails
     * @throws JSONException Used to catch JSON-related errors
     */
    @PostMapping( path = "/tables/{table}",
                  consumes = "application/json" )
    @ResponseBody
    public Object addTableRowsController( @PathVariable( "table" ) String tableName,
                                          @RequestBody String requestBody ) throws JSONException {
        final JSONArray body = new JSONArray( requestBody );

        // Get the first value from the JSONArray,
        // get the provided columns, convert to String
        final String[] selectedColumns = body.getJSONObject( 0 ).get( "columns" )
                                        // Convert to String, remove the brackets
                                        // and double-quotes using regexp, and lastly,
                                        // convert to a String array
                                        .toString().replaceAll( "(\\[)|(])|(\")", "" ).split( "," );

        return addTableRows( tableName, selectedColumns, body.getJSONArray( 1 ) );
    }


    /**
     * This will be used to edit
     * a row of a specific table,
     * using the request body
     * e.g. PUT http://localhost:8093/api/v1/tables/users
     *
     * @param tableName The table name
     * @param requestBody The response body received from communication to this endpoint
     * @throws JSONException Used to catch JSON-related errors
     * @return A ResponseEntity with a code 200, or an error code 400 if something fails
     * @throws JsonProcessingException Used to catch JSON-related errors for the jackson library
     */
    @CrossOrigin( Strings.svelteBackend ) // Explicitly required for PUT requests
    @PutMapping( path = "/tables/{table}",
                 consumes = "application/json" )
    @ResponseBody
    public Object editTableRowController( @PathVariable( "table" ) String tableName,
                                        @RequestBody String requestBody ) throws JSONException, JsonProcessingException {
        final JSONArray body = new JSONArray( requestBody );

        // Using LinkedHashMap prevents using of default
        // sorting, and we'll be using it to parse strings
        // as key-value pairs
        final LinkedHashMap<?, ?> newRow = new ObjectMapper().readValue( body.getString( 1 ), LinkedHashMap.class);
        final LinkedHashMap<?, ?> oldRow = new ObjectMapper().readValue( body.getString( 0 ), LinkedHashMap.class);

        return editTableRow( tableName, newRow, oldRow );
    }

    /**
     * This will be used to delete a
     * specific row of a specific table,
     * or delete the table itself
     * e.g. DELETE http://localhost:8093/api/v1/tables/users
     *
     * @param tableName The table name
     * @param requestBody The response body received from communication to this endpoint
     * @return A ResponseEntity with a code 200, or an error code 400 if something fails
     */
    @CrossOrigin( Strings.svelteBackend ) // Explicitly required for DELETE requests
    @DeleteMapping( path = "/tables/{table}",
                    consumes = "application/json" )
    public Object deleteTableController( @PathVariable( "table" ) String tableName,
                                         @RequestBody( required = false ) String requestBody ) {
        // If the request body is empty,
        // that means we want to delete
        // the table
        if ( requestBody == null ) return deleteTable( tableName );

        // If the request body isn't empty,
        // parse the request body as JSON,
        // then pass the necessary values
        return deleteTableRow( tableName, new GsonJsonParser().parseMap( requestBody ) );
    }
}
