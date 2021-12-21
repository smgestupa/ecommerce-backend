<script>
    import { fade } from "svelte/transition";
    import Loading from "../svg_animated/Loading.svelte";
    import Dashboard from "../components/index/Dashboard.svelte";
    let tables = [];

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
            throw new Error( 'It looks like the backend server isn\'t responding, you should check your Spring Boot application' );
        }
    }
</script>

{ #await fetchTables() } 
    <div class="flex justify-center h-[50vh]">
        <div class="mt-[25vh] space-y-10">
            <!-- Loading animated SVG component -->
            <Loading />

            <!-- Loading message -->
            <h3 class="text-2xl text-gray-300">Please wait for the backend to respond back...</h3>
        </div>
    </div>
{ :then _ }
    <div in:fade={ { duration: 300 } }>
        <Dashboard tables={ tables } />
    </div>
{ :catch err }
    <div class="flex justify-center h-[50vh]">
        <div class="mt-[25vh] space-y-10">
            <!-- Loading animated SVG component -->
            <Loading />

            <!-- Loading messages -->
            <div class="text-center space-y-2.5">
                <h3 class="text-2xl text-red-400">{ err.message }</h3>
                <h3 class="text-lg text-red-300">Most likely your backend is offline, you should check regardless.</h3>
            </div>
        </div>
    </div>
{ /await }