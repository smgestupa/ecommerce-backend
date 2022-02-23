<script>
    /**
    *  Imports
    */

    import { fade } from 'svelte/transition';
    import { CircularCheck, CircularCross } from "$icons/svg.js";


    /**
    *  Variables 
    */

    export let statusCode = 400, statusMessage; // Prop variable(s)
    const success = statusCode === 200;
</script>

<section class="modal-component" in:fade={ { duration: 300 } }>
    <div class="modal-component-spacing">
        <!-- Component svg -->
        <header class="modal-component-header">
            { #if success }
                <figure>
                    <CircularCheck class={ "modal-status-success" } />
                </figure>
            { :else }
                <figure>
                    <CircularCross class={ "modal-status-failed" } />
                </figure>
            { /if }
            </header>

        <!-- Component message -->
        <section class="modal-component-message">
            { #if success }
                <h1 class="modal-message-success">Success!</h1>
                <h3 class="modal-message-status">{ statusMessage }</h3>
            { :else }
                <h3 class="modal-message-failed">Something went wrong! Will be closing shortly.</h3>
            { /if }
        </section>
    </div>
</section>