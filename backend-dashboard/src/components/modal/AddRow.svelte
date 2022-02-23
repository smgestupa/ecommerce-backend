<script>
    /**
    *  Imports
    */

    import { fly, fade } from 'svelte/transition';
    import { onMount } from 'svelte';
    import { showAddRowModal } from "$stores/stores.js";
    import { X, Info } from "$icons/svg.js";
    import ModalLoading from "$components/modal/components/ModalLoading.svelte";
    import ModalStatus from "$components/modal/components/ModalStatus.svelte";


    /**
    *  Variables 
    */

    export let tableName, tableHeaders, tableRefresh; // Prop variable(s)
    const statusMessage = "You have successfully added row(s) to the database.";
    let rows = {}; // List of rows that is expected to be added
    let disabledColumns = [];
    let numberOfRows = 0;
    let modalLoading = false, statusCode;

    
    /**
    *   Functions 
    */

    const addToTable = async () => {
        modalLoading = true;
        
        // Create a variable, that will filter
        // disabled columns
        let columns = tableHeaders.filter( col => !disabledColumns.includes( col ) );
        // Re-assign as a new Map object, and pass the old [columns] as the value
        columns = new Map().set( "columns", columns );
        const rows = saveAddedRows(); // Get all existing (new) rows
        
        const requestBody = new Set(); // Will be used to pass as the body
        // Append data to the Set object, through chaining
        requestBody.add( Object.fromEntries( columns ) ).add( [ ...rows ] );
        
        try {
            const req = await fetch( `http://localhost:8093/api/v1/tables/${ tableName }`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify( [ ...requestBody ] )
            } );
            const res = await req.status;

            statusCode = res;
            tableRefresh();
        } catch ( err ) {
            console.error( err );
        }

        modalLoading = false;
        setTimeout( () => closeModal(), 1500 );
    }

    const closeModal = () => $showAddRowModal = false;

    const initializeRow = () => numberOfRows++;

    const disableColumn = ( headerName ) => {
        if ( disabledColumns.includes( headerName ) ) { // Check if the column is already disabled
            disabledColumns = disabledColumns.filter( column => column !== headerName ); // If it is, then re-enable it
            return;
        }

        // If not already disabled, then
        // disable the selected column
        disabledColumns = disabledColumns.concat( headerName );
    }

    const saveAddedRows = () => {
        const rowsToJSON = new Set(); // Will be used to convert (new) rows into JSON format

        for ( let i = 0; i < numberOfRows; i++ ) { // Loop for each row
            const row = new Map();

            // Loop to get the column and value
            // under it, as the key-value pair
            for ( let r = 0; r < tableHeaders.length; r++ ) {
                // If the specific column is disabled, skip
                if ( disabledColumns.includes( tableHeaders[ r ] ) ) continue;
                row.set( tableHeaders[ r ], rows[ tableHeaders[ r ] + "." + i ] || "" );
            }

            rowsToJSON.add( Object.fromEntries( row ) ); // Pass the key-value pair of the row
        }

        return rowsToJSON;
    }

    const removeTableRow = ( index ) => {
        const table = document.getElementById( "add-table" );

        if ( table.tBodies.length === 1 ) return;
        table.removeChild( table.getElementsByTagName( "tbody" )[ index ] );
    }

    onMount( () => initializeRow() );
</script>

<section class="modal" out:fade={ { duration: 300 } }>
    <div>
        <section class="modal-ui"
        in:fly={ { y: -200, duration: 300 } }>
            { #if modalLoading }
                <ModalLoading />
            { :else if statusCode !== undefined }
                <ModalStatus statusCode={ statusCode }
                statusMessage={ statusMessage }/>
            { :else }
                <section class="modal-section">
                    <header class="modal-header">
                        <!-- Modal notif title -->
                        <h1>You are about to add rows to table <span class="modal-header-add">{ tableName }</span></h1>
    
                        <!-- Modal close button -->
                        <figure class="modal-close">
                            <button on:click={ () => closeModal() }>
                                <X />
                            </button>
                        </figure>
                    </header>
    
                    <!-- Modal notif description -->
                    <section class="modal-description">
                        <figure>
                            <Info class={ "info-modal-icon" } />
                        </figure>
                        <h3>You can add single/multiple rows, drag to delete a column, and disable columns.</h3>
                    </section>
    
                    <!-- Table columns -->
                    <section class="modal-table-columns">
                        <table id="add-table">
                            <thead>
                                <tr class="modal-table-add-header">
                                    { #each tableHeaders as header }
                                        <th class="
                                        { disabledColumns.includes( header ) ? 
                                        'modal-table-add-header-disabled' :
                                        'modal-table-add-header-enabled' }"
                                        on:click={ () => disableColumn( header ) }>
                                            { header }
                                        </th>
                                    { /each }
                                </tr>
                            </thead>
                            { #each { length: numberOfRows } as _, i }
                                <tbody>
                                    <tr class="modal-table-add-row" 
                                    draggable=true
                                    on:dragend={ () => removeTableRow( i ) } >
                                        { #each tableHeaders as header }
                                            <td class=" 
                                            { disabledColumns.includes( header ) ?
                                            'modal-table-add-row-disabled' : 
                                            'modal-table-add-row-enabled' }">
                                                <input type="text"
                                                disabled={ disabledColumns.includes( header ) }
                                                bind:value={ rows[ header + "." + i ] }>
                                            </td> 
                                        { /each }
                                    </tr>
                                </tbody>
                            { /each }
                        </table>
                    </section>
                    
                    <!-- Modal buttons -->
                    <section class="modal-buttons">
                        <!-- Add to table button -->
                        <div class="modal-button-add"
                        on:click={ () => addToTable() }>
                            <button>Add to table</button>
                        </div>
    
                        <!-- Add row button -->
                        <div class="modal-button-row"
                        on:click={ () => initializeRow() }>
                            <button>Add new row</button>
                        </div>
                    </section>
                </section>
            { /if }
        </section>
    </div>
</section>