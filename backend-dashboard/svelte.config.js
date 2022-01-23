import preprocess from "svelte-preprocess";
import { resolve } from 'path';

/** @type {import('@sveltejs/kit').Config} */
const config = {
  kit: {
    vite: {
      resolve: {
        alias: {
          $components: resolve( 'src/components' ),
          $stores: resolve( 'src/stores' ),
          $icons: resolve( 'src/icons' )
        }
      }
    },

    // hydrate the <div id="svelte"> element in src/app.html
    target: "#svelte",
  },

  preprocess: [
    preprocess({
      postcss: true,
    }),
  ],
};

export default config;
