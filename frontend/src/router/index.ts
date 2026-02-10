import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '../stores/auth';
import MainLayout from '../layouts/MainLayout.vue';
import LoginPage from '../pages/LoginPage.vue';
import RegisterPage from '../pages/RegisterPage.vue';
import DashboardPage from '../pages/DashboardPage.vue';
import TransactionsPage from '../pages/TransactionsPage.vue';
import TypesPage from '../pages/TypesPage.vue';
import GoalsPage from '../pages/GoalsPage.vue';

const routes = [
  { path: '/login', component: LoginPage },
  { path: '/register', component: RegisterPage },
  {
    path: '/',
    component: MainLayout,
    meta: { requiresAuth: true },
    children: [
      { path: '', component: DashboardPage },
      { path: 'transactions', component: TransactionsPage },
      { path: 'types', component: TypesPage },
      { path: 'goals', component: GoalsPage }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to) => {
  const auth = useAuthStore();
  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return '/login';
  }
  if ((to.path === '/login' || to.path === '/register') && auth.isAuthenticated) {
    return '/';
  }
  return true;
});

export default router;
