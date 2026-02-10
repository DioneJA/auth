<template>
  <q-page class="q-pa-md flex flex-center">
    <div class="inv-card q-pa-lg" style="max-width: 520px; width: 100%;">
      <div class="text-h5 inv-section-title q-mb-md">Criar conta</div>
      <q-form @submit.prevent="onSubmit" class="q-gutter-md">
        <q-input v-model="name" label="Nome" filled required />
        <q-input v-model="familyName" label="Família" filled required />
        <q-input v-model="email" label="Email" type="email" filled required />
        <q-input v-model="password" label="Senha" type="password" filled required />
        <q-btn label="Cadastrar" color="primary" type="submit" class="full-width" />
        <q-btn flat label="Já tenho conta" @click="goLogin" class="full-width" />
      </q-form>
      <q-banner v-if="error" class="bg-negative text-white q-mt-md">{{ error }}</q-banner>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';

const name = ref('');
const familyName = ref('');
const email = ref('');
const password = ref('');
const error = ref('');

const auth = useAuthStore();
const router = useRouter();

const onSubmit = async () => {
  error.value = '';
  try {
    await auth.register(name.value, email.value, password.value, familyName.value);
    router.push('/');
  } catch (err: any) {
    error.value = err?.response?.data?.message || 'Falha no cadastro';
  }
};

const goLogin = () => router.push('/login');
</script>
