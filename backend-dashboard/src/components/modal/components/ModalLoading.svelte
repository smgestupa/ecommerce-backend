<script>
    import { fade } from 'svelte/transition';
    import Loading from "../../../svg_animated/Loading.svelte";
</script>

<section class="modal-component" in:fade={ { duration: 300 } }>
    <div class="modal-component-spacing">
        <!-- Loading svg -->
        <figure class="modal-component-loading">
            <Loading />
        </figure>

        <!-- Component message -->
        <section class="modal-component-message">
            <h1 class="modal-loading-message">Please wait...</h1>
            <h3 class="modal-loading-submsg">This may take a while, but something will show up if the action was successful or not.</h3>
        </section>
    </div>
</section>