import path from 'path'
import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'

import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

const pathSrc = path.resolve(__dirname, 'src')

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const root = process.cwd()
  const env = loadEnv(mode, root)

  console.log(env)
  return {
    base: env.VITE_BASE_PATH,
    server: {
      host: '0.0.0.0',
      port: 3001
    },
    resolve: {
      alias: {
        '~/': `${pathSrc}/`
      }
    },
    css: {
      preprocessorOptions: {
        scss: {
          additionalData: '@use "~/styles/element/index.scss" as *;'
        }
      }
    },
    plugins: [
      vue(),
      Components({
        resolvers: [
          ElementPlusResolver({
            importStyle: 'sass'
          })
        ],
        dts: path.resolve(pathSrc, 'components.d.ts')
      })
    ]
  }
})
