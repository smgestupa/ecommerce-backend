<script>
    export let tableHeaders, tableRows;
</script>

<div>
    <!-- Table headers & rows -->
    <table class="border-separate shadow-lg">
        <tr class="text-center uppercase">
            { #each tableHeaders as header }
                <th class="bg-blue-300 font-semibold px-12 py-2">{ header }</th>
            { /each }
        </tr>
            { #each { length: Object.keys( tableRows ).length } as _, i }
                <tr class="text-center bg-white hover:bg-blue-100 cursor-pointer duration-300">
                    { #each Object.values( tableRows[ i ] ) as column }
                    <td class="px-4 py-2.5">{ column }</td> 
                    { /each }
                </tr>
            { /each }
    </table>
</div>