import { createApp } from 'vue'
import { createPinia } from 'pinia'
import 'reflect-metadata' // tsyringe 로 추가 설정

import App from './App.vue'
import router from './router'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import 'normalize.css'

const app = createApp(App)
const pinia = createPinia()

app.use(router)
app.use(pinia)
app.use(ElementPlus)

app.mount('#app')
