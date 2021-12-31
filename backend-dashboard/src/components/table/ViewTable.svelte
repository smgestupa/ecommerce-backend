<script>
    import { fade } from "svelte/transition";
    export let tableHeaders, tableRows, rowIndex, changeRowIndex, setSelectedTableData;
</script>

<div in:fade={ { duration: 300 } }>
    <!-- Table headers & rows -->
    <table class="border-separate shadow-lg">
        <tr class="text-center uppercase">
            { #each tableHeaders as header }
                <th class="bg-blue-300 font-semibold px-12 py-2">{ header }</th>
            { /each }
        </tr>
        { #each { length: Object.keys( tableRows ).length } as _, index }
            <tr class="text-center 
            { rowIndex === index + 1 ?
            'bg-blue-100' :
            'bg-white' }
            hover:bg-blue-100 cursor-pointer duration-300"
            on:click={ () => { changeRowIndex( index ); setSelectedTableData( index ) } }>
                { #each Object.values( tableRows[ index ] ) as column }
                    <td class="px-4 py-2.5">{ column }</td> 
                { /each }
            </tr>
        { /each }
    </table>
</div>