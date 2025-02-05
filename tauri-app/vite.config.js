import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

const host = process.env.TAURI_DEV_HOST;

// https://vitejs.dev/config/
export default defineConfig(async () => ({
  plugins: [vue()],

  // Vite options tailored for Tauri development and only applied in `tauri dev` or `tauri build`
  //
  // 1. prevent vite from obscuring rust errors
  clearScreen: false,
  // 2. tauri expects a fixed port, fail if that port is not available
  server: {
    port: 1420,
    strictPort: true,
    host: host || false,
    hmr: host
      ? {
          protocol: "ws",
          host,
          port: 1421,
          cors: {
            origin: [ "http://172.18.0.2:8057", "http://localhost:1420" ],
            methods: "GET,POST,PUT,DELETE,PATCH,HEAD",
            optionsSuccessStatus: 204
          }
        }
      : undefined,
    watch: {
      // 3. tell vite to ignore watching `src-tauri`
      ignored: ["**/src-tauri/**"],
    },
    cors: {
      origin: [ "http://172.18.0.2:8057", "http://localhost:1420" ],
      methods: "GET,POST,PUT,DELETE,PATCH,HEAD",
      optionsSuccessStatus: 204
    }
  },
}));
