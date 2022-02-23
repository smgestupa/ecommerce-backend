<script>
    /**
    *  Imports
    */

    import { fly, fade } from 'svelte/transition';
    import { showConfirmDeleteRowModal } from "$stores/stores.js";
    import { X, Warning } from "$icons/svg.js";
    import ModalLoading from "$components/modal/components/ModalLoading.svelte";
    import ModalStatus from "$components/modal/components/ModalStatus.svelte";

    /**
    *  Variables 
    */

    export let tableName, rowData, tableRefresh; // Prop variable(s)
    const statusMessage = "You have successfully delete the table " + tableName + ".";
    let modalLoading = false, statusCode;

    
    /**
    *   Functions 
    */

    const closeModal = () => {
        $showConfirmDeleteRowModal = false;
    }

    const deleteRow = async () => {
        modalLoading = true;

        try {
            // Pass the data of the selected row
            // as the body
            const req = await fetch( `http://localhost:8093/api/v1/tables/${ tableName.toLowerCase() }`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: rowData
            } );
            const res = await req.status;
            
            statusCode = res;
            tableRefresh();
        } catch ( err ) {
            console.error( err );
        }

        modalLoading = false;
        setTimeout( () => closeModal(), 1500 );
    };
</script>

<section class="modal" out:fade={ { duration: 300 } }>
    <div>
        <section class="modal-ui" in:fly={ { y: -200, duration: 300 } }>
            { #if modalLoading }
                <ModalLoading />
            { :else if statusCode !== undefined }
                <ModalStatus statusCode={ statusCode }
                statusMessage={ statusMessage }/>
            { :else }
                <section class="modal-section">
                    <header class="modal-header">
                        <!-- Modal notif title -->
                        <h1>You are about to delete the selected row</h1>
        
                        <!-- Modal close button -->
                        <section class="modal-close">
                            <button on:click={ closeModal }>
                                <X />
                            </button>
                        </section>
                    </header>
        
                    <!-- Modal notif description -->
                    <section class="modal-description-warning">
                        <figure>
                            <Warning class={ "warning-modal-icon" } />
                        </figure>
                        <h3>Please confirm the action that you intend to do.</h3>
                    </section>
        
                    <!-- Modal buttons -->
                    <section class="modal-buttons">
                        <!-- Confirm button -->
                        <div class="modal-button-delete"
                        on:click={ () => deleteRow() }>
                            <button>Confirm</button>
                        </div>
        
                        <!-- Cancel button -->
                        <div class="modal-button-cancel"
                        on:click={ () => closeModal() }>
                            <button>Cancel</button>
                        </div>
                    </section>
                </section>
            { /if }
        </section>
    </div>
</section>