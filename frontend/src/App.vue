<template>
  <div id="app">
    <component :is="layout" v-if="$route.meta.layout && !isDeleted">
      <router-view />
    </component>
    <div class="deleted-account" v-else-if="isDeleted">
      <delete-account :info="info" />
    </div>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import DeleteAccount from './layouts/DeleteAccount.vue';

export default {
  name: 'App',
  components: {
    FormLayout: () => import('@/layouts/FormLayout'),
    MainLayout: () => import('@/layouts/MainLayout'),
    EmptyLayout: () => import('@/layouts/EmptyLayout'),
    DeleteAccount
  },
  computed: {
    ...mapGetters('global/alert', ['getState']),
    ...mapState('profile/info', ['info']),

    alert() {
      return this.$store.state.global.alert;
    },
    layout() {
      return this.$route.meta.layout + '-layout';
    },

    isDeleted() {
      return this.info && this.info.isDeleted;
    },
  },

  watch: {
    'alert.show'(value) {
      if (!value) {
        return;
      }

      if (this.$store.state.global.alert.status === 'success') {
        this.$vToastify.success(this.$store.state.global.alert.text);
        return;
      }

      if (this.$store.state.global.alert.status === 'response') {
        this.$vToastify.success(this.$store.state.global.alert.text, 'Ответ запроса');
        return;
      }

      if (this.$store.state.global.alert.status === 'action') {
        this.$vToastify.success(this.$store.state.global.alert.text, 'Действие невозможно');
        return;
      }

      if (this.$store.state.global.alert.status === 'validation') {
        this.$vToastify.success(this.$store.state.global.alert.text, 'Неверные параметры');
        return;
      }

      this.$vToastify.error(this.$store.state.global.alert.text, 'Ошибка');
      return;
    },
  },

  async mounted() {
    if (!this.getInfo) {
      await this.apiInfo();
    }
  },

  methods: {
    ...mapGetters('profile/info', ['getInfo']),
  }

};
</script>
<style lang="stylus">

@import './assets/stylus/main.styl';
@import './assets/stylus/base/vars.styl'

  .vt-notification-container {
    display block !important
  }

.v-snack__wrapper
  &.success
    background-color ui-cl-color-eucalypt

  &.error
    background-color ui-cl-color-wild-watermelon

.deleted-account
  display flex
  align-items center
  justify-content center
  width 100%
  height 100vh
</style>

<style lang="css">
@import '../public/css/style.min.css';

::-webkit-scrollbar {
  width: 5px;
}

::-webkit-scrollbar-track {
  background-color: darkgrey;
}

::-webkit-scrollbar-thumb {
  box-shadow: inset 0 0 6px #363636;
  border-radius: 8px;
}
</style>
