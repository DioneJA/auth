<template>
  <q-layout view="lHh Lpr lff">
    <q-header elevated>
      <q-toolbar class="bg-primary text-white">
        <q-btn flat dense round icon="menu" @click="toggleLeft" class="lt-sm" />
        <q-toolbar-title>Involvest</q-toolbar-title>
        <q-btn flat dense icon="logout" @click="logout" />
      </q-toolbar>
    </q-header>

    <q-drawer v-model="leftDrawerOpen" show-if-above bordered>
      <q-list>
        <q-item clickable to="/">
          <q-item-section avatar><q-icon name="dashboard" /></q-item-section>
          <q-item-section>Dashboard</q-item-section>
        </q-item>
        <q-item clickable to="/transactions">
          <q-item-section avatar><q-icon name="list" /></q-item-section>
          <q-item-section>Movimentações</q-item-section>
        </q-item>
        <q-item clickable to="/types">
          <q-item-section avatar><q-icon name="category" /></q-item-section>
          <q-item-section>Tipos</q-item-section>
        </q-item>
        <q-item clickable to="/goals">
          <q-item-section avatar><q-icon name="flag" /></q-item-section>
          <q-item-section>Metas</q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useAuthStore } from '../stores/auth';

const leftDrawerOpen = ref(false);
const auth = useAuthStore();

const toggleLeft = () => {
  leftDrawerOpen.value = !leftDrawerOpen.value;
};

const logout = () => {
  auth.logout();
};
</script>

<style scoped>
.bg-primary {
  background: var(--inv-primary) !important;
}
</style>
