import { createApp } from 'vue';
import { Quasar } from 'quasar';
import { createPinia } from 'pinia';
import router from './router';
import App from './App.vue';
import './app.css';
import '@quasar/extras/material-icons/material-icons.css';
import { useAuthStore } from './stores/auth';

const app = createApp(App);
app.use(Quasar, { plugins: {} });
app.use(createPinia());
app.use(router);
useAuthStore().loadFromStorage();
app.mount('#q-app');
