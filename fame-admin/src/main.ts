import { createApp } from 'vue'
import { ElCollapseTransition } from 'element-plus'
import App from './App.vue'
import router from './router'

// import '~/styles/element/index.scss'
// import ElementPlus from "element-plus";
// import all element css, uncommented next line
// import 'element-plus/dist/index.css'

// or use cdn, uncomment cdn link in `index.html`

import '~/styles/index.scss'

// If you want to use ElMessage, import it.
import 'element-plus/theme-chalk/src/message.scss'
import 'element-plus/theme-chalk/src/loading.scss'

const app = createApp(App)
// app.use(ElementPlus);
app.component(ElCollapseTransition.name, ElCollapseTransition)
app.use(router).mount('#app')
