<script context="module">
    export const load = async ( { page } ) => {
        let tableRows, tableHeaders = [];
        try {
            const req = await fetch( `http://localhost:8093/api/v1/tables/${ page.params.table }`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            } );

            tableRows = await req.json();

            for ( let i = 0; i < Object.keys( tableRows[0] ).length; i++ ) {
                tableHeaders.push( Object.keys( tableRows[0] )[i] );
            }
        } catch ( err ) {
            throw new Error( `Something went wrong with getting the contents of table ${ page.params.table }.` );
        }

        return { props: { tableName: page.params.table, tableHeaders: tableHeaders, tableRows: tableRows } }
    }
</script>

<script>
    import { fade } from "svelte/transition";
    import { showConfirmDeleteRowModal, confirmDeleteRow_Index } from "../../stores/stores.js";
    import { LeftArrow } from "../../icons/svg.js";
    import ViewTable from "../../components/table/ViewTable.svelte";
    import ConfirmDeleteRow from "../../components/modal/ConfirmDeleteRow.svelte";
    export let tableName, tableHeaders, tableRows;

    const openConfirmDeleteRowModal = () => {
        if ( $confirmDeleteRow_Index === -1 ) return;
        $showConfirmDeleteRowModal = true;
    }
</script>

{ #await { tableName, tableHeaders, tableRows } }
    loading...
{ :then _ }
    { #if $showConfirmDeleteRowModal }
        <ConfirmDeleteRow />
    { /if }

    <div class="ml-12" in:fade={ { duration: 300 } }>
        <div class="flex">
            <!-- Back button -->
            <a class="flex items-center text-gray-400 hover:text-white cursor-pointer mb-6 space-x-0.5 duration-300" href="/">
                <div class="text-xl translate-y-0.5">
                    <!-- Left arrow SVG component -->
                    <LeftArrow />
                </div>

                <!-- Back button text -->
                <div class="text-xl">
                    <h3 class="">Go back</h3>
                </div>
            </a>
        </div>
    </div>

    <div class="flex justify-center" in:fade={ { duration: 300 } }>
        <div class="text-center">
            <!-- Table name -->
            <div class="mb-5">
                <h1 class="font-bold text-white text-3xl">{ tableName.charAt(0).toUpperCase() + tableName.substring( 1 ) }</h1>

                <!-- Table rows buttons -->
                <div class="flex justify-center items-center mt-5 space-x-5">
                    <!-- Add row button -->
                    <button class="font-semibold text-lg text-green-400 border-[2.5px] border-green-200 rounded-md px-6 py-1.5 hover:border-green-400 duration-300 uppercase">
                        <h3>Add row/s</h3>
                    </button>

                    <!-- Edit button -->
                    <button class="font-semibold text-lg
                    { $confirmDeleteRow_Index === -1 ?
                    'text-gray-400 border-gray-200 hover:border-gray-400' :
                    'text-blue-400 border-blue-200 hover:border-blue-400' }
                    border-[2.5px] rounded-md px-8 py-1.5 duration-300 uppercase">
                        <h3>Edit row</h3>
                    </button>

                    <!-- Delete row button -->
                    <button class="font-semibold text-lg
                    { $confirmDeleteRow_Index === -1 ? 
                    'text-gray-400 border-gray-200 hover:border-gray-400' :
                    'text-red-400 border-red-200 hover:border-red-400' } 
                    border-[2.5px] rounded-md px-6 py-1.5 duration-300 uppercase"
                    on:click={ () => openConfirmDeleteRowModal() }>
                        <h3>Delete row</h3>
                    </button>
                </div>
            </div>

            <!-- View table component -->
            <ViewTable tableHeaders={ tableHeaders }
            tableRows={ tableRows }/>
        </div>
    </div>
{ /await }