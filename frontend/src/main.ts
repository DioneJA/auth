import { createApp } from 'vue';
import { Quasar } from 'quasar';
import router from './router';
import App from './App.vue';
import '@quasar/extras/material-icons/material-icons.css';
import { useAuthStore } from './stores/auth';
import { pinia } from './stores';

const app = createApp(App);
app.use(Quasar, { plugins: {} });
app.use(pinia);
app.use(router);
useAuthStore().loadFromStorage();
app.mount('#q-app');
