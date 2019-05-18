import sitemapConfig from './config/sitemap-config'
import feedConfig from './config/feed-config'
import googleAnalytics from './config/google-analytics-config'

// noinspection JSAnnotator
export default {
  /*
   ** Headers of the page
   */
  head: {
    title: 'Blog',
    titleTemplate: '%s - Fame',
    meta: [
      { charset: 'utf-8' },
      { 'http-equiv': 'cleartype', content: 'on' },
      { 'http-equiv': 'Cache-Control' },
      {
        name: 'viewport',
        content: 'width=device-width, initial-scale=1, user-scalable=no'
      },
      {
        hid: 'description',
        name: 'description',
        content: 'A nuxt blog by Fame'
      },
      {
        hid: 'keywords',
        name: 'keywords',
        content: 'vue, nuxt, java, spring-boot, maven'
      },
      { name: 'author', content: 'zzzzbw@gmail.com' },
      {
        name: 'google-site-verification',
        content: process.env.GOOGLE_SITE_VERIFICATION
      }
    ],
    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' },
      {
        rel: 'stylesheet',
        type: 'text/css',
        href: 'https://fonts.font.im/css?family=Roboto|Source+Code+Pro'
      }
    ],
    noscript: [{ innerHTML: 'This website requires JavaScript.' }]
  },
  /*
   ** Global CSS
   */
  css: [
    { src: '~assets/css/main.css' },
    { src: '~assets/css/normalize.css' },
    { src: '~assets/css/style.css' },
    'highlight.js/styles/tomorrow.css',
    { src: '~assets/css/icon.css' }
  ],
  modules: ['@nuxtjs/google-analytics', '@nuxtjs/sitemap', '@nuxtjs/feed'],
  sitemap: sitemapConfig.config,
  feed: feedConfig.config,
  googleAnalytics: googleAnalytics.config,
  plugins: [
    { src: '~plugins/highlight.js' },
    { src: '~plugins/clickoutside.js' },
    { src: '~plugins/filters.js' },
    { src: '~plugins/global.js' },
    { src: '~plugins/tools.js' }
  ],
  router: {
    linkActiveClass: 'active',
    // nuxt 的bug,scrollToTop不生效，要重写scrollBehavior方法
    scrollBehavior: function(to, from, savedPosition) {
      if (savedPosition) {
        return savedPosition
      } else {
        let position = {}
        if (to.matched.length < 2) {
          position = { x: 0, y: 0 }
        } else if (
          to.matched.some(r => r.components.default.options.scrollToTop)
        ) {
          position = { x: 0, y: 0 }
        }
        if (to.hash) {
          position = { selector: to.hash }
        }
        return position
      }
    }
  },
  /*
   ** Customize the progress bar color
   */
  loading: { color: '#5764c6' },
  /*
   ** Build configuration
   */
  build: {
    extractCSS: true,
    /*
     ** Run ESLint on save
     */
    extend(config, { isDev, isClient }) {
      if (isDev && isClient) {
        config.module.rules.push({
          enforce: 'pre',
          test: /\.(js|vue)$/,
          loader: 'eslint-loader',
          exclude: /(node_modules)/
        })
      }
    }
  },
  env: {
    // 环境变量,用于docker
    baseUrl: process.env.BASE_URL || undefined,
    proxyHost: process.env.PROXY_HOST || undefined,
    proxyPort: process.env.PROXY_PORT || undefined,
    icp: process.env.ICP || '在 nuxt.config.js 配置你的备案号'
  }
}
