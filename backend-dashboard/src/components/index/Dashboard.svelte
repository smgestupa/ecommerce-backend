<script>
    import { Trash, Eye } from "$icons/svg.js";
    export let tables, openConfirmDeleteTableModal;
</script>

<div class="dashboard">
    <!-- Backend administration title -->
    <header class="dashboard-header">
        <h1>Backend administration</h1>
    </header>

    <div class="dashboard-content">
        <!-- Tables -->
        <div class="backend-tables">
            <!-- Authentication and authorization -->
            <div>
                <!-- Table title -->
                <header class="dashboard-table-header">
                    <h1>Authentication and Authorization</h1>
                </header>
                
                <!-- Table contents -->
                <div class="dashboard-table-contents">
                    { #each tables[0] as authTable }
                            <div class="dashboard-table">
                                <!-- Content title -->
                                <a href="/table/{ authTable.name }">
                                    { authTable.name.charAt( 0 ).toUpperCase() + authTable.name.substring( 1 ) }
                                </a>

                                <!-- Content options -->
                                <div class="dashboard-table-options">
                                    <!-- View button -->
                                    <a class="dashboard-table-view"
                                    href="/table/{ authTable.name }">
                                        <figure>
                                            <Eye />
                                        </figure>
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
                <header class="dashboard-table-header">
                    <h1>Catalog</h1>
                </header>

                <!-- Table contents -->
                <div class="dashboard-table-contents">
                    { #each tables[1] as catalogTable }
                        <div class="dashboard-table">
                            <!-- Content title -->
                            <a href="/table/{ catalogTable.name }">
                                { catalogTable.name.charAt(0).toUpperCase() + catalogTable.name.substring( 1 ) }
                            </a>

                            <!-- Content options -->
                            <div class="dashboard-table-options">
                                <!-- View button -->
                                <a class="dashboard-table-view"
                                href="/table/{ catalogTable.name }">
                                    <figure>
                                        <Eye />
                                    </figure>
                                    <h3>View</h3>
                                </a>

                                <!-- Delete button -->
                                <button class="dashboard-table-delete" 
                                on:click={ openConfirmDeleteTableModal( catalogTable.name.charAt( 0 ).toUpperCase() + catalogTable.name.substring( 1 ) ) }>
                                    <figure>
                                        <Trash />
                                    </figure>
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