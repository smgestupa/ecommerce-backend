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
        <a class="nav-title" href="/">E-Commerce Backend Dashboard</a>
    </header>
</nav>