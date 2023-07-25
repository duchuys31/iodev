<template>
    <div class="container pa-6">
      <header class="jumbotron">
        <h3>
          <strong>{{currentUser.username}}</strong> Hồ sơ
        </h3>
      </header>
      <p>
        <strong>Token:</strong>
        {{currentUser.accessToken.substring(0, 20)}} ... {{currentUser.accessToken.substr(currentUser.accessToken.length - 20)}}
      </p>
      <p>
        <strong> Mã cá nhân:</strong>
        {{currentUser.id}}
      </p>
      <p>
        <strong>Thư điện tử:</strong>
        {{currentUser.email}}
      </p>
      <strong>Vai trò:</strong>
      <ul>
        <li v-for="(role,index) in currentUser.vaiTros" :key="index">{{role}}</li>
      </ul>
    </div>
  </template>
  
  <script>
  export default {
    name: 'Profile',
    computed: {
      currentUser() {
        return this.$store.state.auth.user;
      }
    },
    mounted() {
      if (!this.currentUser) {
        this.$router.push('/dangnhap');
      }
    }
  };
  </script>