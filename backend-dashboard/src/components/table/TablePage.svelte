<script>
    export let page, lastPage; // Prop variables
    export let switchPageRow; // Prop functions
</script>

<!-- Pages list -->
<section class="page-section">
    <div class="pages">
        <button class="page-button"
        class:page-button-disabled={ page === 0 }
        on:click={ switchPageRow( page, true ) }
        disabled={ page === 0 }>
            <span>←</span>
        </button>    
    
        <div class="top-1/2 font-semibold text-lg text-black bg-blue-300 rounded-md px-5 cursor-default py-2.5">
            { page + 1 }
        </div>
        
        <button class="page-button"
        class:page-button-disabled={ lastPage }
        on:click={ switchPageRow( page, false ) }
        disabled={ lastPage }>
            <span>→</span>
        </button>
    </div>
</section>