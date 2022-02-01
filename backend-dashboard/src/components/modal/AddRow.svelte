<script>
    import { fly, fade } from 'svelte/transition';
    import { onMount } from 'svelte';
    import { showAddRowModal } from "$stores/stores.js";
    import { X, Info } from "$icons/svg.js";
    import ModalLoading from "$components/modal/components/ModalLoading.svelte";
    import ModalStatus from "$components/modal/components/ModalStatus.svelte";
    export let tableName, tableHeaders, tableRefresh;
    const statusMessage = "You have successfully added row(s) to the database";
    let rows = {};
    let disabledColumns = [];
    let numberOfRows = 0;
    let modalLoading = false, statusCode;

    const addToTable = async () => {
        modalLoading = true;

        let columns = tableHeaders.filter( col => !disabledColumns.includes( col ) );
        columns = new Map().set( "columns", columns );
        const rows = saveAddedRows();
        
        const requestBody = new Set();
        requestBody.add( Object.fromEntries( columns ) );
        requestBody.add( [ ...rows ] );
        
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
        } catch ( err ) {
            console.error( err );
        }

        modalLoading = false;
        tableRefresh();
        setTimeout( () => closeModal(), 1500 );
    }

    const closeModal = () => $showAddRowModal = false;

    const initializeRow = () => numberOfRows++;

    const disableColumn = ( headerName ) => {
        if ( disabledColumns.includes( headerName ) ) {
            disabledColumns = disabledColumns.filter( column => column !== headerName );
            return;
        }

        disabledColumns = disabledColumns.concat( headerName );
    }

    const saveAddedRows = () => {
        const rowsToJSON = new Set();

        for ( let i = 0; i < numberOfRows; i++ ) {
            const row = new Map();

            for ( let r = 0; r < tableHeaders.length; r++ ) {
                if ( disabledColumns.includes( tableHeaders[ r ] ) ) continue;
                row.set( tableHeaders[ r ], rows[ tableHeaders[ r ] + "." + i ] || "" );
            }

            rowsToJSON.add( Object.fromEntries( row ) );
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

<div class="modal" out:fade={ { duration: 300 } }>
    <div>
        <div class="modal-ui"
        in:fly={ { y: -200, duration: 300 } }>
            { #if modalLoading }
                <ModalLoading />
            { :else if statusCode !== undefined }
                <ModalStatus statusCode={ statusCode }
                statusMessage={ statusMessage }/>
            { :else }
                <div class="modal-section">
                    <header class="modal-header">
                        <!-- Modal notif title -->
                        <h1>You are about to add rows to table <span class="modal-header-add">{ tableName }</span></h1>
    
                        <!-- Modal close button -->
                        <section class="modal-close">
                            <button on:click={ () => closeModal() }>
                                <X />
                            </button>
                        </section>
                    </header>
    
                    <!-- Modal notif description -->
                    <div class="modal-description">
                        <figure>
                            <Info />
                        </figure>
                        <h3>You can add single/multiple rows, drag to delete a column, and disable columns.</h3>
                    </div>
    
                    <!-- Table columns -->
                    <div class="modal-table-columns">
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
                    </div>
                    
                    <!-- Modal buttons -->
                    <div class="modal-buttons">
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
                    </div>
                </div>
            { /if }
        </div>
    </div>
</div>