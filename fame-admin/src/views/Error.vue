<template>
  <div class="error">
    <h1>{{ state }}</h1>
    <h3>{{ message }}</h3>
  </div>
</template>

<script setup lang="ts">
  import { ref } from 'vue'
  import { useRoute } from 'vue-router'

  const route = useRoute()
  const state = ref()
  const message = ref()

  state.value = route.params.state || 404
  message.value = route.params.message || 'Page Not found'
</script>

<style scoped>
  .error {
    margin-top: 180px;
  }

  h1 {
    font-size: 64px;
    text-align: center;
  }

  h3 {
    font-weight: 600;
    text-align: center;
  }
</style>
