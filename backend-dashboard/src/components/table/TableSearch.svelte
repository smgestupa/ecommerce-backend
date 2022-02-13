<script>
    import { Search } from '$icons/svg.js'
    export let columns; // Prop variables
    export let searchRow; // Prop functions
    let specifiedColumn, searchValue;
</script>

<section class="flex justify-start my-5 space-x-3">
    <!-- Search input -->
    <input class="rounded-md pl-2.5 pr-3.5 w-full outline-none" type="text" placeholder="Type a query here" bind:value={ searchValue }>

    <!-- Column box -->
    <select name="columns" id="columns" bind:value={ specifiedColumn }>
        { #each columns as column }
            <option value={ column }>{ column }</option>
        { /each }
    </select>

    <!-- Search button -->
    <button class="top-1/2 font-semibold text-lg text-gray-700 bg-gray-200 rounded-md px-6 py-2.5 hover:bg-gray-300 duration-300"
    on:click={ searchRow( specifiedColumn, searchValue ) }>
        <Search />
    </button>
</section>