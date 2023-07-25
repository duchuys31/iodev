<template>
  <v-app id="inspire">
    <v-navigation-drawer
      v-model="drawer"
      v-if="loggedIn"
      app
      >
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="text-h6">
            Quản trị hệ thống
          </v-list-item-title>
          <v-list-item-subtitle>
            Hội tin học Việt Nam
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-divider></v-divider>

      <v-list
        dense
        nav
      >
        <v-list-item
          v-for="item in menuFilters"
          :key="item.title"
          :to="item.to"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content v-if="item.tag == 'logOut'" @click="logOut()">
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>

          <v-list-item-content v-if="item.tag != 'logOut'">
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar app v-if="loggedIn">
      <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>

      <v-toolbar-title>IODEV System</v-toolbar-title>
    </v-app-bar>

    <v-main>
      <router-view></router-view>
    </v-main>
  </v-app>
</template>
<script>
export default {
    name: 'App',
    components: {
    },
    data: () => ({
      drawer: null,
      items: [
        { title: 'Cuộc thi', icon: 'mdi-view-dashboard', to:'/cuocthi', requiredLoggedIn: true },
        { title: 'Khối thi', icon: 'mdi-image', to:'/khoithi', requiredLoggedIn: true },
        { title: 'Tài khoản', icon: 'mdi-account', to:'/taikhoan', requiredLoggedIn: true },
        { title: 'Đăng nhập', icon: 'mdi-login', to:'/dangnhap', requiredLoggedIn: false },
        { title: 'Đăng xuất', icon: 'mdi-logout', to:'/dangnhap', requiredLoggedIn: true, tag: 'logOut' },
        { title: 'Giới thiệu', icon: 'mdi-help-box', to:'/gioithieu', requiredLoggedIn: true },
        { title: 'Thí sinh', icon: 'mdi-account-multiple', to:'/thisinh', requiredLoggedIn: true },
        { title: 'Đội thi', icon: 'mdi-account-group', to: '/doithi', requiredLoggedIn: true },
        { title: 'Tổ chức', icon: 'mdi-bank', to: '/tochuc', requiredLoggedIn: true },
        { title: 'Profile', icon: 'mdi-clipboard-list', to: '/profile', requiredLoggedIn: true}
      ],
    }),
    computed: {
      menuFilters() {
        return this.items.filter(item => (item.requiredLoggedIn == true && this.loggedIn) || (item.requiredLoggedIn == false && !this.loggedIn))
      },
      loggedIn() {
        return this.$store.state.auth.status.loggedIn;
      }
    },
    methods: {
      logOut() {
        this.$store.dispatch('auth/logout');
        this.$router.push('/dangnhap');
      }
    },
    created () {
      document.title = "Quản trị IO";
    }
};
</script>