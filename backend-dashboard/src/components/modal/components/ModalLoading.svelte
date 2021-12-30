<script>
    import { fade } from 'svelte/transition';
    import Loading from "../../../svg_animated/Loading.svelte";
</script>

<div class="flex justify-center px-5 pt-16 pb-8 space-y-10" in:fade={ { duration: 300 } }>
    <div class="space-y-14">
        <!-- Loading svg -->
        <div class="scale-[150%]">
            <Loading />
        </div>

        <!-- Component message -->
        <div class="text-center w-[25rem] space-y-1.5">
            <h3 class="text-2xl">Please wait...</h3>
            <h3 class="text-lg text-gray-600">This may take a while, but something will show up if the action was successful or not.</h3>
        </div>
    </div>
</div>