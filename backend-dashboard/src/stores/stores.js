import { writable } from "svelte/store";

const dashboardRefresh = writable( false );

// ConfirmDeleteTable variables
const showConfirmDeleteTableModal = writable( false );
const confirmDeleteTable_Name = writable( `` );

// ConfirmDeleteRow variables
const tableViewRefresh = writable( false );
const showConfirmDeleteRowModal = writable( false );
const confirmDeleteRow_Index = writable( -1 );

export { 
        dashboardRefresh,
        showConfirmDeleteTableModal,
        confirmDeleteTable_Name,
        tableViewRefresh,
        showConfirmDeleteRowModal,
        confirmDeleteRow_Index
        }