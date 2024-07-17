<template>
  <div class="forgot">
    <h2 class="forgot__title form__title">{{ translations.recoveryPassTitle }}</h2>
    <span class="forgot__title-descr">{{ translations.recoveryPassDescription }}</span>
    <form class="forgot__form" @submit.prevent="submitHandler">
      <email-field id="forgot-email" v-model="email" :v="$v.email" />

      <div class="forgot__action">
        <button :disabled="!email" type="submit" class="form-layout__btn btn__fargot">{{ translations.recoveryPassBtn }}</button>
      </div>
    </form>
  </div>
</template>

<script>
import { required, email } from 'vuelidate/lib/validators';
import EmailField from '@/components/FormElements/EmailField';
import { mapActions, mapMutations } from 'vuex';
import translations from '@/utils/lang.js';

export default {
  name: 'Forgot',
  components: {
    EmailField,
  },

  data: () => ({
    email: '',
  }),

  validations: {
    email: { required, email },
  },

  computed: {
    translations() {
      const lang = this.$store.state.auth.languages.language.name;
      if (lang === 'Русский') {
        return translations.rus;
      } else {
        return translations.eng;
      }
    },
  },

  methods: {
    ...mapActions('profile/account', ['passwordRecovery']),
    ...mapMutations('auth/info', ['setBtn']),

    submitHandler() {
      if (this.$v.$invalid) {
        this.$v.$touch();
        return;
      }

      this.$store.dispatch('global/alert/setAlert', {
        status: 'status',
        text: 'Такого email-адреса не существует',
      });

      this.passwordRecovery({ email: this.email }).then(() => {
        const emailDomain = 'https://' + this.email.split('@').pop();
        this.setBtn({
          pageId: 'forgot-success',
          btnId: 'href',
          btnIdValue: emailDomain,
        });

        this.$router.push({ name: 'ForgotSuccess' });
      });
    },
  },

};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'
.forgot
  display flex
  justify-content center
  flex-direction column
  height 100%

.btn__fargot
  &:disabled
    background-color ui-cl-color-light-grey
    &:hover
      background-color ui-cl-color-light-grey !important
  @media (any-hover: hover)
    &:hover
      background-color ui-cl-color-full-black !important

.forgot__title
  color ui-cl-color-white-theme
  margin-bottom 5px

.forgot__title-descr
  color #afadde
  margin-bottom 30px

.forgot__action
  margin-top 30px
</style>
