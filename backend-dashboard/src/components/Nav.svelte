<script>
    import Loading from '../animated_svg/Loading.svelte';
    let username = "Example";
    let isRefreshing, backendOnline = false;

    const checkBackendStatus = async () => {
        if ( isRefreshing ) return;
        isRefreshing = true;

        const req = await fetch( 'http://localhost:8093/', {
            mode: 'no-cors'
        } );
        const status = await req.status;
        
        status === 0 ? backendOnline = true : backendOnline = false;
        isRefreshing = false;
    }
</script>

<div class="flex justify-between items-center text-gray-400 text-md bg-gray-500 mb-8 pt-10 pb-5">
    <!-- Backend website title and status -->
    <div class="flex items-center text-gray-300 ml-12 space-x-3">
        <h1 class="text-2xl">Svelte + Spring Boot</h1>
        <span>|</span>

        <!-- 
            Make this clickable to refresh
            the website status and this
            will change the text
        -->
        <h3 class="flex justify-center items-center text-red-400">
            { #await checkBackendStatus() }
                <div class="absolute -scale-25 { isRefreshing ? '' : 'hidden' }">
                    <Loading />
                </div>
            { :then _ }
                <div class="absolute -scale-25 { isRefreshing ? '' : 'hidden' }">
                    <Loading />
                </div>
            { /await }
            <button class="{ isRefreshing ? 'opacity-0 cursor-default' : '' } 
                { backendOnline ? 'text-green-400' : 'text-red-400' }" 
            on:click|preventDefault={ checkBackendStatus }>
                { backendOnline ? 'Backend is online' : 'Backend is offline' }
            </button>
        </h3>
    </div>

    <!-- Welcome and user-related options -->
    <div class="flex text-lg mr-12 space-x-3">
        <h3 class="text-gray-300">
            Welcome,
            <span class="text-white">{ username }</span>.
        </h3>
        <span>/</span>
        <a class="text-gray-300" href="#">Activities</a>
        <span>/</span>
        <a class="text-gray-300" href="#">Options</a>
        <span>/</span>
        <h3 class="text-gray-300">Logout</h3>
    </div>
</div>