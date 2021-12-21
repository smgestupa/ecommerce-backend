<script>
    import { fly, fade } from 'svelte/transition';
    import { dashboardRefresh, showConfirmDeleteTableModal, confirmDeleteTable_Name } from "../../stores/stores.js";
    import { X } from "../../icons/svg.js";

    const closeModal = () =>{
        $showConfirmDeleteTableModal = false;
    }

    const deleteTable = async ( tableName ) => {
        $dashboardRefresh = true;

        try {
            const req = await fetch( `http://localhost:8093/api/v1/tables/${ tableName }?delete=true` );
        } catch ( err ) {
            console.error( err );
        }

        $showConfirmDeleteTableModal = false;
        $dashboardRefresh = false;
    };
</script>

<div class="absolute inset-0 bg-black bg-opacity-25" out:fade={ { duration: 300 } }>
    <div class="flex justify-center">
        <div class="mt-12 bg-gray-300 pl-5 pt-3 pb-5 rounded-md" in:fly={ { y: -200, duration: 300 } }>
            <div class="flex justify-between items-center">
                <!-- Modal notif title -->
                <div>
                    <h3 class="text-xl pr-32">You are about to delete the <span class="font-bold text-2xl text-red-600 ">{ $confirmDeleteTable_Name }</span> table</h3>
                </div>

                <!-- Modal close button -->
                <div class="mr-5 translate-y-0.5">
                    <button class="text-2xl text-gray-400 hover:text-gray-500 duration-300" 
                    on:click={ closeModal }>
                        <X />
                    </button>
                </div>
            </div>

            <!-- Modal notif description -->
            <div class="mt-2.5 mb-2.5 mr-5 w-11/12">
                <h3>Before deleting the table, please confirm the action that you are about to do.</h3>
            </div>

            <!-- Modal buttons -->
            <div class="flex items-center mt-5 space-x-5">
                <!-- Confirm button -->
                <div class="cursor-pointer border-2 border-red-300 hover:border-red-400 rounded-md px-2.5 py-0.5 duration-300"
                on:click={ deleteTable( $confirmDeleteTable_Name.toLowerCase() ) }>
                    <button class="font-semibold text-lg text-red-600">Confirm</button>
                </div>

                <!-- Cancel button -->
                <div class="cursor-pointer border-2 border-gray-400 bg-gray-400 hover:border-gray-500 hover:bg-gray-500 rounded-md px-4 py-0.5 duration-300"
                on:click={ closeModal }>
                    <button class="font-semibold text-lg text-white">Cancel</button>
                </div>
            </div>
        </div>
    </div>
</div>