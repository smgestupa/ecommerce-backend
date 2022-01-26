<script>
    import Loading from '../svg_animated/Loading.svelte';
    let username = "Example";
    let isRefreshing, backendOnline = false;

    const checkBackendStatus = async () => {
        if ( isRefreshing ) return;
        isRefreshing = true;
        
        try {
            const req = await fetch( 'http://localhost:8093', {
                mode: 'no-cors'
            } );
            const status = await req.status;
        
            if ( status === 0 ) backendOnline = true;
        } catch ( err ) {
            console.error( "Don\'t worry, most likely the backend is offline,\n" + err );
            backendOnline = false;
        }
    
        isRefreshing = false;
    }
</script>

<div class="flex justify-between items-center text-gray-400 text-md bg-gray-500 mb-8 pt-10 pb-5">
    <!-- Backend website title and status -->
    <div class="flex items-center text-gray-300 ml-12 space-x-3">
        <a class="text-2xl hover:text-white duration-300" href="/">Svelte + Spring Boot</a>
        <span>|</span>

        <!-- 
            Making this to clickable, in order
            to refresh and show the backend status
            which will change the text, according
            to status
        -->
        <h3 class="flex justify-center items-center text-red-400">
            { #await checkBackendStatus() }
                <div class="absolute scale-[-45%]">
                    <Loading />
                </div>
            { :then _ }
                <div class="absolute scale-[-45%] { isRefreshing ? '' : 'hidden' }">
                    <Loading />
                </div>
            { /await }
            <button class="{ isRefreshing ? 'opacity-0 cursor-default' : '' } 
                { backendOnline ? 'text-green-400' : 'text-red-400' } duration-300" 
            on:click|preventDefault={ checkBackendStatus }>
                { backendOnline ? 'Backend is online' : 'Backend is offline' }
            </button>
        </h3>
    </div>
</div>