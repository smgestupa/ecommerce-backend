<script>
    import { fly, fade } from 'svelte/transition';
    import { tableViewRefresh, showEditRowModal } from "$stores/stores.js";
    import { X, Info } from "$icons/svg.js";
    import ModalLoading from "$components/modal/components/ModalLoading.svelte";
    import ModalStatus from "$components/modal/components/ModalStatus.svelte";
    export let tableName, tableHeaders, selectedTableData, rowIndex, tableRefresh;
    const selectedColumnsData = Object.entries( selectedTableData[ 1 ] );
    const newColumnsData = Object.entries( selectedTableData[ 1 ] );
    const statusMessage = "You have successfully edited the selected row";
    let modalLoading = false, statusCode;

    const closeModal = () => $showEditRowModal = false;

    const editRow = async () => {
        modalLoading = true;

        const selectedForJSON = Object.fromEntries( new Map( selectedColumnsData ) );
        const newForJSON = Object.fromEntries( new Map( newColumnsData ) );

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
        } catch ( err ) {
            console.error( err );
        }

        modalLoading = false;
        tableRefresh();
        setTimeout( () => closeModal(), 5000 );
    }
</script>

<div class="absolute inset-0 bg-black bg-opacity-25" out:fade={ { duration: 300 } }>
    <div class="flex justify-center">
        <div class="mt-12 bg-gray-300 rounded-md" 
        style="scrollbar-width: none" 
        in:fly={ { y: -200, duration: 300 } }>
            { #if modalLoading }
                <ModalLoading />
            { :else if statusCode !== undefined }
                <ModalStatus statusCode={ statusCode }
                statusMessage={ statusMessage }/>
            { :else }
                <div class="pl-5 pt-3 pb-3 max-h-[40rem] overflow-auto scroll-smooth">
                    <div class="flex justify-between items-center">
                        <!-- Modal notif title -->
                        <div>
                            <h3 class="text-xl pr-32">You are about edit row no. <span class="font-bold text-2xl text-blue-600 ">{ rowIndex }</span></h3>
                        </div>
        
                        <!-- Modal close button -->
                        <div class="mr-5 translate-y-0.5">
                            <button class="text-2xl text-gray-400 hover:text-gray-500 duration-300" 
                            on:click={ () => closeModal() }>
                                <X />
                            </button>
                        </div>
                    </div>
        
                    <!-- Modal notif description -->
                    <div class="flex items-center bg-gray-100 rounded-md mt-4 mb-2.5 mr-5 pl-2 pr-2 py-2 space-x-2">
                        <div class="text-2xl text-gray-600">
                            <Info />
                        </div>
                        <h3 class="text-gray-600 w-[90%]">You can edit all or specific columns of the selected row.</h3>
                    </div>
        
                    <!-- Table columns -->
                    <div class="flex justify-center items-center mt-4 mb-2.5 mr-5 space-x-5">
                        <table class="border-separate">
                            <tr class="items-center text-center uppercase">
                                { #each tableHeaders as header }
                                    <th class="items-center font-semibold bg-blue-300 text-black px-12 py-2">
                                        { header }
                                    </th>
                                { /each }
                            </tr>
                            <tr class="text-center bg-white">
                                { #each { length: tableHeaders.length } as _, index }
                                    <td class="hover:bg-blue-100 focus-within:bg-blue-100 duration-300">
                                        <input class="px-4 py-2.5 outline-none bg-transparent" 
                                        type="text"
                                        bind:value={ newColumnsData[ index ][ 1 ] }>
                                    </td> 
                                { /each }
                            </tr>
                        </table>
                    </div>
                    
                    <!-- Modal buttons -->
                    <div class="flex items-center mt-6 space-x-3">
                        <!-- Edit selected row button -->
                        <div class="cursor-pointer bg-blue-400 hover:bg-blue-500 rounded-md px-3 py-1 shadow-lg duration-300">
                            <button class="font-semibold text-lg text-white"
                            on:click={ () => editRow() }>Edit selected row</button>
                        </div>
                    </div>
                </div>
            { /if }
        </div>
    </div>
</div>