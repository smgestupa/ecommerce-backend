<script>
    import { fly, fade } from 'svelte/transition';
    import { onMount } from 'svelte';
    import { showAddRowModal } from "../../stores/stores.js";
    import { X, Info } from "../../icons/svg.js";
    import ModalLoading from "./components/ModalLoading.svelte";
    import ModalStatus from "./components/ModalStatus.svelte";
    export let tableName, tableHeaders, tableRefresh;
    const statusMessage = "You have successfully added row(s) to the database.";
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
        setTimeout( () => closeModal(), 5000 );
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

    onMount( () => initializeRow() );
    $: console.log( tableHeaders );
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
                            <h3 class="text-xl pr-32">You are about to add rows to table <span class="font-bold text-2xl text-green-600 ">{ tableName }</span></h3>
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
                        <h3 class="text-gray-600 w-[90%]">You can add or remove single/multiple rows, as well as disable columns.</h3>
                    </div>
    
                    <!-- Table columns -->
                    <div class="flex justify-center items-center mt-4 mb-2.5 mr-5 space-x-5">
                        <table class="border-separate">
                            <tr class="items-center text-center uppercase">
                                { #each tableHeaders as header }
                                    <th class="items-center font-semibold px-12 py-2 cursor-pointer
                                    { disabledColumns.includes( header ) ? 
                                    'bg-gray-400 text-white' :
                                    'bg-blue-300 text-black hover:bg-gray-400 hover:text-white' }
                                    duration-300"
                                    on:click={ () => disableColumn( header ) }>
                                        { header }
                                    </th>
                                { /each }
                            </tr>
                            { #each { length: numberOfRows } as _, i }
                                <tr class="text-center bg-white" draggable=true>
                                    { #each tableHeaders as header }
                                        <td class=" 
                                        { disabledColumns.includes( header ) ?
                                        'text-white bg-gray-200' : 
                                        'hover:bg-blue-100 focus-within:bg-blue-100' } 
                                        duration-300">
                                            <input class="px-4 py-2.5 outline-none bg-transparent" 
                                            type="text"
                                            disabled={ disabledColumns.includes( header ) }
                                            bind:value={ rows[ header + "." + i ] }>
                                        </td> 
                                    { /each }
                                </tr>
                            { /each }
                        </table>
                    </div>
                    
                    <!-- Modal buttons -->
                    <div class="flex items-center mt-6 space-x-3">
                        <!-- Add to table button -->
                        <div class="cursor-pointer bg-green-400 hover:bg-green-500 rounded-md px-3 py-1 shadow-lg duration-300"
                        on:click={ () => addToTable() }>
                            <button class="font-semibold text-lg text-white">Add to table</button>
                        </div>
    
                        <!-- Add row button -->
                        <div class="cursor-pointer bg-blue-400 hover:bg-blue-500 rounded-md px-3 py-1 shadow-lg duration-300"
                        on:click={ () => initializeRow() }>
                            <button class="font-semibold text-lg text-white">Add new row</button>
                        </div>
                    </div>
                </div>
            { /if }
        </div>
    </div>
</div>