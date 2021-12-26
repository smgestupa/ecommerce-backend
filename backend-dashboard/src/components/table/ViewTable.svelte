<script>
    import { confirmDeleteRow_Index } from "../../stores/stores.js";
    export let tableHeaders, tableRows;
    
    $confirmDeleteRow_Index = -1;
</script>

<div>
    <!-- Table headers & rows -->
    <table class="border-separate shadow-lg">
        <tr class="text-center uppercase">
            { #each tableHeaders as header }
                <th class="bg-blue-300 font-semibold px-12 py-2">{ header }</th>
            { /each }
        </tr>
        { #each { length: Object.keys( tableRows ).length } as _, index }
            <tr class="text-center bg-white hover:bg-blue-100 cursor-pointer duration-300" 
            on:click={ () => $confirmDeleteRow_Index = index }>
                { #each Object.values( tableRows[ index ] ) as column }
                    <td class="px-4 py-2.5">{ column }</td> 
                { /each }
            </tr>
        { /each }
    </table>
</div>