<template>
  <div class="error">
    <h1>{{ state }}</h1>
    <h3>{{ message }}</h3>
  </div>
</template>

<script lang="ts">
  import { defineComponent, ref } from 'vue'
  import { useRoute } from 'vue-router'

  export default defineComponent({
    setup() {
      const route = useRoute()
      const state = ref(404)
      const message = ref('Page Not found')

      state.value = route.params.state || 404
      message.value = route.params.message || 'Page Not found'
      return {
        state,
        message
      }
    }
  })
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
