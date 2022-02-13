<script>
    export let page, lastPage; // Prop variables
    export let switchPageRow; // Prop functions
</script>

<!-- Pages list -->
<section class="flex justify-center mt-6 mb-5">
    <div class="flex items-center space-x-2.5">
        <button class="top-1/2 font-semibold text-lg text-black bg-blue-200 rounded-md px-3 cursor-pointer py-1.5 hover:bg-blue-300 duration-300"
        on:click={ switchPageRow( page, true ) }>
            <span>←</span>
        </button>    
    
        <div class="top-1/2 font-semibold text-lg text-black bg-blue-300 rounded-md px-5 cursor-default py-2.5">
            { page + 1 }
        </div>
        
        <button class="top-1/2 font-semibold text-lg text-black bg-blue-200 rounded-md px-3 cursor-pointer py-1.5 hover:bg-blue-300 duration-300"
        on:click={ switchPageRow( page, false ) }>
            <span>→</span>
        </button>
    </div>
</section>