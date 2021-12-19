<script>
    import Loading from "../animated_svg/Loading.svelte";
    let tables = [];

    const fetchTables = async () => {
        try {
            const req = await fetch( 'http://localhost:8093/api/v1/tables', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        } );
        const res = await req.json();
        
        const authTables = res.filter( table => table.description === "authorization/authentication" );
        const catalogTables = res.filter( table => table.description === "catalog" );
        tables = [ authTables, catalogTables ];
        } catch ( err ) {
            throw new Error( 'Something happened!' );
        }
    }
</script>

{ #await fetchTables() } 
    <div class="flex flex-col h-full">
        <div class="flex-1">
            <Loading />
        </div>
    </div>
{ :then _ }
    <div class="ml-12">
        <!-- Backend administration title -->
        <div class="text-2xl mb-6">
            <h1 class="text-white">Backend administration</h1>
        </div>

        <div class="flex space-x-10">
                <!-- Tables -->
                <div class="space-y-16 w-3/6">
                    <!-- Authentication and authorization -->
                    <div>
                        <!-- Table title -->
                        <div class="text-white text-xl uppercase bg-blue-500 px-4 py-2.5">
                            Authentication and Authorization
                        </div>
                            <!-- Table contents -->
                        <div class="text-lg">
                            { #each tables[0] as authTable }
                                    <div class="flex justify-between font-bold text-blue-400 border-b border-gray-400 px-4 py-4">
                                        <!-- Content title -->
                                        <h3 class="text-blue-300">{ authTable.name.charAt(0).toUpperCase() + authTable.name.substring( 1 ) }</h3>

                                        <!-- Content options -->
                                        <div class="flex mr-12 space-x-8">
                                            <!-- View button -->
                                            <button class="flex items-center font-semibold space-x-2">
                                                <svg class="text-white" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true" role="img" width="1em" height="1em" preserveAspectRatio="xMidYMid meet" viewBox="0 0 32 32"><circle cx="16" cy="16" r="4" fill="currentColor"/><path d="M30.94 15.66A16.69 16.69 0 0 0 16 5A16.69 16.69 0 0 0 1.06 15.66a1 1 0 0 0 0 .68A16.69 16.69 0 0 0 16 27a16.69 16.69 0 0 0 14.94-10.66a1 1 0 0 0 0-.68zM16 22.5a6.5 6.5 0 1 1 6.5-6.5a6.51 6.51 0 0 1-6.5 6.5z" fill="currentColor"/></svg>
                                                <h3 class="">View</h3>
                                            </button>

                                            <!-- Add button -->
                                            <button class="flex items-center font-semibold space-x-2">
                                                <svg class="text-green-400" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true" role="img" width="1em" height="1em" preserveAspectRatio="xMidYMid meet" viewBox="0 0 24 24"><g fill="none"><path fill-rule="evenodd" clip-rule="evenodd" d="M12 1C5.925 1 1 5.925 1 12s4.925 11 11 11s11-4.925 11-11S18.075 1 12 1zm1 15a1 1 0 1 1-2 0v-3H8a1 1 0 1 1 0-2h3V8a1 1 0 1 1 2 0v3h3a1 1 0 1 1 0 2h-3v3z" fill="currentColor"/></g></svg>
                                                <h3 class="">Add</h3>
                                            </button>

                                            <!-- Modify button -->
                                            <button class="flex items-center font-semibold space-x-2">
                                                <svg class="text-yellow-400" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true" role="img" width="1em" height="1em" preserveAspectRatio="xMidYMid meet" viewBox="0 0 36 36"><path class="clr-i-solid clr-i-solid-path-1" d="M4.22 23.2l-1.9 8.2a2.06 2.06 0 0 0 2 2.5a2.14 2.14 0 0 0 .43 0L13 32l15.84-15.78L20 7.4z" fill="currentColor"/><path class="clr-i-solid clr-i-solid-path-2" d="M33.82 8.32l-5.9-5.9a2.07 2.07 0 0 0-2.92 0L21.72 5.7l8.83 8.83l3.28-3.28a2.07 2.07 0 0 0-.01-2.93z" fill="currentColor"/></svg>
                                                <span>Modify</span>
                                            </button>
                                        </div>
                                    </div>
                            { /each }
                        </div>
                    </div>

                    <!-- Catalog -->
                    <div>
                        <!-- Table title -->
                        <div class="text-white text-xl uppercase bg-blue-500 px-4 py-2.5">
                            Catalog
                        </div>
                        <!-- Table contents -->
                        <div class="text-lg">
                            { #each tables[1] as catalogTable }
                                <div class="flex justify-between font-bold text-blue-400 border-b border-gray-400 px-4 py-4">
                                    <!-- Content title -->
                                    <h3 class="text-blue-300">{ catalogTable.name.charAt(0).toUpperCase() + catalogTable.name.substring( 1 ) }</h3>

                                    <!-- Content options -->
                                    <div class="flex mr-12 space-x-8">
                                        <!-- View button -->
                                        <button class="flex items-center font-semibold space-x-2">
                                            <svg class="text-white" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true" role="img" width="1em" height="1em" preserveAspectRatio="xMidYMid meet" viewBox="0 0 32 32"><circle cx="16" cy="16" r="4" fill="currentColor"/><path d="M30.94 15.66A16.69 16.69 0 0 0 16 5A16.69 16.69 0 0 0 1.06 15.66a1 1 0 0 0 0 .68A16.69 16.69 0 0 0 16 27a16.69 16.69 0 0 0 14.94-10.66a1 1 0 0 0 0-.68zM16 22.5a6.5 6.5 0 1 1 6.5-6.5a6.51 6.51 0 0 1-6.5 6.5z" fill="currentColor"/></svg>
                                            <h3 class="">View</h3>
                                        </button>

                                        <!-- Add button -->
                                        <button class="flex items-center font-semibold space-x-2">
                                            <svg class="text-green-400" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true" role="img" width="1em" height="1em" preserveAspectRatio="xMidYMid meet" viewBox="0 0 24 24"><g fill="none"><path fill-rule="evenodd" clip-rule="evenodd" d="M12 1C5.925 1 1 5.925 1 12s4.925 11 11 11s11-4.925 11-11S18.075 1 12 1zm1 15a1 1 0 1 1-2 0v-3H8a1 1 0 1 1 0-2h3V8a1 1 0 1 1 2 0v3h3a1 1 0 1 1 0 2h-3v3z" fill="currentColor"/></g></svg>
                                            <span>Add</span>
                                        </button>

                                        <!-- Modify button -->
                                        <button class="flex items-center font-semibold space-x-2">
                                            <svg class="text-yellow-400" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true" role="img" width="1em" height="1em" preserveAspectRatio="xMidYMid meet" viewBox="0 0 36 36"><path class="clr-i-solid clr-i-solid-path-1" d="M4.22 23.2l-1.9 8.2a2.06 2.06 0 0 0 2 2.5a2.14 2.14 0 0 0 .43 0L13 32l15.84-15.78L20 7.4z" fill="currentColor"/><path class="clr-i-solid clr-i-solid-path-2" d="M33.82 8.32l-5.9-5.9a2.07 2.07 0 0 0-2.92 0L21.72 5.7l8.83 8.83l3.28-3.28a2.07 2.07 0 0 0-.01-2.93z" fill="currentColor"/></svg>
                                            <span>Modify</span>
                                        </button>
                                    </div>
                                </div>
                            { /each }
                        </div>
                    </div>
                </div>

                <!-- Recent activities / History -->
                <div>
                    <div class="bg-gray-300 rounded-sm pl-5 pt-4 pb-8">
                        <!-- Title -->
                        <h3 class="text-xl pr-40">Recent activities</h3>

                        <!-- Custom horizontal bar -->
                        <div class="mt-5 mb-2.5 mr-5">
                            <hr>
                        </div>

                        <!-- Activity history -->
                        <div class="mt-2.5 space-y-2">
                            <div>
                                <h3>None available</h3>
                            </div>
                            <div>
                                <h3>None available</h3>
                            </div>
                            <div>
                                <h3>None available</h3>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
{ :catch err }
    <p class="text-white">{ err.message }</p>
{ /await }