<script>
    // Imports
    import { fade } from "svelte/transition";
    import { dashboardRefresh, showConfirmDeleteTableModal } from "$stores/stores.js";
    import { Loading, Warning } from "$icons/svg.js";
    import Dashboard from "$components/index/Dashboard.svelte";
    import ConfirmDeleteTable from "$components/modal/ConfirmDeleteTable.svelte";

    // Variables
    let tables = [], selectedTable = "";

    // This will fetch all available tables
    // from the specified database
    const fetchTables = async () => {
        try {
            // Fetch table rows in JSON format
            const req = await fetch( 'http://localhost:8093/api/v1/tables', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        } );
        const res = await req.json();
        
        // Separate admin tables from catalog tables
        const authTables = res.filter( table => table.description === "authorization/authentication" );
        const catalogTables = res.filter( table => table.description === "catalog" );

        tables = [ authTables, catalogTables ]; // Pass the two separate tables into the [tables] array
        } catch ( err ) {
            // Throw error if catch-block is executed
            throw new Error( 'It seems like the backend server isn\'t responding, try refreshing this page' );
        }
    }

    // This will be used to open the modal for deleting tables
    const openConfirmDeleteTableModal = ( tableName ) => {
        selectedTable = tableName; // Pass in the name of the selected table
        $showConfirmDeleteTableModal = true; // Show the modal
    }
</script>

<svelte:head>
    <title>Dashboard</title>
</svelte:head>

{ #await fetchTables() } 
    <section class="loading">
        <header class="loading-header">
            <!-- Loading animated SVG component -->
            <figure>
                <Loading class={ "loading-icon" } />
            </figure>

            <!-- Loading message -->
            <h3 class="loading-message">Please wait for the backend to respond back...</h3>
        </header>
    </section>
{ :then _ }
    { #if $showConfirmDeleteTableModal }
        <ConfirmDeleteTable tableName={ selectedTable } />
    { /if }
    
    { #if !$dashboardRefresh }
        <main in:fade={ { duration: 300 } }>
            <Dashboard tables={ tables }
            openConfirmDeleteTableModal={ openConfirmDeleteTableModal } />
        </main>
    { /if }
{ :catch err }
    <section class="loading">
        <header class="loading-header">
            <!-- Loading animated SVG component -->
            <figure>
                <Warning class={ "warning-icon" } />
            </figure>

            <!-- Loading messages -->
            <div class="loading-error">
                <h1 class="loading-error-message">{ err.message }</h1>
                <h3 class="loading-error-submsg">Most likely your backend is offline, you should check your Spring Boot application</h3>
            </div>
        </header>
    </section>
{ /await }