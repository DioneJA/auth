<template>
  <q-page class="q-pa-md">
    <div class="row q-col-gutter-lg">
      <div class="col-12 col-md-4">
        <q-card class="inv-card">
          <q-card-section>
            <div class="text-h6">Novo tipo</div>
          </q-card-section>
          <q-card-section>
            <q-form class="q-gutter-sm" @submit.prevent="onSubmit">
              <q-input v-model="name" label="Nome" filled required />
              <q-select v-model="kind" :options="kinds" label="Tipo" filled />
              <q-btn label="Salvar" color="primary" type="submit" class="full-width" />
            </q-form>
            <q-banner v-if="error" class="bg-negative text-white q-mt-md">{{ error }}</q-banner>
          </q-card-section>
        </q-card>
      </div>
      <div class="col-12 col-md-8">
        <div class="text-h6 inv-section-title q-mb-sm">Tipos cadastrados</div>
        <q-list bordered>
          <q-item v-for="type in finance.types" :key="type.id">
            <q-item-section>{{ type.name }}</q-item-section>
            <q-item-section side>{{ type.kind }}</q-item-section>
          </q-item>
        </q-list>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useFinanceStore } from '../stores/finance';

const finance = useFinanceStore();
const name = ref('');
const kind = ref<'INCOME' | 'EXPENSE'>('INCOME');
const error = ref('');
const kinds = ['INCOME', 'EXPENSE'];

const onSubmit = async () => {
  error.value = '';
  try {
    await finance.createType(name.value, kind.value);
    name.value = '';
  } catch (err: any) {
    error.value = err?.response?.data?.message || 'Erro ao criar tipo';
  }
};

onMounted(() => {
  finance.loadTypes();
});
</script>
