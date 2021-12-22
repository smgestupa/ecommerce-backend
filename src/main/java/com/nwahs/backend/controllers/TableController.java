package com.nwahs.backend.controllers;

import com.nwahs.backend.models.Table;
import com.nwahs.backend.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping( Strings.apiPath )
public class TableController {

    // Will be used to connect to
    // database, and you can
    // execute statements with this
    @Autowired
    private DataSource dataSource;

    // This will return all available
    // tables in your database by
    // accessing through "/tables/"
    // e.g. http://localhost:8093/api/v1/tables/
    @GetMapping( path = "/tables",
                 produces = "application/json" )
    public Object getTables() {
        // Try-with-resources is used in order for
        // connections to be disposed and avoid
        // connection leaks; Java will handle
        // closing the connection
        try ( final Connection conn = dataSource.getConnection() ){
            // This will open a connection and
            // get the metadata from the database
            // in order to get the tables only
            final DatabaseMetaData metaData = conn.getMetaData();
            final ResultSet tables = metaData.getTables( null, null, null, new String[] { "TABLE" } );

            final List< Table > tablesList = new ArrayList<>();
            while ( tables.next() ) {
                // Get the table name and the comment,
                // as the description, from the ResultSet
                // metadata
                final String tableName = tables.getString( "TABLE_NAME" );
                final String tableDescription = tables.getString( "TABLE_COMMENT" );

                // Add the strings inside the [tablesList]
                // list, which will be used as the JSON
                // response
                tablesList.add( new Table( tableName, tableDescription ) );
            }

            // Returns the HashMap as a JSON
            // and with the proper HTTP status
            // as the responses
            return new ResponseEntity<>( tablesList, HttpStatus.OK );
        } catch ( Exception err ) {
            System.err.println( err.getMessage() );
        }

        // Returns an error code 400 for
        // the HTTP Status as the response,
        // in case the catch-block was
        // executed instead
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
    }

    // This will return the rows
    // that exist from the provided
    // {table} parameter
    // e.g. http://localhost:8093/api/v1/tables/users
    // will try to get the rows in the [users] table
    @GetMapping( path = "/tables/{table}",
                 produces = "application/json" )
    @ResponseBody
    public Object tableRows( @PathVariable( "table" ) String tableName,
                             @RequestParam( required = false ) boolean delete ) {
        // If '?delete=true' is present in the url,
        // e.g. .../api/v1/table/{tableName}?delete=true
        // the table in {tableName} will be deleted
        // through this method
        if ( delete ) return deleteTable( tableName );

        // If no other params are present in the url,
        // then just fallback to returning all rows
        // in the provided table name
        return getTableRows( tableName );
    }

    public ResponseEntity<?> getTableRows( String tableName ) {
        try ( final Connection conn = dataSource.getConnection() ) {
            // Prepare a query statement to
            // be executed later in your database
            // which will return the rows of the
            // specified table, if it exists
            final PreparedStatement statement = conn.prepareStatement( "SELECT * FROM " + tableName );
            final ResultSet res = statement.executeQuery();
            // Get the PreparedStatement's metadata
            // which will be used to get the
            // column count, as well as the
            // column names
            final ResultSetMetaData metaData = res.getMetaData();

            // Initialize a LinkedHashMap, since
            // HashMap messes up with the order
            final HashMap< Integer, Object > tableRows = new LinkedHashMap<>();

            // This index will be used
            // as a row's index when
            // returning as JSON
            int index = 0;
            while ( res.next() ) { // Check if there are still rows to be returned
                final HashMap< String, String > specificRow = new LinkedHashMap<>();
                for ( int i = 1; i <= metaData.getColumnCount(); i++ ) {
                    // Put the column name from the
                    // position [i] as the key and the
                    // value under the said column as
                    // the value for the LinkedHashMap
                    specificRow.put( metaData.getColumnName( i ),
                            res.getString( i ) );
                }
                // Put the [specificRow] LinkedHashMap
                // to the [tableRows] variable as the
                // value and the current [index] as
                // the key, then increment it
                tableRows.put( index++, specificRow );
            }

            // Returns the HashMap as a JSON
            // and with the proper HTTP status
            // as the responses
            return new ResponseEntity<>( tableRows, HttpStatus.OK );
        } catch ( Exception err ) {
            System.err.println( err.getMessage() );
        }

        // Returns an error code 400 for
        // the HTTP Status as the response,
        // in case the catch-block was
        // executed instead
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
    }

    public ResponseEntity<?> deleteTable( String tableName ) {
        try ( final Connection conn = dataSource.getConnection() ) {
            // Prepare a query statement to be
            // executed later in your MySQL
            // database which will remove
            // the table with the provided
            // name
            final PreparedStatement statement = conn.prepareStatement( "DROP TABLE " + tableName );
            // This will execute the query statement
            // and will return either 0 or 1, whenever
            // the query was successfully executed
            final int res = statement.executeUpdate();

            // The 'res' variable should only
            // have 0, since the statement will
            // drop the table itself and no rows
            // will be affected
            if ( res == 0 ) return new ResponseEntity<>( HttpStatus.OK );
        } catch ( Exception err ) {
            System.err.println( err.getMessage() );
        }

        return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
    }
}
