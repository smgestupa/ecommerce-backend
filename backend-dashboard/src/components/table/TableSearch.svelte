<script>
    import { Search } from '$icons/svg.js'
    export let columns; // Prop variables
    export let searchRow; // Prop functions
    let specifiedColumn, searchValue;
</script>

<section class="search-section">
    <!-- Search input -->
    <input class="search-input" type="text" placeholder="Type a query here" bind:value={ searchValue }>

    <!-- Column box -->
    <select name="columns" id="columns" bind:value={ specifiedColumn }>
        { #each columns as column }
            <option value={ column }>{ column }</option>
        { /each }
    </select>

    <!-- Search button -->
    <button class="search-button"
    on:click={ searchRow( specifiedColumn, searchValue ) }>
        <Search />
    </button>
</section>