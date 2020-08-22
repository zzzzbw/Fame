<template>
  <div class="error">
    <h1>{{ state }}</h1>
    <h3>{{ message }}</h3>
  </div>
</template>

<script>
export default {
  data: function () {
    return {
      state: '',
      message: '',
    }
  },
  mounted() {
    this.state = this.$route.params.state || 404
    this.message = this.$route.params.message || 'Page Not found'
  },
}
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
