<template>
  <div>
    <fame-header/>
    <div class="container">
      <transition name="flow" mode="out-in">
        <nuxt/>
      </transition>
    </div>
    <fame-scroll-top/>
    <fame-footer/>
  </div>
</template>

<script>
  import FameHeader from '~/components/Header.vue'
  import FameFooter from '~/components/Footer.vue'
  import FameScrollTop from '~/components/ScrollTop.vue'

  export default {
    components: {
      FameHeader,
      FameFooter,
      FameScrollTop
    }
  }
</script>

<style>
  .container {
    padding: 2em 1em;
    margin: 30px auto 0;
    height: 100%;
    max-width: 980px;
    font-size: 16px;
    position: relative;
    animation: flow;
    animation-duration: .5s;
  }
</style>
