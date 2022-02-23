<script>
    /**
    *  Imports
    */

    import { fade } from "svelte/transition";


    /**
    *  Variables
    */

    export let tableHeaders, tableRows, rowIndex; // Prop variable(s)
    export let changeRowIndex, setSelectedTableData; // Prop function(s)
</script>

<section in:fade={ { duration: 300 } }>
    <!-- Table headers & rows -->
    <table class="view-table">
        <thead>
            <tr class="table-header">
                { #each tableHeaders as header }
                    <th>{ header }</th>
                { /each }
            </tr>
        </thead>
        <tbody>
            { #each { length: Object.keys( tableRows ).length } as _, index }
                <tr class="table-row 
                { rowIndex === index + 1 ?
                'bg-blue-100' :
                'bg-white' }"
                on:click={ () => { changeRowIndex( index ); setSelectedTableData( index ) } }>
                    { #each Object.values( tableRows[ index ] ) as column }
                        <td>{ column.length > 15 ? column.slice( 0, 16 ) + "..." : column }</td> 
                    { /each }
                </tr>
            { /each }
        </tbody>
    </table>
</section>