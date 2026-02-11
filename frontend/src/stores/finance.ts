import { defineStore } from 'pinia';
import { api } from '../boot/axios';

export interface MovementType {
  id: number;
  name: string;
  kind: 'INCOME' | 'EXPENSE';
}

export interface Transaction {
  id: number;
  typeId: number;
  typeName: string;
  kind: string;
  amount: number;
  description?: string;
  occurredAt: string;
}

export interface Goal {
  id: number;
  name: string;
  targetAmount: number;
  targetDate: string;
  progressAmount: number;
  progressPercent: number;
}

export interface Dashboard {
  totalIncome: number;
  totalExpense: number;
  balance: number;
  goals: Goal[];
}

export const useFinanceStore = defineStore('finance', {
  state: () => ({
    types: [] as MovementType[],
    transactions: [] as Transaction[],
    goals: [] as Goal[],
    dashboard: null as Dashboard | null
  }),
  actions: {
    async loadTypes() {
      const { data } = await api.get('/types');
      this.types = data;
    },
    async createType(name: string, kind: 'INCOME' | 'EXPENSE') {
      const { data } = await api.post('/types', { name, kind });
      this.types.push(data);
    },
    async loadTransactions(month?: string) {
      const { data } = await api.get('/transactions', { params: { month } });
      this.transactions = data;
    },
    async createTransaction(payload: any) {
      await api.post('/transactions', payload);
    },
    async loadGoals() {
      const { data } = await api.get('/goals');
      this.goals = data;
    },
    async createGoal(payload: any) {
      const { data } = await api.post('/goals', payload);
      this.goals.unshift(data);
    },
    async loadDashboard(month?: string) {
      const { data } = await api.get('/dashboard', { params: { month } });
      this.dashboard = data;
    }
  }
});
