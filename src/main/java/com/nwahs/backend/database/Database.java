package com.nwahs.backend.database;

import com.nwahs.backend.models.Table;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class Database {

    // Will be used to connect to
    // database, and you can
    // execute statements with this
    @Autowired
    protected DataSource dataSource;

    protected ResponseEntity<?> getTables() {
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

            final List<Table> tablesList = new ArrayList<>();
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

    protected ResponseEntity<?> getTableRows( String tableName, int offset ) {
        try ( final java.sql.Connection conn = dataSource.getConnection() ) {
            // Prepare a query statement to
            // be executed later in your database
            // which will return the rows of the
            // specified table, if it exists
            final PreparedStatement statement = conn.prepareStatement( "SELECT * FROM " + tableName + " LIMIT 10 OFFSET " + offset + ";" );
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

            return new ResponseEntity<>( tableRows, HttpStatus.OK );
        } catch ( Exception err ) {
            System.err.println( err.getMessage() );
        }

        return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
    }

    protected ResponseEntity<?> searchTableRow( String tableName, String tableColumn, String keyword ) {
        try ( final java.sql.Connection conn = dataSource.getConnection() ) {
            // Prepare a query statement that
            // will search for a specific
            // keyword using regex
            final String statement = "SELECT * FROM " + tableName + " WHERE " + tableColumn + " IN ( '" + keyword + "' ) LIMIT 10;";
            final PreparedStatement preparedStatement = conn.prepareStatement( statement );
            final ResultSet result = preparedStatement.executeQuery();

            final ResultSetMetaData metaData = result.getMetaData();

            final HashMap<Integer, Object> searchedTableRows = new LinkedHashMap<>();

            int index = 0;
            while ( result.next() ) {
                final HashMap<String, String> rowValues = new LinkedHashMap<>();
                for ( int i = 1; i <= metaData.getColumnCount(); i++ ) {
                    rowValues.put( metaData.getColumnName( i ), result.getString( i ) );
                }
                searchedTableRows.put( index++, rowValues );
            }

            System.out.println( searchedTableRows );

            return new ResponseEntity<>( searchedTableRows, HttpStatus.OK );
        } catch ( Exception err ) {
            System.err.println( err.getMessage() );
        }

        return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
    }

    protected ResponseEntity<?> addTableRows( String tableName, String[] providedColumns, JSONArray providedRows ) {
        try ( final java.sql.Connection conn = dataSource.getConnection() ) {
            // Create a StringBuilder object which
            // will be used to append provided row
            // values into the specific table
            final StringBuilder sqlStatement = new StringBuilder( "INSERT INTO " + tableName );

            // Create a for-loop to iterate over
            // [providedColumns] and append those
            // strings into [sqlStatement]
            sqlStatement.append( " ( " + providedColumns[ 0 ] );
            for ( int i = 1; i < providedColumns.length; i++ ) {
                sqlStatement.append( ", " + providedColumns[ i ] );
            }
            sqlStatement.append( " )\nVALUES\n" );

            // Create another for-loop, which is used
            // to iterate over [providedRows] and append
            // them to [sqlStatement]
            for ( int i = 0; i < providedRows.length(); i++ ) {
                final JSONObject selectedRow = providedRows.getJSONObject( i );

                sqlStatement.append( "  ( " );
                sqlStatement.append( "'" + selectedRow.get( providedColumns[ 0 ] ) + "'" );

                for ( int r = 1; r < providedColumns.length; r++ ) {
                    sqlStatement.append( ", '" + selectedRow.get( providedColumns[ i ] ) + "'" );
                }

                sqlStatement.append( " )" );
                // If the for-loop is at the last iteration,
                // then append a semicolon, if not, then
                // append a comma and move to next line
                // which will be used to connect next
                // rows
                sqlStatement.append( i == providedRows.length() - 1 ? ";" : ",\n" );
            }

            // Prepare a query statement that
            // will add the provided row(s) into
            // the specified table
            final PreparedStatement statement = conn.prepareStatement( sqlStatement.toString() );
            // This will execute the query statement
            // and will return a value more than 0,
            // because the statement is used to add rows,
            // therefore, rows are being modified
            final int res = statement.executeUpdate();

            // If [res] is not equals to 0, then
            // that means we have successfully added
            // a row into the specified table
            if ( res != 0 ) return new ResponseEntity<>( HttpStatus.OK );
        } catch ( Exception err ) {
            System.err.println( err.getMessage() );
        }

        return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
    }

    protected ResponseEntity<?> editTableRow(String tableName, LinkedHashMap<?, ?> newRow, LinkedHashMap<?, ?> oldRow ) {
        try ( final java.sql.Connection conn = dataSource.getConnection() ) {
            // Create a StringBuilder object which
            // will be used to append the keys and
            // values of both [newRow] and [oldRow]
            final StringBuilder sqlStatement = new StringBuilder( "UPDATE " + tableName + "\nSET\n");

            // Append the first key and value into
            // the StringBuilder object, and format
            // according to SQL statements
            sqlStatement.append( "  " + newRow.keySet().iterator().next() + " = '" + newRow.values().iterator().next() + "'");
            // Create a for-each loop that will iterate
            // for the key and value of each entry set,
            // then append strings just like the one
            // above
            for ( Map.Entry<?, ?> row : newRow.entrySet() ) {
                // This is used in order to iterate on the second
                // line and avoid duplication of first statement
                if ( newRow.keySet().iterator().next() == row.getKey() ) continue;

                sqlStatement.append( ",\n" );
                sqlStatement.append( "  " + row.getKey() + " = '" + row.getValue() + "'" );
            }

            sqlStatement.append( "\nWHERE\n" );
            sqlStatement.append( "  " + oldRow.keySet().iterator().next() + " = '" + oldRow.values().iterator().next() + "'");
            for ( Map.Entry<?, ?> row : oldRow.entrySet() ) {
                if ( oldRow.keySet().iterator().next() == row.getKey() ) continue;

                sqlStatement.append( " AND \n" );
                sqlStatement.append( "  " + row.getKey() + " = '" + row.getValue() + "'" );
            }
            sqlStatement.append( ";" );

            // Prepare a query statement that
            // will update the selected row with
            // new value(s)
            final PreparedStatement statement = conn.prepareStatement( sqlStatement.toString() );
            final int res = statement.executeUpdate();

            if ( res != 0 ) return new ResponseEntity<>( HttpStatus.OK );
        } catch ( Exception err ) {
            System.err.println( err.getMessage() );
        }

        return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
    }

    protected ResponseEntity<?> deleteTable( String tableName ) {
        try ( final java.sql.Connection conn = dataSource.getConnection() ) {
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

            // The [res] variable should only
            // have 0, since the statement will
            // drop the table itself and no rows
            // will be affected
            if ( res == 0 ) return new ResponseEntity<>( HttpStatus.OK );
        } catch ( Exception err ) {
            System.err.println( err.getMessage() );
        }

        return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
    }

    protected ResponseEntity<?> deleteTableRow( String tableName, Map< String, Object > rowToBeDeleted  ) {
        try ( final java.sql.Connection conn = dataSource.getConnection() ) {
            final StringBuilder sqlStatement = new StringBuilder( "DELETE FROM " + tableName + " WHERE\n" );
            sqlStatement.append( "    " +
                    rowToBeDeleted.entrySet().iterator().next().getKey() + " = '" +
                    rowToBeDeleted.entrySet().iterator().next().getValue() + "'" );

            for ( Map.Entry< String, Object > column : rowToBeDeleted.entrySet() ) {
                if ( rowToBeDeleted.entrySet().iterator().next().getKey().equals( column.getKey() ) ) continue;
                sqlStatement.append( " AND\n    " + column.getKey() + " = " + " '" + column.getValue() + "'" );
            }
            sqlStatement.append( ";" );

            // Prepare a query statement to be
            // executed later in your MySQL
            // database which will remove
            // the table with the provided
            // name
            final PreparedStatement statement = conn.prepareStatement( sqlStatement.toString() );
            // This will execute the query statement
            // and will return either 0 or 1, whenever
            // the query was successfully executed
            final int res = statement.executeUpdate();

            // The [res] variable should only
            // have 0, since the statement will
            // drop the table itself and no rows
            // will be affected
            if ( res == 1 ) return new ResponseEntity<>( HttpStatus.OK );
        } catch ( Exception err ) {
            System.err.println( err.getMessage() );
        }

        return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
    }
}
