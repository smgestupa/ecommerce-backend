<script>
    /**
    *  Imports 
    */

    import { fly, fade } from 'svelte/transition';
    import { showEditRowModal } from "$stores/stores.js";
    import { X, Info } from "$icons/svg.js";
    import ModalLoading from "$components/modal/components/ModalLoading.svelte";
    import ModalStatus from "$components/modal/components/ModalStatus.svelte";


    /**
    *  Variables
    */

    export let tableName, tableHeaders, selectedTableData; // Prop variable(s)
    export let tableRefresh; // Prop function(s)
    const selectedColumnsData = Object.entries( selectedTableData[ 1 ] );
    const newColumnsData = Object.entries( selectedTableData[ 1 ] );
    const statusMessage = "You have successfully edited the selected row.";
    let modalLoading = false, statusCode;


    /**
    *   Functions
    */

    const closeModal = () => $showEditRowModal = false;

    const editRow = async () => {
        modalLoading = true;
    
        const selectedForJSON = Object.fromEntries( new Map( selectedColumnsData ) ); // Data from the selected row
        const newForJSON = Object.fromEntries( new Map( newColumnsData ) ); // New data for the selected row,

        // Pass the current data and new data into
        // a JSON object variable
        const responseBody = JSON.stringify( [ selectedForJSON, newForJSON ] );
        try {
            const req = await fetch( `http://localhost:8093/api/v1/tables/${ tableName }`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: responseBody
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
                        <h1>You are currently editing the selected row</h1>
        
                        <!-- Modal close button -->
                        <section class="modal-close">
                            <button on:click={ () => closeModal() }>
                                <X />
                            </button>
                        </section>
                    </header>
        
                    <!-- Modal notif description -->
                    <section class="modal-description">
                        <figure>
                            <Info class={ "info-modal-icon" } />
                        </figure>
                        <h3>You can edit all or specific columns of the selected row.</h3>
                    </section>
        
                    <!-- Table columns -->
                    <div class="modal-table-columns">
                        <table>
                            <thead>
                                <tr class="modal-table-header">
                                    { #each tableHeaders as header }
                                        <th>
                                            { header }
                                        </th>
                                    { /each }
                                </tr>
                            </thead>
                            <tbody>
                                <tr class="modal-table-row">
                                    { #each { length: tableHeaders.length } as _, index }
                                        <td>
                                            <input type="text"
                                            bind:value={ newColumnsData[ index ][ 1 ] }>
                                        </td> 
                                    { /each }
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    
                    <!-- Modal buttons -->
                    <section class="modal-buttons">
                        <!-- Edit selected row button -->
                        <div class="modal-button-edit">
                            <button on:click={ () => editRow() }>Edit selected row</button>
                        </div>
                    </section>
                </section>
            { /if }
        </section>
    </div>
</section>