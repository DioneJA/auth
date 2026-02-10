import { defineStore } from 'pinia';
import api from '../boot/axios';

interface AuthState {
  accessToken: string | null;
  refreshToken: string | null;
  userName: string | null;
  familyId: number | null;
  userId: number | null;
}

const STORAGE_KEY = 'involvest.auth';

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    accessToken: null,
    refreshToken: null,
    userName: null,
    familyId: null,
    userId: null
  }),
  getters: {
    isAuthenticated: (state) => !!state.accessToken
  },
  actions: {
    loadFromStorage() {
      const raw = localStorage.getItem(STORAGE_KEY);
      if (raw) {
        const parsed = JSON.parse(raw) as AuthState;
        this.accessToken = parsed.accessToken;
        this.refreshToken = parsed.refreshToken;
        this.userName = parsed.userName;
        this.familyId = parsed.familyId;
        this.userId = parsed.userId;
      }
    },
    persist() {
      localStorage.setItem(STORAGE_KEY, JSON.stringify({
        accessToken: this.accessToken,
        refreshToken: this.refreshToken,
        userName: this.userName,
        familyId: this.familyId,
        userId: this.userId
      }));
    },
    async login(email: string, password: string) {
      const { data } = await api.post('/auth/login', { email, password });
      this.applyAuth(data);
    },
    async register(name: string, email: string, password: string, familyName: string) {
      const { data } = await api.post('/auth/register', { name, email, password, familyName });
      this.applyAuth(data);
    },
    async refresh() {
      const { data } = await api.post('/auth/refresh', { refreshToken: this.refreshToken });
      this.applyAuth(data);
    },
    applyAuth(data: any) {
      this.accessToken = data.accessToken;
      this.refreshToken = data.refreshToken;
      this.userName = data.userName;
      this.familyId = data.familyId;
      this.userId = data.userId;
      this.persist();
    },
    logout() {
      this.accessToken = null;
      this.refreshToken = null;
      this.userName = null;
      this.familyId = null;
      this.userId = null;
      localStorage.removeItem(STORAGE_KEY);
      window.location.href = '/login';
    }
  }
});
