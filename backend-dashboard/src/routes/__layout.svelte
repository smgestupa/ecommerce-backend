<script>
  import "../app.css";
  import Nav from "$components/Nav.svelte"; // Importing the Nav component
</script>

<div>
  <Nav /> <!-- Navigation bar -->
  <slot /> <!-- Every HTML content of a page -->
</div>