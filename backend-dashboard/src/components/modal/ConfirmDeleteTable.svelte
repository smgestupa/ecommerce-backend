<script>
    import { fly, fade } from 'svelte/transition';
    import { dashboardRefresh, showConfirmDeleteTableModal } from "$stores/stores.js";
    import { X, Warning } from "$icons/svg.js";
    export let tableName;

    const closeModal = () =>{
        $showConfirmDeleteTableModal = false;
    }

    const deleteTable = async () => {
        $dashboardRefresh = true;

        try {
            const req = await fetch( `http://localhost:8093/api/v1/tables/${ tableName.toLowerCase() }`, {
                method: 'DELETE',
            } );
        } catch ( err ) {
            console.error( err );
        }

        $showConfirmDeleteTableModal = false;
        $dashboardRefresh = false;
    };
</script>

<section class="modal" out:fade={ { duration: 300 } }>
    <div>
        <section class="modal-ui-table" in:fly={ { y: -200, duration: 300 } }>
            <header class="modal-header">
                <!-- Modal notif title -->
                <h1>You are about to delete the <span class="modal-header-delete">{ tableName }</span> table</h1>

                <!-- Modal close button -->
                <section class="modal-close">
                    <button on:click={ () => closeModal() }>
                        <X />
                    </button>
                </section>
            </header>

            <!-- Modal notif description -->
            <section class="modal-description-warning">
                <figure>
                    <Warning />
                </figure>
                <h3>Please confirm the action that you intend to do.</h3>
            </section>

            <!-- Modal buttons -->
            <section class="modal-buttons">
                <!-- Confirm button -->
                <div class="modal-button-delete"
                on:click={ () => deleteTable() }>
                    <button>Confirm</button>
                </div>

                <!-- Cancel button -->
                <div class="modal-button-cancel"
                on:click={ () => closeModal() }>
                    <button>Cancel</button>
                </div>
            </section>
        </section>
    </div>
</section>