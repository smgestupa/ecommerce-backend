package com.nwahs.backend.controllers;

import com.nwahs.backend.models.Table;
import com.nwahs.backend.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
    @GetMapping( "/tables" )
    public List< Table > getTables() throws Exception {
        // This will open a connection and
        // get the metadata from the database
        // in order to get the table names
        final DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
        final ResultSet tables = metaData.getTables( null, null, null, new String[] { "TABLE" } );

        final List< Table > tablesList = new ArrayList<>();
        while ( tables.next() ) {
            final String tableName = tables.getString( "TABLE_NAME" );
            final String tableDescription = tables.getString( "TABLE_COMMENT" );

            tablesList.add( new Table( tableName, tableDescription ) );
        }

        return tablesList;
    }

    // This will return the rows
    // that exist from the provided
    // {table} parameter
    // e.g. http://localhost:8093/api/v1/tables/users
    // will try to get the rows in the [users] table
    @RequestMapping( "/tables/{table}" )
    public HashMap< Integer, Object > getColumns( @PathVariable( "table" ) String table ) throws Exception {
        // Prepare a query statement to
        // be executed later in your database
        // which will return the rows of the
        // specified table, if it exists
        final PreparedStatement statement = dataSource.getConnection().prepareStatement( "SELECT * FROM " + table );
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

        return tableRows;
    }
}
