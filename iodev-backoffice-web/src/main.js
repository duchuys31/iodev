import Vue from 'vue';
import App from './App.vue';
import { router } from './router';
import store from './store';
import VeeValidate from 'vee-validate';
import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import "@mdi/font/css/materialdesignicons.css";
import 'material-design-icons-iconfont/dist/material-design-icons.css'
// import axios from "axios";

import
{
  faHome,
  faUser,
  faUserPlus,
  faSignInAlt,
  faSignOutAlt
} from '@fortawesome/free-solid-svg-icons';

import vuetify from './plugins/vuetify'

// axios.defaults.baseURL = "http://localhost:8081/api"

library.add( faHome, faUser, faUserPlus, faSignInAlt, faSignOutAlt );

Vue.config.productionTip = false;

Vue.use( VeeValidate );
Vue.component( 'font-awesome-icon', FontAwesomeIcon );

new Vue( {
  router,
  store,
  vuetify,
  render: h => h( App )
} ).$mount( '#app' );