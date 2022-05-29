<template>
  <Editor :value="contentVal" :plugins="plugins" @change="handleChange" />
</template>

<script setup lang="ts">
  import { ref } from 'vue'
  import { Editor } from '@bytemd/vue-next'
  import gfm from '@bytemd/plugin-gfm'
  import highlight from '@bytemd/plugin-highlight'
  import 'bytemd/dist/index.css'

  import 'highlight.js/styles/vs.css'
  import 'github-markdown-css' // placed after highlight styles to override `code` padding

  const props = defineProps({
    contentVal: {
      type: String,
      default: ''
    }
  })

  const content = ref(props.contentVal)

  const plugins = [gfm(), highlight()]

  const handleChange = (v: string) => {
    content.value = v
  }
</script>

<style>
  .bytemd {
    height: calc(100vh - 240px);
  }
  .medium-zoom-overlay {
    z-index: 100;
  }
  .medium-zoom-image--opened {
    z-index: 101;
  }
</style>
