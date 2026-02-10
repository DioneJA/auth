<template>
  <q-page class="q-pa-md">
    <div class="row q-col-gutter-lg">
      <div class="col-12 col-md-4">
        <q-card class="inv-card">
          <q-card-section>
            <div class="text-h6">Nova meta</div>
          </q-card-section>
          <q-card-section>
            <q-form class="q-gutter-sm" @submit.prevent="onSubmit">
              <q-input v-model="name" label="Nome" filled required />
              <q-input v-model.number="targetAmount" label="Valor alvo" type="number" filled required />
              <q-input v-model="targetDate" label="Prazo" type="date" filled required />
              <q-btn label="Salvar" color="primary" type="submit" class="full-width" />
            </q-form>
            <q-banner v-if="error" class="bg-negative text-white q-mt-md">{{ error }}</q-banner>
          </q-card-section>
        </q-card>
      </div>
      <div class="col-12 col-md-8">
        <div class="text-h6 inv-section-title q-mb-sm">Metas cadastradas</div>
        <q-card v-for="goal in finance.goals" :key="goal.id" class="inv-card q-mb-sm">
          <q-card-section>
            <div class="text-subtitle1">{{ goal.name }}</div>
            <div class="text-caption">Alvo: {{ formatCurrency(goal.targetAmount) }} até {{ goal.targetDate }}</div>
            <q-linear-progress :value="goal.progressPercent / 100" color="secondary" class="q-mt-sm" />
            <div class="text-caption q-mt-xs">
              {{ formatCurrency(goal.progressAmount) }} ({{ goal.progressPercent.toFixed(1) }}%)
            </div>
          </q-card-section>
        </q-card>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useFinanceStore } from '../stores/finance';

const finance = useFinanceStore();
const name = ref('');
const targetAmount = ref(0);
const targetDate = ref(new Date().toISOString().slice(0, 10));
const error = ref('');

const formatCurrency = (value: number) =>
  new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);

const onSubmit = async () => {
  error.value = '';
  try {
    await finance.createGoal({ name: name.value, targetAmount: targetAmount.value, targetDate: targetDate.value });
    await finance.loadGoals();
    name.value = '';
    targetAmount.value = 0;
  } catch (err: any) {
    error.value = err?.response?.data?.message || 'Erro ao criar meta';
  }
};

onMounted(() => {
  finance.loadGoals();
});
</script>
