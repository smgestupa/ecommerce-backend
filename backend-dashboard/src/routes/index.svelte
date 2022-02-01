<script>
    import { fade } from "svelte/transition";
    import { dashboardRefresh, showConfirmDeleteTableModal } from "$stores/stores.js";
    import Loading from "../svg_animated/Loading.svelte";
    import Dashboard from "$components/index/Dashboard.svelte";
    import ConfirmDeleteTable from "$components/modal/ConfirmDeleteTable.svelte";
    let tables = [], selectedTable = "";

    const fetchTables = async () => {
        try {
            const req = await fetch( 'http://localhost:8093/api/v1/tables', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        } );
        const res = await req.json();
        
        const authTables = res.filter( table => table.description === "authorization/authentication" );
        const catalogTables = res.filter( table => table.description === "catalog" );
        tables = [ authTables, catalogTables ];
        } catch ( err ) {
            throw new Error( 'It seems like the backend server isn\'t responding, try refreshing this page' );
        }
    }

    const openConfirmDeleteTableModal = ( tableName ) => {
        selectedTable = tableName;
        $showConfirmDeleteTableModal = true;
    }
</script>

{ #await fetchTables() } 
    <div class="loading">
        <div class="loading-header">
            <!-- Loading animated SVG component -->
            <Loading />

            <!-- Loading message -->
            <h3 class="loading-message">Please wait for the backend to respond back...</h3>
        </div>
    </div>
{ :then _ }
    { #if $showConfirmDeleteTableModal }
        <ConfirmDeleteTable tableName={ selectedTable } />
    { /if }
    
    { #if !$dashboardRefresh }
        <div in:fade={ { duration: 300 } }>
            <Dashboard tables={ tables }
            openConfirmDeleteTableModal={ openConfirmDeleteTableModal } />
        </div>
    { /if }
{ :catch err }
    <div class="loading">
        <div class="loading-header">
            <!-- Loading animated SVG component -->
            <Loading />

            <!-- Loading messages -->
            <div class="loading-error">
                <h3 class="loading-error-message">{ err.message }</h3>
                <h3 class="loading-error-submsg">Most likely your backend is offline, you should check your Spring Boot application</h3>
            </div>
        </div>
    </div>
{ /await }