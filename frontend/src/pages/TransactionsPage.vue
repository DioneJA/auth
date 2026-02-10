<template>
  <q-page class="q-pa-md">
    <div class="row q-col-gutter-lg">
      <div class="col-12 col-md-4">
        <q-card class="inv-card">
          <q-card-section>
            <div class="text-h6">Nova movimentação</div>
          </q-card-section>
          <q-card-section>
            <q-form class="q-gutter-sm" @submit.prevent="onSubmit">
              <q-select
                v-model="form.typeId"
                :options="typeOptions"
                label="Tipo"
                emit-value
                map-options
                filled
                required
              />
              <q-input v-model.number="form.amount" label="Valor" type="number" filled required />
              <q-input v-model="form.description" label="Descrição" filled />
              <q-input v-model="form.occurredAt" type="date" label="Data" filled required />
              <q-btn label="Salvar" color="primary" type="submit" class="full-width" />
            </q-form>
            <q-banner v-if="error" class="bg-negative text-white q-mt-md">{{ error }}</q-banner>
          </q-card-section>
        </q-card>
      </div>
      <div class="col-12 col-md-8">
        <div class="text-h6 inv-section-title q-mb-sm">Movimentações do mês</div>
        <q-list bordered class="rounded-borders">
          <q-item v-for="item in finance.transactions" :key="item.id">
            <q-item-section>
              <div class="text-subtitle2">{{ item.typeName }} · {{ item.occurredAt }}</div>
              <div class="text-caption">{{ item.description || '-' }}</div>
            </q-item-section>
            <q-item-section side>
              <div :class="item.kind === 'INCOME' ? 'text-positive' : 'text-negative'">
                {{ formatCurrency(item.amount) }}
              </div>
            </q-item-section>
          </q-item>
        </q-list>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { onMounted, computed, ref } from 'vue';
import { useFinanceStore } from '../stores/finance';

const finance = useFinanceStore();
const error = ref('');

const form = ref({
  typeId: null as number | null,
  amount: 0,
  description: '',
  occurredAt: new Date().toISOString().slice(0, 10)
});

const typeOptions = computed(() =>
  finance.types.map((type) => ({ label: `${type.name} (${type.kind})`, value: type.id }))
);

const formatCurrency = (value: number) =>
  new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);

const onSubmit = async () => {
  error.value = '';
  try {
    await finance.createTransaction({
      typeId: form.value.typeId,
      amount: form.value.amount,
      description: form.value.description,
      occurredAt: form.value.occurredAt
    });
    await finance.loadTransactions();
  } catch (err: any) {
    error.value = err?.response?.data?.message || 'Erro ao salvar movimentação';
  }
};

onMounted(async () => {
  await finance.loadTypes();
  await finance.loadTransactions();
});
</script>
