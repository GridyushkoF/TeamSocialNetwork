<template>
  <div class="settings-security">
    <div class="settings-security__block">
      <div class="settings-security__mail">
        <h3 class="settings-security__title">Сменить E-mail:</h3>

        <input
          class="settings-security__value"
          v-model="changeEmail"
          autocomplete="off"
        />

        <button
          class="settings-security__btn"
          @click.prevent="openModal('email')"
        >
          {{ translations.settingBtnChange }}
        </button>
      </div>

      <h3 class="settings-security__title">Сменить пароль:</h3>
      <password-field-change-old
        id="old-password"
        v-model="passwordOld"
        :v="$v.passwordOld"
        info="info"
        registration="registration"
        :class="{
          checked: $v.passwordOld.required
        }"
      />
      <password-field
        id="change-password"
        v-model="password"
        :v="$v.password"
        info="info"
        registration="registration"
        :class="{
          checked: $v.password.required && $v.passwordTwo.sameAsPassword,
        }"
      />
      <password-repeat-field
        id="change-repeat-password"
        v-model="passwordTwo"
        :v="$v.passwordTwo"
        :class="{
          checked: $v.password.required && $v.passwordTwo.sameAsPassword,
        }"
      />
      <div class="change-password__action">
        <button type="submit" class="settings-security__btn" @click.prevent="openModal('password')">
          {{ translations.settingBtnChange }}</button>
      </div>
    </div>

    <modal v-model="modalShow">
        <p class="change-password__done" v-if="modalText"><b>{{ modalText }}</b></p>
        <div class="change-password__timer">
          <p>Вы будете перенаправлены на страницу входа через:</p>
          <div>{{ counter }}</div>
        </div>
      <!-- <template slot="actions">
        <button-hover @click.native="closeModal">{{ translations.yes }}</button-hover>
      </template> -->
    </modal>
  </div>
</template>

<script>
import PasswordField from '@/components/FormElements/PasswordFieldChange';
import PasswordRepeatField from '@/components/FormElements/PasswordRepeatFieldChange';
import PasswordFieldChangeOld from '../FormElements/PasswordFieldChangeOld.vue';
import Modal from '@/components/Modal';
import auth from '@/requests/auth';
import translations from '@/utils/lang.js';
import { mapGetters, mapActions } from 'vuex';
import { required, sameAs, minLength } from 'vuelidate/lib/validators';

export default {
  name: 'SettingsSecurity',
  components: {
    Modal,
    PasswordField,
    PasswordRepeatField,
    PasswordFieldChangeOld
   },

  data: () => ({
    modalShow: false,
    modalText: '',
    changeEmail: '',
    passwordOld: '',
    password: '',
    passwordTwo: '',
    secret: '',
    counter: 5,
    submit: true,
  }),

  computed: {
    ...mapGetters('profile/info', ['getInfo']),

    translations() {
      const lang = this.$store.state.auth.languages.language.name;
      if (lang === 'Русский') {
        return translations.rus;
      } else {
        return translations.eng;
      }
    },
  },

  mounted() {
    setTimeout(() => {
      this.changeEmail = this.getInfo?.email;
      this.password = '';
      this.passwordTwo = '';
    }, 300);

    this.secret = this.$route.params.secret || '';
  },

  validations: {
    passwordOld: {
      required
    },
    password: {
      required,
      minLength: minLength(8)
    },
    passwordTwo: {
      required,
      sameAsPassword: sameAs('password'),
      minLength: minLength(8),
    },
  },

  methods: {
    ...mapActions('auth/api', ['logout']),
    ...mapActions('profile/account', ['passwordSet']),

    closeModal() {
      this.modalShow = false;
    },

    timer() {
      const intervalId = setInterval(() => {
        this.counter -= 1;
        if (this.counter === 0) {
          clearInterval(intervalId);
          this.onLogout()
        }
      }, 1000);
    },

    onLogout() {
      this.logout().finally(() => {
        this.$router.push('/login');
      });
    },

    async openModal(id) {
      if (id === 'email') {
        await auth.requestChangeEmailLink({ email: this.changeEmail }).then(() => {
          this.modalText = `${this.translations.settingModalEmailChange} ${this.changeEmail}`;
          this.modalShow = true;
          setTimeout(() => {
            this.logout().finally(() => {
              this.$router.push('/login');
            });
          }, 3000);
        });
      }

      if (id === 'password') {
        if (this.$v.$invalid) {
          this.$v.$touch();
          return;
        };
        if (this.password === this.passwordTwo && this.submit === true) {
          await auth.requestChangePasswordLink({ oldPassword: this.passwordOld, newPassword1: this.password, newPassword2: this.passwordTwo,}).then(() => {
            this.modalText = `${this.translations.settingModalPasswordChange}`;
            this.submit = false;
            this.modalShow = true;
            this.timer();
          });
        };
      }
    },
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'


.settings-security__block
  background ui-cl-color-white-theme
  box-shadow box-shadow-main
  display flex
  flex-direction column
  width 100%
  padding 30px
  font-size font-size-downdefault
  border-radius border-big-radius

  &+&
    margin-top 20px

.settings-security__mail
  margin-bottom 30px

.settings-security__btn
  display block
  min-width 180px
  max-width 180px
  color ui-cl-color-eucalypt
  border-radius border-small
  text-align center
  background ui-cl-color-white-theme
  border 1px solid ui-cl-color-eucalypt
  font-size font-size-small-medium
  padding 10px
  @media (any-hover: hover)
    &:hover
      background ui-cl-color-eucalypt
      color ui-cl-color-white-theme

.settings-security__title
  color ui-cl-color-full-black
  margin-bottom 15px
  font-family 'Exo', Arial, sans-serif
  font-size 24px
  font-weight font-weight-bold

.form__input_stylus
  color ui-cl-color-full-black

.settings-security__value
  display block
  width 100%
  color ui-cl-color-767676
  border-radius border-small
  background ui-cl-color-white-theme
  border 1px solid ui-cl-color-ababab
  font-size font-size-small-medium
  padding 10px 15px
  margin-bottom 15px

.change-password__action
  margin-top -15px

.change-password__done
  color ui-cl-color-eucalypt

.change-password__timer
  margin 20px auto
  max-width 350px

</style>
