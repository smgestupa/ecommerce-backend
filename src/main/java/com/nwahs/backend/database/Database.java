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

    @Autowired
    protected DataSource dataSource; // Used to connect/execute statements to the database

    /**
     *
     * @return A ResponseEntity with JSON body, or an error code 400 if something fails
     */
    protected ResponseEntity<?> getTables() {
        // Try-with-resources is used to allow
        // connections be disposed immediately
        // after communicating to the database
        try ( final Connection conn = dataSource.getConnection() ){
            // This will open a connection, then
            // get existing tables from the metadata
            final DatabaseMetaData metaData = conn.getMetaData();
            final ResultSet tables = metaData.getTables( null, null, null, new String[] { "TABLE" } );

            final List<Table> tablesList = new ArrayList<>();
            while ( tables.next() ) {
                // Get the table name and the comment,
                // from the ResultSet metadata
                final String tableName = tables.getString( "TABLE_NAME" );
                final String tableDescription = tables.getString( "TABLE_COMMENT" );

                // Add the strings inside this list,
                // which will be used as the response
                tablesList.add( new Table( tableName, tableDescription ) );
            }

            // Returns the List as a JSON,
            // along with the proper HTTP
            // status as the responses
            return new ResponseEntity<>( tablesList, HttpStatus.OK );
        } catch ( Exception err ) {
            System.err.println( err.getMessage() );
        }

        return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
    }

    /**
     *
     * @param tableName The table name
     * @param page Table rows' starting offset
     * @return A ResponseEntity with JSON body, or an error code 400 if something fails
     */
    protected ResponseEntity<?> getTableRows( String tableName, int page ) {
        try ( final java.sql.Connection conn = dataSource.getConnection() ) {
            // Prepare a statement, which will
            // be used to get table rows
            final String sqlStatement = "SELECT * FROM " + tableName + " LIMIT 11 OFFSET " + ( page * 10 ) + ";";
            final PreparedStatement prepareStatement = conn.prepareStatement( sqlStatement ); // This will be used to communicate to the database
            final ResultSet res = prepareStatement.executeQuery(); // This will return certain rows, if they exist

            // Get the metadata, to get
            // the number of columns and
            // their names
            final ResultSetMetaData metaData = res.getMetaData();

            // Initialize a LinkedHashMap, to
            // prevent default sorting
            final HashMap< Integer, Object > tableRows = new LinkedHashMap<>();

            int index = 0; // This will be used to act as the row index
            while ( res.next() ) {
                final HashMap<String, String> thisRow = new LinkedHashMap<>();
                for ( int i = 1; i <= metaData.getColumnCount(); i++ ) {
                    // The column name will be the key,
                    // and the value as the pair
                    thisRow.put( metaData.getColumnName( i ), res.getString( i ) );
                }
                tableRows.put( index++, thisRow ); // Add to the LinkedHashMap variable
            }

            return new ResponseEntity<>( tableRows, HttpStatus.OK );
        } catch ( Exception err ) {
            System.err.println( err.getMessage() );
        }

        return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
    }

    /**
     *
     * @param tableName The table name
     * @param tableColumn Specific table column name
     * @param keyword A string used as the basis for finding a specific result, using regexp
     * @return A ResponseEntity with JSON body, or an error code 400 if something fails
     */
    protected ResponseEntity<?> searchTableRow( String tableName, String tableColumn, String keyword ) {
        try ( final java.sql.Connection conn = dataSource.getConnection() ) {
            // Prepare a statement, which will
            // be used to get a specific table
            // row
            final String sqlStatement = "SELECT * FROM " + tableName + " WHERE " + tableColumn + " IN ( '" + keyword + "' ) LIMIT 10;";
            final PreparedStatement prepareStatement = conn.prepareStatement( sqlStatement );
            final ResultSet res = prepareStatement.executeQuery();

            final ResultSetMetaData metaData = res.getMetaData();

            final HashMap<Integer, Object> tableRows = new LinkedHashMap<>();

            int index = 0;
            while ( res.next() ) {
                final HashMap<String, String> rowValues = new LinkedHashMap<>();
                for ( int i = 1; i <= metaData.getColumnCount(); i++ ) {
                    rowValues.put( metaData.getColumnName( i ), res.getString( i ) );
                }
                tableRows.put( index++, rowValues );
            }

            // If there are no rows found,
            // then return rows from the
            // first page instead
            if ( tableRows.isEmpty() ) return getTableRows( tableName, 0 );
            return new ResponseEntity<>( tableRows, HttpStatus.OK );
        } catch ( Exception err ) {
            System.err.println( err.getMessage() );
        }

        return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
    }

    /**
     *
     * @param tableName The table name
     * @param providedColumns Specific columns where a row value can be added
     * @param providedRows Rows with values, that is relative to the number of specified columns
     * @return A ResponseEntity with a code 200, or an error code 400 if something fails
     */
    protected ResponseEntity<?> addTableRows( String tableName, String[] providedColumns, JSONArray providedRows ) {
        try ( final java.sql.Connection conn = dataSource.getConnection() ) {
            // StringBuilder will be used to append
            // column and row values with a for-loop
            final StringBuilder sqlStatement = new StringBuilder( "INSERT INTO " + tableName );

            // This loop will be used to append
            // provided columns into the string
            sqlStatement.append( " ( " + providedColumns[ 0 ] ); // Append the first column for proper SQL formatting
            for ( int i = 1; i < providedColumns.length; i++ ) {
                sqlStatement.append( ", " + providedColumns[ i ] );
            }
            sqlStatement.append( " )\nVALUES\n" );

            // This loop will be used to append
            // individual row values into the
            // string
            for ( int i = 0; i < providedRows.length(); i++ ) {
                final JSONObject row = providedRows.getJSONObject( i );

                // Append the first row value, for proper SQL formatting
                sqlStatement.append( "  ( " );
                sqlStatement.append( "'" + row.get( providedColumns[ 0 ] ) + "'" );

                // This loop will be used to append
                // row values of this current row
                for ( int r = 1; r < providedColumns.length; r++ ) {
                    sqlStatement.append( ", '" + row.get( providedColumns[ i ] ) + "'" );
                }

                sqlStatement.append( " )" );

                // If in the last iteration,
                // then append a semi-colon,
                // else, move to next line
                sqlStatement.append( i == providedRows.length() - 1 ? ";" : ",\n" );
            }

            // Use the appended strings from the
            // StringBuilder as the statement
            final PreparedStatement prepareStatement = conn.prepareStatement( sqlStatement.toString() );

            // Execute, and this will return a value
            // more than 0, signifying that table rows
            // were modified
            final int res = prepareStatement.executeUpdate();

            if ( res != 0 ) return new ResponseEntity<>( HttpStatus.OK );
        } catch ( Exception err ) {
            System.err.println( err.getMessage() );
        }

        return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
    }

    /**
     *
     * @param tableName The table name
     * @param newRow The new row values
     * @param oldRow The old row values
     * @return A ResponseEntity with a code 200, or an error code 400 if something fails
     */
    protected ResponseEntity<?> editTableRow(String tableName, LinkedHashMap<?, ?> newRow, LinkedHashMap<?, ?> oldRow ) {
        try ( final java.sql.Connection conn = dataSource.getConnection() ) {
            // StringBuilder will be used to append
            // old row and new row values
            final StringBuilder sqlStatement = new StringBuilder( "UPDATE " + tableName + "\nSET\n");

            final Object[] newRowKeys = newRow.keySet().toArray(); // Keys of new row values
            final Object[] newRowValues = newRow.values().toArray(); // Pair of new row values

            // Append the first statement for
            // proper SQL statement formatting
            sqlStatement.append( "  " + newRowKeys[0]  + " = '" + newRowValues[0] + "'" );

            // This loop will be used to append
            // the key-value pair of the new row
            for ( int i = 1; i < newRow.size(); i++ ) {
                sqlStatement.append( ",\n" );
                sqlStatement.append( "  " + newRowKeys[ i ]  + " = '" + newRowValues[ i ] + "'" );
            }
            sqlStatement.append( "\nWHERE\n" );

            final Object[] oldRowKeys = oldRow.keySet().toArray(); // Keys of old row values
            final Object[] oldRowValues = oldRow.values().toArray(); // Pair of old row values

            sqlStatement.append( "  " + oldRowKeys[0]  + " = '" + oldRowValues[0] + "'" );

            // This loop will be used to append
            // the key-value pair of the old row
            for ( int i = 1; i < oldRow.size(); i++ ) {
                sqlStatement.append( " AND\n" );
                sqlStatement.append( "  " + oldRowKeys[ i ]  + " = '" + oldRowValues[ i ] + "'" );
            }
            sqlStatement.append( ";" );

            // Prepare a statement, which will
            // be used to replace the old row
            // with new row values
            final PreparedStatement prepareStatement = conn.prepareStatement( sqlStatement.toString() );
            final int res = prepareStatement.executeUpdate();

            if ( res != 0 ) return new ResponseEntity<>( HttpStatus.OK );
        } catch ( Exception err ) {
            System.err.println( err.getMessage() );
        }

        return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
    }

    /**
     *
     * @param tableName The table name
     * @return A ResponseEntity with a code 200, or an error code 400 if something fails
     */
    protected ResponseEntity<?> deleteTable( String tableName ) {
        try ( final java.sql.Connection conn = dataSource.getConnection() ) {
            // Prepare a statement, which will
            // be used to delete the specified
            // table
            final String sqlStatement = "DROP TABLE " + tableName;
            final PreparedStatement prepareStatement = conn.prepareStatement( sqlStatement );

            // Execute, and this will return a value
            // of 0 when successful, or else, it will
            // return a value of 1
            final int res = prepareStatement.executeUpdate();

            if ( res == 0 ) return new ResponseEntity<>( HttpStatus.OK );
        } catch ( Exception err ) {
            System.err.println( err.getMessage() );
        }

        return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
    }

    /**
     *
     * @param tableName The table name
     * @param rowToBeDeleted Specified row to be deleted
     * @return A ResponseEntity with a code 200, or an error code 400 if something fails
     */
    protected ResponseEntity<?> deleteTableRow( String tableName, Map< String, Object > rowToBeDeleted  ) {
        try ( final java.sql.Connection conn = dataSource.getConnection() ) {
            final StringBuilder sqlStatement = new StringBuilder( "DELETE FROM " + tableName + " WHERE\n" );

            final Object[] selectedRowKeys = rowToBeDeleted.keySet().toArray(); // Keys of the selected row for deletion
            final Object[] selectedRowValues = rowToBeDeleted.values().toArray(); // Pair of the selected row for deletion

            sqlStatement.append( "    " + selectedRowKeys[0] + " = " + " '" + selectedRowValues[0] + "'" );

            // This loop will be used to append
            // the column and its relative row
            // value
            for ( int i = 1; i < rowToBeDeleted.size(); i++ ) {
                sqlStatement.append( " AND\n" );
                sqlStatement.append( "    " + selectedRowKeys[ i ] + " = " + " '" + selectedRowValues[ i ] + "'" );
            }
            sqlStatement.append( ";" );

            // Prepare a statement, which will
            // be used to delete the specified
            // row
            final PreparedStatement statement = conn.prepareStatement( sqlStatement.toString() );

            // Execute, and this will return a value
            // of 1 when successful, or else, it will
            // return a value of 0
            final int res = statement.executeUpdate();

            if ( res == 1 ) return new ResponseEntity<>( HttpStatus.OK );
        } catch ( Exception err ) {
            System.err.println( err.getMessage() );
        }

        return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
    }
}
