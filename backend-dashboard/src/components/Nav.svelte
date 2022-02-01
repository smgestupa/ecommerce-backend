<script>
    import Loading from '../svg_animated/Loading.svelte';
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

<nav>
    <!-- Backend website title and status -->
    <header class="nav-content">
        <a class="nav-title" href="/">Svelte + Spring Boot</a>
        <span>|</span>

        <!-- 
            Making this to clickable, in order
            to refresh and show the backend status
            which will change the text, according
            to status
        -->
        <h3 class="backend-status">
            { #await checkBackendStatus() }
                <div class="backend-status-loading">
                    <Loading />
                </div>
            { :then _ }
                <div class="backend-status-loading { isRefreshing ? '' : 'hidden' }">
                    <Loading />
                </div>
            { /await }
            <button 
            class="{ isRefreshing ? 'backend-status-text-loading' : '' } 
            { backendOnline ? 'backend-status-online' : 'backend-status-offline' }" 
            on:click|preventDefault={ checkBackendStatus }>
                { backendOnline ? 'Backend is online' : 'Backend is offline' }
            </button>
        </h3>
    </header>
</nav>