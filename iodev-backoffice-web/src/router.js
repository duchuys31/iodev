import Vue from 'vue';
import Router from 'vue-router';
import Home from './views/Home.vue';
import Register from './views/authenticate/Register.vue';
import CuocThi from './components/cms/CuocThi.vue'
import KhoiThi from './components/cms/KhoiThi.vue'
import TaiKhoan from './components/auth/TaiKhoan.vue'
import DangNhap from './components/auth/DangNhap.vue'
import ThiSinh from './components/cms/ThiSinh.vue'
import DoiThi from './components/cms/DoiThi.vue'
import ToChuc from './components/humanresources/ToChuc.vue'

Vue.use(Router);

export const router = new Router({
  // mode: 'history',
  routes: [
    {
      path: '/cuocthi',
      name: 'CuocThi',
      component: CuocThi
    },
    {
      path: '/khoithi',
      name: 'KhoiThi',
      component: KhoiThi
    },
    {
      path: '/taikhoan',
      name: 'TaiKhoan',
      component: TaiKhoan
    },
    {
      path: '/dangnhap',
      name: 'DangNhap',
      component: DangNhap
    },
    {
      path: '/',
      component: Home
    },
    {
      path: '/register',
      component: Register
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('./views/authenticate/Profile.vue')
    },
    {
      path: '/thisinh',
      name: 'thisinh',
      component: ThiSinh
    },
    {
      path: '/tochuc',
      name: 'toChuc',
      component: ToChuc
    },
    {
      path: '/doithi',
      name: 'doithi',
      component: DoiThi
    }
  ]
});