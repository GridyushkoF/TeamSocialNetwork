import Vue from 'vue';

import Vuelidate from 'vuelidate';
Vue.use(Vuelidate);

import VueSimpleSVG from 'vue-simple-svg';
Vue.use(VueSimpleSVG);

import VTooltip from 'v-tooltip';
Vue.use(VTooltip);

import VueToastify from 'vue-toastify';
Vue.use(VueToastify);

import moment from 'moment';

const language = JSON.parse(localStorage.getItem('selectedLanguage'))
if (language) {
  if (language.name === 'Русский') {
    moment.locale('ru')
  } else {
    moment.locale('en')
  }
} else {
  moment.locale('ru')
}

import VueMoment from 'vue-moment';
Vue.use(VueMoment, {
  moment,
});

// import chat from "@/plugins/socketio";
import chat from '@/plugins/websocket';
Vue.use(chat, { server: '95.174.93.240:9090' }); // <- для стэнда, чтобы работал websocket.

// import VueSocketIO from 'vue-socket.io';
// Vue.use(chat, { server: 'localhost:9090' }); // <- для локальной разработки. При выгрузке на стэнд закомментировать.
