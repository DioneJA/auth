import axios from 'axios';
import { boot } from 'quasar/wrappers';
import { useAuthStore } from '../stores/auth';

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080'
});

api.interceptors.request.use((config) => {
  const auth = useAuthStore();
  if (auth.accessToken) {
    config.headers.Authorization = `Bearer ${auth.accessToken}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const auth = useAuthStore();
    if (error.response?.status === 401 && auth.refreshToken) {
      try {
        await auth.refresh();
        error.config.headers.Authorization = `Bearer ${auth.accessToken}`;
        return api.request(error.config);
      } catch (refreshError) {
        auth.logout();
      }
    }
    return Promise.reject(error);
  }
);

export default boot(({ app }) => {
  app.config.globalProperties.$api = api;
});
