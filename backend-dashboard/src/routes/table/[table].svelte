<script context="module">
    export const load = async ( { page } ) => {
        return { props: { tableName: page.params.table } }
    }
</script>

<script>
    import { fade } from "svelte/transition";
    import { showAddRowModal, showConfirmDeleteRowModal, showEditRowModal } from "../../stores/stores.js";
    import { LeftArrow } from "../../icons/svg.js";
    import TableSearch from "../../components/table/TableSearch.svelte";
    import ViewTable from "../../components/table/ViewTable.svelte";
    import AddRow from "../../components/modal/AddRow.svelte";
    import ConfirmDeleteRow from "../../components/modal/ConfirmDeleteRow.svelte";
    import EditRow from "../../components/modal/EditRow.svelte";
    import Loading from "../../svg_animated/Loading.svelte";
    export let tableName;
    let rowIndex = -1;
    let rowData = {};
    let selectedTableData = {};

    tableName = tableName.charAt( 0 ).toUpperCase() + tableName.substring( 1 );

    const changeRowIndex = ( index ) => {
        rowIndex = index + 1;
        rowData = JSON.stringify( Object.entries( table.tableRows )[ index ][ 1 ] );
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

    const setSelectedTableData = ( rowIndex ) => selectedTableData = Object.entries( table.tableRows )[ rowIndex ];

    const tableRefresh = async ( offset, column, query ) => {
        try {
            let tableRows = {};

            if ( column !== undefined && query !== undefined ) tableRows = await searchRow( column, query );
            else tableRows = await getTables();

            let tableHeaders = [];
            for ( const header of Object.keys( tableRows[0] ) ) {
                tableHeaders.push( header );
            }
            
            return { tableRows, tableHeaders };
        } catch ( err ) {
            throw new Error( `Something went wrong with getting the contents of this table: ${ tableName }` );
        }
    }

    const getTables = async () => {
        const req = await fetch( `http://localhost:8093/api/v1/tables/${ tableName.toLowerCase() }`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            } );

        return await req.json();
    }

     const searchRow = async ( column, query ) => {
        const req = await fetch( `http://localhost:8093/api/v1/tables/${ tableName.toLowerCase() }/?column=${ column }&search=${ query }`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            } );

        return res;
     }
</script>


<section class="back" 
in:fade={ { duration: 300 } }>
    <!-- Back button -->
    <a class="back-button" 
    href="/">
        <div class="back-arrow">
            <!-- Left arrow SVG component -->
            <LeftArrow />
        </div>

        <!-- Back button text -->
        <div class="back-text">
            <h3>Go back</h3>
        </div>
    </a>
</section>

{ #await tableRefresh() }
    <section class="loading">
        <header class="loading-header">
            <!-- Loading animated SVG component -->
            <Loading />

            <!-- Loading message -->
            <h3 class="loading-message">Please wait for the table to load...</h3>
        </header>
    </section>
{ :then table }
    { #if $showAddRowModal }
        <AddRow tableName={ tableName }
        tableHeaders={ table.tableHeaders }
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
        tableHeaders={ table.tableHeaders }
        rowIndex={ rowIndex }
        selectedTableData={ selectedTableData }
        tableRefresh={ tableRefresh }/>
    { /if }

    <main class="table" in:fade={ { duration: 300 } }>
        <div class="table-content">
            <!-- Table name -->
            <section class="table-overview">
                <header class="table-overview-header">
                    <h1>{ tableName }</h1>
                </header>

                <!-- Table rows buttons -->
                <div class="table-overview-options">
                    <!-- Add row button -->
                    <button 
                    class="table-overview-add"
                    on:click={ () => openAddRowModal() }>
                        <h3>Add row/s</h3>
                    </button>

                    <!-- Edit button -->
                    <button class="table-overview-button
                    { rowIndex === -1 ?
                    'table-overview-inactive' :
                    'table-overview-edit' }"
                    on:click={ () => openEditRowModal() }>
                        <h3>Edit row</h3>
                    </button>

                    <!-- Delete row button -->
                    <button class="table-overview-button
                    { rowIndex === -1 ? 
                    'table-overview-inactive' :
                    'table-overview-delete' } 
                    px-6"
                    on:click={ () => openConfirmDeleteRowModal() }>
                        <h3>Delete row</h3>
                    </button>
                </div>

                <TableSearch columns={ table.tableHeaders }/>
            </section>

            <!-- View table component -->
            <ViewTable tableHeaders={ table.tableHeaders }
            tableRows={ table.tableRows }
            rowIndex={ rowIndex }
            changeRowIndex={ changeRowIndex }
            setSelectedTableData={ setSelectedTableData }/>
        </div>
    </main>
{ :catch err }
    <section class="loading">
        <header class="loading-header">
            <!-- Loading animated SVG component -->
            <Loading />

            <!-- Loading messages -->
            <div class="loading-error">
                <h3 class="loading-error-message">{ err.message }</h3>
                <h3 class="loading-error-submsg">Most likely this table has no existing rows, you should check this in your database</h3>
            </div>
        </header>
    </section>
{ /await }