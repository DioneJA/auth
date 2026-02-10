<template>
  <q-page class="q-pa-md">
    <div class="inv-hero q-mb-md">
      <div class="text-h5">Resumo do mês</div>
      <div class="text-subtitle2">{{ monthLabel }}</div>
    </div>

    <div class="row q-col-gutter-md">
      <div class="col-12 col-md-4">
        <q-card class="inv-card">
          <q-card-section>
            <div class="text-subtitle1">Receitas</div>
            <div class="text-h6 text-positive">{{ formatCurrency(dashboard?.totalIncome) }}</div>
          </q-card-section>
        </q-card>
      </div>
      <div class="col-12 col-md-4">
        <q-card class="inv-card">
          <q-card-section>
            <div class="text-subtitle1">Despesas</div>
            <div class="text-h6 text-negative">{{ formatCurrency(dashboard?.totalExpense) }}</div>
          </q-card-section>
        </q-card>
      </div>
      <div class="col-12 col-md-4">
        <q-card class="inv-card">
          <q-card-section>
            <div class="text-subtitle1">Saldo</div>
            <div class="text-h6">{{ formatCurrency(dashboard?.balance) }}</div>
          </q-card-section>
        </q-card>
      </div>
    </div>

    <div class="q-mt-lg">
      <div class="text-h6 inv-section-title q-mb-sm">Metas financeiras</div>
      <q-card v-for="goal in dashboard?.goals || []" :key="goal.id" class="inv-card q-mb-sm">
        <q-card-section>
          <div class="text-subtitle1">{{ goal.name }}</div>
          <div class="text-caption">Alvo: {{ formatCurrency(goal.targetAmount) }} até {{ goal.targetDate }}</div>
          <q-linear-progress :value="goal.progressPercent / 100" color="secondary" class="q-mt-sm" />
          <div class="text-caption q-mt-xs">
            {{ formatCurrency(goal.progressAmount) }} ({{ goal.progressPercent.toFixed(1) }}%)
          </div>
        </q-card-section>
      </q-card>
      <q-banner v-if="!dashboard" class="q-mt-md">Carregando...</q-banner>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { onMounted, computed } from 'vue';
import { useFinanceStore } from '../stores/finance';
import { initLocalDb, saveDashboardCache, getLastDashboardCache } from '../services/sqlite';

const finance = useFinanceStore();
const dashboard = computed(() => finance.dashboard);

const monthLabel = new Date().toLocaleDateString('pt-BR', { month: 'long', year: 'numeric' });

const formatCurrency = (value?: number) => {
  const v = value ?? 0;
  return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(v);
};

onMounted(async () => {
  await initLocalDb();
  try {
    await finance.loadDashboard();
    if (finance.dashboard) {
      await saveDashboardCache(JSON.stringify(finance.dashboard));
    }
  } catch (err) {
    const cached = await getLastDashboardCache();
    if (cached) {
      finance.dashboard = JSON.parse(cached);
    }
  }
});
</script>
