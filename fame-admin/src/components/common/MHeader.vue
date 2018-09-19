<template>
  <div class="header">
    <div class="collapse">
      <a @click="collapse"><span class="icon-th-list"></span></a>
    </div>
    <h3 class="title">Fame</h3>
    <ul class="header-right">
      <li>
        <a @click="logout">
          <span class="icon-sign-in"></span>
          <span>Log out</span>
        </a>
      </li>
    </ul>
  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    methods: {
      collapse: function () {
        this.$root.$emit('collapse')
      },
      logout () {
        this.$api.auth.logout().then(data => {
          if (data.success) {
            this.$message({
              type: 'success',
              message: '登出成功!'
            })
            this.$router.push('/admin/login')
          } else {
            this.$message({
              type: 'error',
              message: data.msg || '登出失败!'
            })
            this.$router.push('/admin/login')
          }
        })
      }
    }
  }
</script>

<style scoped>

  .header {
    width: 100%;
    height: 60px;
    display: inline-block;
    background-color: #fff;
    line-height: 60px;
    text-align: center;
    box-shadow: 0 2px 3px hsla(0, 0%, 7%, .1), 0 0 0 1px hsla(0, 0%, 7%, .1);
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

  .header-right {
    list-style: none;
    float: right;
    margin: 0 15px 0 0;
    color: #7f8c8d;
  }

  .header-right a {
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
