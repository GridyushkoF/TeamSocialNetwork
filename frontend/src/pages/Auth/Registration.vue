<template>
  <div class="registration">
    <span class="registration__steps">{{ currentStep }}</span>
    <form class="registration__form" @submit.prevent="submitHandler">
      <div class="form__block" v-if="step === 1">
        <h4 class="form__subtitle">{{ translations.createAccTitle }}</h4>

        <email-field
          id="register-email"
          v-model="email"
          :v="$v.email"
          :class="{ checked: $v.email.required && $v.email.email }"
        />

        <password-field
          id="register-password"
          v-model="password1"
          :v="$v.password1"
          info
          registration
          :class="{
            checked: $v.password1.required && $v.password2.sameAsPassword && $v.password1.minLength,
          }"
        />

        <password-repeat-field
          id="register-repeat-password"
          v-model="password2"
          :v="$v.password2"
          :class="{
            checked: $v.password1.required && $v.password2.sameAsPassword && $v.password2.minLength,
          }"
        />

        <button class="form__block-nextstep" :disabled="isStep1Disabled" @click.prevent="nextStep">{{ translations.createAccStepBtn }}</button>
      </div>
      <div v-if="step === 2">
        <div class="form__block">
          <h4 class="form__subtitle">{{ translations.createAccPersonalTitle }}</h4>
          <name-field id="register-firstName" v-model="firstName" :v="$v.firstName" :label="translations.createAccNameField1" />
          <name-field id="register-lastName" v-model="lastName" :v="$v.lastName" :label="translations.createAccNameField2" />
        </div>
        <div class="form__block">
          <h4 class="form__captcha-desc">{{ translations.createAccCaptchaDescr }}</h4>
          <div class="img_captcha">
            <img :src="imgCode" :alt="'image'" />
            <button class="btn__update" @click.prevent="updateCatcha">{{ translations.createAccCaptchaRefresh }}</button>
          </div>
          <code-field
            id="register-code"
            v-model="captchaCode"
            :v="$v.captchaCode"
            :class="{ checked: $v.captchaCode.required && $v.captchaCode.isCode }"
          />
          <confirm-field id="register-confirm" v-model="confirm" :v="$v.confirm" />
        </div>
        <div class="registration__action">
          <button
            type="submit"
            class="form-layout__btn btn__login"
          >
            {{ translations.createAccBtnReg }}
          </button>
          <button class="btn__back" @click.prevent="prevStep">{{ translations.createAccBtnBack }}</button>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import { required, email, minLength, sameAs } from 'vuelidate/lib/validators';
import PasswordField from '@/components/FormElements/PasswordField';
import PasswordRepeatField from '@/components/FormElements/PasswordRepeatField';
import EmailField from '@/components/FormElements/EmailField';
import NameField from '@/components/FormElements/NameField';
import CodeField from '@/components/FormElements/CodeField';
import ConfirmField from '@/components/FormElements/ConfirmField';
import translations from '@/utils/lang.js';

export default {
  name: 'Registration',
  components: {
    PasswordField,
    EmailField,
    NameField,
    CodeField,
    ConfirmField,
    PasswordRepeatField,
  },

  data: () => ({
    step: 1,
    totalSteps: 2,
    email: '',
    password1: '',
    password2: '',
    firstName: '',
    lastName: '',
    imgCode: '',
    captchaCode: '',
    captchaSecret: '',
    confirm: false,
    isCode: false,
  }),

  computed: {
    ...mapGetters('auth/captcha', ['getCaptcha']),

    isStep1Disabled() {
      return !this.email || !this.password1 || !this.password2;
    },

    currentStep() {
      return `${this.step}/${this.totalSteps}`;
    },

    translations() {
      const lang = this.$store.state.auth.languages.language.name;
      if (lang === 'Русский') {
        return translations.rus;
      } else {
        return translations.eng;
      }
    },

  },

  watch: {
    captchaCode() {
      if (this.isCode === false) {
        this.isCode = true;
      }
    },
  },

  beforeMount() {
    this.updateCatcha();
  },

  mounted() {
    // this.code = this.getCode;
  },

  methods: {
    ...mapActions('auth/api', ['register']),
    ...mapActions('auth/captcha', ['fetchCaptcha']),

    nextStep() {
      this.step++;
    },

    prevStep() {
      this.step--;
    },

    updateCatcha() {
      this.fetchCaptcha().then(() => {
        this.imgCode = this.getCaptcha.imgCode;
        this.captchaSecret = this.getCaptcha.secret;
      });
    },

    submitHandler() {
      if (this.$v.$invalid) {
        this.$v.$touch();
        return;
      }
      const { email, password1, password2, firstName, lastName, captchaCode, captchaSecret } = this;
      this.register({
        email,
        password1,
        password2,
        firstName,
        lastName,
        captchaCode,
        captchaSecret,
      })
        .then(() => {
          this.$router.push({ name: 'RegisterSuccess' });
        })
        .catch((error) => {
          const errorMessage = error.response.data.error_description[0] || '';
          if (errorMessage === 'Неверный код авторизации') {
            this.isCode = false;
          }
        });
    },
  },

  validations: {
    confirm: { sameAs: sameAs(() => true) },
    email: { required, email },
    password1: { required, minLength: minLength(8) },
    password2: {
      required,
      minLength: minLength(8),
      sameAsPassword: sameAs('password1'),
    },
    firstName: { required, minLength: minLength(3) },
    lastName: { required, minLength: minLength(3) },
    captchaCode: {
      required,
      isCode() {
        return this.isCode;
      },
    },
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'

.btn__back
  margin-left 15px
  background-color transparent
  color rgba(255, 255, 255, 0.5)
  font-size font-size-small
  transition all .2s ease-in-out
  &:hover
    color ui-cl-color-white-theme

.form__captcha-desc
  font-weight font-weight-light
  color ui-cl-color-white-theme
  font-size font-size-small-medium
  margin-bottom 15px

.form__block+.form__block
  margin-top 30px

.form__block-nextstep
  background-color ui-cl-color-eucalypt
  border-radius border-super-small
  color ui-cl-color-white-theme
  padding 13px
  transition all .2s ease-in-out
  margin-top 30px
  &:disabled
    background-color ui-cl-color-light-grey
    &:hover
      background-color ui-cl-color-light-grey
  @media (any-hover: hover)
    &:hover
      background-color ui-cl-color-full-black

.registration
  position relative
  min-height 100%
  display flex
  flex-direction column
  justify-content center

  &__steps
    color #afadde
    position absolute
    top 0
    right 0

  .form__block
    margin-bottom 0

  .form__group+.form__group
    margin-top 30px

  .form__group:after
    right 14px
    bottom 18px

.registration__action
  margin-top 40px

  @media (max-width breakpoint-xxl)
    margin-top 20px

.img_captcha
  display flex
  gap 20px
  align-items center
  margin-bottom 6px

.btn__update
  background-color ui-cl-color-eucalypt
  border-radius border-super-small
  font-size font-size-super-medium-small
  color ui-cl-color-white-theme
  padding 5px
  transition all .2s ease-in-out
  &:hover
    background-color ui-cl-color-full-black

.last-block_reg
  margin-top 15px !important
</style>
