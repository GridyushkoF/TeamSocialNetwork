import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

import global from './global';
import auth from './auth';
import profile from './profile';
import users from './users';

export default new Vuex.Store({
  namespaced: true,
  modules: {
    global,
    auth,
    profile,
    users,
  },
  state: {
    captchaCode: Math.floor(Math.random() * 10000)
      .toString()
      .padStart(4, '0'),
    currentUser: null,
  },
  getters: {
    getCode: (s) => s.captchaCode,
    getUser: (s) => s.currentUser,
  },
  actions: {
    loadUser(contex, payload) {
      contex.commit('loadUser', payload);
    },
  },
  mutations: {
    loadUser(state, payload) {
      state.currentUser = payload;
    },
  },
  strict: process.env.NODE_ENV !== 'production',
});
