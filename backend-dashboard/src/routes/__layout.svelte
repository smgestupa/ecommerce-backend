<script>
  import "../app.css";
  import Nav from "../components/Nav.svelte"; // Importing the Nav component
</script>

<div>
  <Nav /> <!-- Navigation bar -->
  <slot /> <!-- Every HTML content of a page -->
</div>

<!-- Setting up the whole site's bg color -->
<style>
  :global( body ) {
    @apply bg-gray-700;
  }
</style>