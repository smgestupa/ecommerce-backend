<script context="module">
    export const load = async ( { page } ) => {
        return { props: { tableName: page.params.table } }
    }
</script>

<script>
    import { fade } from "svelte/transition";
    import { showAddRowModal, showConfirmDeleteRowModal, showEditRowModal } from "../../stores/stores.js";
    import { LeftArrow } from "../../icons/svg.js";
    import ViewTable from "../../components/table/ViewTable.svelte";
    import AddRow from "../../components/modal/AddRow.svelte";
    import ConfirmDeleteRow from "../../components/modal/ConfirmDeleteRow.svelte";
    import EditRow from "../../components/modal/EditRow.svelte";
    import Loading from "../../svg_animated/Loading.svelte";
    export let tableName;
    let viewTableRefresh = false;
    let tableHeaders = [];
    let tableRows = {};
    let rowIndex = -1;
    let rowData = {};
    let selectedTableData = {};

    tableName = tableName.charAt( 0 ).toUpperCase() + tableName.substring( 1 );

    const changeRowIndex = ( index ) => {
        rowIndex = index + 1;
        rowData = JSON.stringify( Object.entries( tableRows )[ index ][ 1 ] );
    }

    const openAddRowModal = () => $showAddRowModal = true;

    const openConfirmDeleteRowModal = () => {
        if ( rowIndex === -1 ) return;
        $showConfirmDeleteRowModal = true;
    }

    const openEditRowModal = () => {
        if ( rowIndex === -1 ) return;
        $showEditRowModal = true;
    }

    const setSelectedTableData = ( rowIndex ) => selectedTableData = Object.entries( tableRows )[ rowIndex ];

    const tableRefresh = async () => {
        viewTableRefresh = true;

        try {
            const req = await fetch( `http://localhost:8093/api/v1/tables/${ tableName.toLowerCase() }`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            } );

            tableRows = await req.json();

            tableHeaders = [];
            for ( const header of Object.keys( tableRows[0] ) ) {
                tableHeaders.push( header );
            }
        } catch ( err ) {
            throw new Error( `Something went wrong with getting the contents of this table: ${ tableName }` );
        }

        viewTableRefresh = false;
    };
</script>


<div class="ml-12" 
in:fade={ { duration: 300 } }>
    <div class="flex">
        <!-- Back button -->
        <a class="flex items-center text-gray-400 hover:text-white cursor-pointer mb-6 space-x-0.5 duration-300" 
        href="/">
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

{ #await tableRefresh() }
    <div class="flex justify-center h-[50vh]">
        <div class="mt-[25vh] space-y-10">
            <!-- Loading animated SVG component -->
            <Loading />

            <!-- Loading message -->
            <h3 class="text-2xl text-gray-300">Please wait for the table to load...</h3>
        </div>
    </div>
{ :then _ }
    { #if $showAddRowModal }
        <AddRow tableName={ tableName }
        tableHeaders={ tableHeaders }
        tableRefresh={ tableRefresh }/>
    { /if }

    { #if $showConfirmDeleteRowModal }
        <ConfirmDeleteRow tableName={ tableName } 
        rowIndex={ rowIndex }
        rowData={ rowData }
        tableRefresh={ tableRefresh }/>
    { /if }

    { #if $showEditRowModal }
        <EditRow tableName={ tableName } 
        tableHeaders={ tableHeaders }
        rowIndex={ rowIndex }
        selectedTableData={ selectedTableData }
        tableRefresh={ tableRefresh }/>
    { /if }

    <div class="flex justify-center" in:fade={ { duration: 300 } }>
        <div class="text-center">
            <!-- Table name -->
            <div class="mb-5">
                <h1 class="font-bold text-white text-3xl">
                    { tableName }
                </h1>

                <!-- Table rows buttons -->
                <div class="flex justify-center items-center mt-5 space-x-5">
                    <!-- Add row button -->
                    <button class="font-semibold text-lg text-green-400 border-[2.5px] border-green-200 rounded-md px-6 py-1.5 hover:border-green-400 duration-300 uppercase"
                    on:click={ () => openAddRowModal() }>
                        <h3>Add row/s</h3>
                    </button>

                    <!-- Edit button -->
                    <button class="font-semibold text-lg
                    { rowIndex === -1 ?
                    'text-gray-400 border-gray-200 hover:border-gray-400' :
                    'text-blue-400 border-blue-200 hover:border-blue-400' }
                    border-[2.5px] rounded-md px-8 py-1.5 duration-300 uppercase"
                    on:click={ () => openEditRowModal() }>
                        <h3>Edit row</h3>
                    </button>

                    <!-- Delete row button -->
                    <button class="font-semibold text-lg
                    { rowIndex === -1 ? 
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
            tableRows={ tableRows }
            rowIndex={ rowIndex }
            changeRowIndex={ changeRowIndex }
            setSelectedTableData={ setSelectedTableData }/>
        </div>
    </div>
{ :catch err }
    <div class="flex justify-center h-[50vh]">
        <div class="mt-[25vh] space-y-10">
            <!-- Loading animated SVG component -->
            <Loading />

            <!-- Loading messages -->
            <div class="text-center space-y-2.5">
                <h3 class="text-2xl text-red-400">{ err.message }</h3>
                <h3 class="text-lg text-red-300">Most likely this table has no existing rows, you should check this in your database</h3>
            </div>
        </div>
    </div>
{ /await }