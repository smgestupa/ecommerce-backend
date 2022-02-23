import { writable } from "svelte/store";

const dashboardRefresh = writable( false );

// AddRow modal variable(s)
const showAddRowModal = writable( false );

// ConfirmDeleteRow modal variable(s)
const showConfirmDeleteRowModal = writable( false );

// ConfirmDeleteTable modal variable(s)
const showConfirmDeleteTableModal = writable( false );

// EditRow modal variable(s)
const showEditRowModal = writable( false );

export { 
        dashboardRefresh,
        showAddRowModal,
        showConfirmDeleteRowModal,
        showConfirmDeleteTableModal,
        showEditRowModal
        }