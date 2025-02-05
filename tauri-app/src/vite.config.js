import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    host: '127.0.0.1',
    cors: {
      origin: [ "http://172.18.0.2:8057", "http://localhost:1420" ],
      methods: "GET,POST,PUT,DELETE,PATCH,HEAD",
      optionsSuccessStatus: 204
    }
  }
})
