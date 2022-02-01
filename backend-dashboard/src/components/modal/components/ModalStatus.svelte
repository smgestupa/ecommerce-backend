<script>
    import { fade } from 'svelte/transition';
    import { CircularCheck, CircularCross } from "$icons/svg.js";
    export let statusCode = 400, statusMessage;
    const success = statusCode === 200;
</script>

<div class="modal-component" in:fade={ { duration: 300 } }>
    <div class="modal-component-spacing">
        <!-- Component svg -->
        <header class="modal-component-header">
            { #if success }
                <figure class="modal-status-success">
                    <CircularCheck />
                </figure>
            { :else }
                <figure class="modal-status-failed">
                    <CircularCross />
                </figure>
            { /if }
            </header>

        <!-- Component message -->
        <div class="modal-component-message">
            { #if success }
                <h1 class="modal-message-success">Success!</h1>
                <h3 class="modal-message-status">{ statusMessage }.</h3>
            { :else }
                <h3 class="modal-message-failed">Something went wrong! Will be closing shortly.</h3>
            { /if }
        </div>
    </div>
</div>