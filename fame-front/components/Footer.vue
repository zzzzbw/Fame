<template>
  <div class="footer">
    <p class="copyright">
      © 2017 | Proudly powered by
      <a href="https://github.com/zzzzbw/Fame" target="_blank">Fame</a>
      <br />
      <a href="http://www.miitbeian.gov.cn" target="_blank"> {{ icp }} </a>
    </p>
  </div>
</template>

<script type="text/ecmascript-6">
export default {
  data: function () {
    return {
      icp: process.env.icp
    }
  }
}
</script>

<style scoped>
.footer {
  padding: 1em 0;
  margin: 0 auto;
  max-width: 850px;
  border-top: 1px solid #e5e5e5;
  text-align: center;
  font-size: 0.9em;
}

a {
  color: #5764c6;
  text-decoration: none;
}
</style>
