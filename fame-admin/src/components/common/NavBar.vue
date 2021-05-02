<template>
  <div class="nav-bar">
    <div class="collapse">
      <a @click="collapse"><span class="icon-th-list"></span></a>
    </div>
    <h3 class="title">Fame Dashboard</h3>
    <ul class="nav-bar-right">
      <li>
        <a @click="logout">
          <span class="icon-sign-in"></span>&nbsp;
          <span>退出</span>
        </a>
      </li>
    </ul>
  </div>
</template>

<script type="text/ecmascript-6">
export default {
  methods: {
    collapse: function () {
      this.$root.$emit('side-bar-collapse')
    },
    logout() {
      this.$api.auth.logout().then(data => {
        if (data.success) {
          this.$message({
            type: 'success',
            message: '登出成功!'
          })
          this.$router.push('/login')
        } else {
          this.$message({
            type: 'error',
            message: data.msg || '登出失败!'
          })
          this.$router.push('/login')
        }
      })
    }
  }
}
</script>

<style scoped>
.nav-bar {
  width: 100%;
  height: 100%;
  display: inline-block;
  background-color: #fff;
  line-height: 60px;
  text-align: center;
  box-shadow: 0 2px 3px hsla(0, 0%, 7%, 0.1), 0 0 0 1px hsla(0, 0%, 7%, 0.1);
}

.collapse {
  display: none;
  float: left;
  width: 64px;
}

.title {
  margin: 0;
  display: inline-block;
}

.nav-bar-right {
  list-style: none;
  float: right;
  margin: 0 15px 0 0;
  color: #7f8c8d;
}

.nav-bar-right a {
  color: #7f8c8d;
  cursor: pointer;
  text-decoration: none;
}

@media screen and (max-width: 600px) {
  .collapse {
    display: block;
  }
}
</style>
