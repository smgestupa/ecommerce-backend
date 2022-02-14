<script>
    import { fade } from 'svelte/transition';
    import LeftArrow from '$icons/svg/LeftArrow.svelte';
    import Warning from '$icons/svg/Warning.svelte';
</script>

<section class="back" 
in:fade={ { duration: 300 } }>
    <!-- Back button -->
    <a class="back-button" 
    href="/">
        <div class="back-arrow">
            <!-- Left arrow SVG component -->
            <LeftArrow />
        </div>

        <!-- Back button text -->
        <div class="back-text">
            <h3>Go back</h3>
        </div>
    </a>
</section>

<section class="loading">
    <header class="loading-header">
        <!-- Loading animated SVG component -->
        <figure class="flex justify-center">
            <Warning class={ "text-center scale-[7.5] m-auto fill-yellow-400" }/>
        </figure>

        <!-- Loading messages -->
        <div class="loading-error">
            <h3 class="loading-error-message">This page does not exist, go back to the dashboard</h3>
        </div>
    </header>
</section>