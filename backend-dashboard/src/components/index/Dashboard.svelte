<script>
    import { Trash, Eye } from "$icons/svg.js";
    export let tables, openConfirmDeleteTableModal;
</script>

<div class="ml-12">
<!-- Backend administration title -->
<div class="text-2xl mb-6">
    <h1 class="text-white">Backend administration</h1>
</div>

<div class="flex space-x-10">
        <!-- Tables -->
        <div class="space-y-16 md:w-3/6">
            <!-- Authentication and authorization -->
            <div>
                <!-- Table title -->
                <div class="text-white text-xl uppercase bg-blue-500 px-4 py-2.5">
                    Authentication and Authorization
                </div>
                
                <!-- Table contents -->
                <div class="text-lg">
                    { #each tables[0] as authTable }
                            <div class="flex justify-between font-bold text-blue-400 border-b border-gray-400 px-4 py-4 hover:bg-white hover:bg-opacity-[3%]">
                                <!-- Content title -->
                                <a class="text-blue-300 cursor-pointer" 
                                href="/table/{ authTable.name }">
                                    { authTable.name.charAt( 0 ).toUpperCase() + authTable.name.substring( 1 ) }
                                </a>

                                <!-- Content options -->
                                <div class="flex mr-12 space-x-8">
                                    <!-- View button -->
                                    <a class="flex items-center font-semibold space-x-2 hover:text-gray-200 duration-300" href="/table/{ authTable.name }">
                                        <div class="text-white">
                                            <Eye />
                                        </div>
                                        <h3>View</h3>
                                    </a>
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
                        <div class="flex justify-between font-bold text-blue-400 border-b border-gray-400 px-4 py-4 hover:bg-white hover:bg-opacity-[3%]">
                            <!-- Content title -->
                            <a class="text-blue-300 cursor-pointer" href="/table/{ catalogTable.name }">{ catalogTable.name.charAt(0).toUpperCase() + catalogTable.name.substring( 1 ) }</a>

                            <!-- Content options -->
                            <div class="flex mr-12 space-x-8">
                                <!-- View button -->
                                <a class="flex items-center font-semibold space-x-2 hover:text-gray-200 duration-300" href="/table/{ catalogTable.name }">
                                    <div class="text-white">
                                        <Eye />
                                    </div>
                                    <h3>View</h3>
                                </a>

                                <!-- Delete button -->
                                <button class="flex items-center font-semibold space-x-2 hover:text-red-200 duration-300" 
                                on:click={ openConfirmDeleteTableModal( catalogTable.name.charAt( 0 ).toUpperCase() + catalogTable.name.substring( 1 ) ) }>
                                    <div class="text-red-400">
                                        <Trash />
                                    </div>
                                    <h3>Delete</h3>
                                </button>
                            </div>
                        </div>
                    { /each }
                </div>
            </div>
        </div>
    </div>
</div>