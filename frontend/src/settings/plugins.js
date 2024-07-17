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
Vue.use(chat, { server: '82.202.214.42' }); // <- для стэнда, чтобы работал websocket.

// import VueSocketIO from 'vue-socket.io';
// Vue.use(chat, { server: 'localhost:8080' }); // <- для локальной разработки. При выгрузке на стэнд закомментировать.
// Vue.use(chat, { server: 'localhost:8099' });
