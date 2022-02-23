<script>
    /**
    *  Imports
    */
    import { Trash, Eye } from "$icons/svg.js";

    /**
    *  Variables 
    */

    export let tables, openConfirmDeleteTableModal; // Prop variable(s)
</script>

<section class="dashboard">
    <!-- Backend administration title -->
    <header class="dashboard-header">
        <h1>Backend administration</h1>
    </header>

    <section class="dashboard-content">
        <!-- Tables -->
        <section class="backend-tables">
            <!-- Authentication and authorization -->
            <div>
                <!-- Table title -->
                <header class="dashboard-table-header">
                    <h1>Authentication and Authorization</h1>
                </header>
                
                <!-- Table contents -->
                <section class="dashboard-table-contents">
                    { #each tables[0] as authTable }
                            <section class="dashboard-table">
                                <!-- Content title -->
                                <header>
                                    <a href="/table/{ authTable.name }">
                                        { authTable.name.charAt( 0 ).toUpperCase() + authTable.name.substring( 1 ) }
                                    </a>
                                </header>

                                <!-- Content options -->
                                <section class="dashboard-table-options">
                                    <!-- View button -->
                                    <a class="dashboard-table-view"
                                    href="/table/{ authTable.name }">
                                        <figure>
                                            <Eye class={ "view-icon" } />
                                        </figure>
                                        <h3>View</h3>
                                    </a>
                                </section>
                            </section>
                    { /each }
                </section>
            </div>

            <!-- Catalog -->
            <div>
                <!-- Table title -->
                <header class="dashboard-table-header">
                    <h1>Catalog</h1>
                </header>

                <!-- Table contents -->
                <section class="dashboard-table-contents">
                    { #each tables[1] as catalogTable }
                        <section class="dashboard-table">
                            <!-- Content title -->
                            <header>
                                <a href="/table/{ catalogTable.name }">
                                    { catalogTable.name.charAt(0).toUpperCase() + catalogTable.name.substring( 1 ) }
                                </a>
                            </header>

                            <!-- Content options -->
                            <section class="dashboard-table-options">
                                <!-- View button -->
                                <a class="dashboard-table-view"
                                href="/table/{ catalogTable.name }">
                                    <figure>
                                        <Eye class={ "view-icon" } />
                                    </figure>
                                    <h3>View</h3>
                                </a>

                                <!-- Delete button -->
                                <button class="dashboard-table-delete" 
                                on:click={ openConfirmDeleteTableModal( catalogTable.name.charAt( 0 ).toUpperCase() + catalogTable.name.substring( 1 ) ) }>
                                    <figure>
                                        <Trash class={ "delete-icon" } />
                                    </figure>
                                    <h3>Delete</h3>
                                </button>
                            </section>
                        </section>
                    { /each }
                </section>
            </div>
        </section>
    </section>
</section>