<script>
    import { fade } from 'svelte/transition';
    import { CircularCheck, CircularCross } from "../../../icons/svg.js";
    export let statusCode, statusMessage;
    const success = statusCode === 200;
</script>

<div class="flex justify-center px-5 pt-16 pb-8 space-y-10" in:fade={ { duration: 300 } }>
    <div class="space-y-14">
        <!-- Component svg -->
        <div class="flex justify-center scale-[250%]">
            { #if success }
                <div class="text-green-500">
                    <CircularCheck />
                </div>
            { :else }
                <div class="text-red-500">
                    <CircularCross />
                </div>
            { /if }
        </div>

        <!-- Component message -->
        <div class="text-center w-[25rem] space-y-1">
            { #if success }
                <h3 class="font-semibold text-2xl text-green-800">Success!</h3>
                <h3 class="text-lg text-green-700">{ statusMessage }. This will close in 5 seconds.</h3>
            { :else }
                <h3 class="text-2xl text-red-800">Failed.</h3>
                <h3 class="text-lg text-red-700">Something went wrong! Closing in 5 seconds.</h3>
            { /if }
        </div>
    </div>
</div>