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
    import { LeftArrow } from "../../icons/svg.js";
    import ViewTable from "../../components/table/ViewTable.svelte";
    export let tableName, tableHeaders, tableRows;
</script>

{ #await { tableName, tableHeaders, tableRows } }
    loading...
{ :then _ }
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
            <div class="text-2xl mb-6">
                <h1 class="text-white">{ tableName.charAt(0).toUpperCase() + tableName.substring( 1 ) }</h1>
            </div>

            <!-- View table component -->
            <ViewTable tableHeaders={ tableHeaders }
            tableRows={ tableRows }/>
        </div>
    </div>
{ /await }