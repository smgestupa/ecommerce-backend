import { writable } from "svelte/store";

const dashboardRefresh = writable( false );
const showConfirmDeleteTableModal = writable( false );
const confirmDeleteTable_Name = writable( `` );

export { 
        dashboardRefresh,
        showConfirmDeleteTableModal,
        confirmDeleteTable_Name 
        }