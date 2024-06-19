<template>
  <div class="change-password__group" :class="{ fill: password.length > 0 }">

    <input
      class="change-password__input"
      :type="passwordFieldType"
      name="password"
      :id="id"
      v-model.trim="password"
      :class="{
        invalid: (v.$dirty && !v.required),
      }"
      @change="passwordBlur"
      placeholder="Введите текущий пароль"
      pattern="[A-Za-z0-9!@#$%^&*]+"
      autocomplete="new-password"
    />

    <span class="change-password__error" v-if="v.$dirty && !v.required">Обязательно для заполнения</span>

    <div
      class="change-password__password-icon"
      :class="{ active: password.length > 0 }"
      @click="switchVisibility"

    >
      <simple-svg :filepath="'/static/img/password-eye.svg'" />
    </div>
  </div>
</template>

<script>
import translations from '@/utils/lang.js';

export default {
  name: 'PasswordField',
  props: {
    value: {
      type: String,
      default: '',
    },
    v: {
      type: Object,
      required: true,
    },
    info: Boolean,
    registration: Boolean,
    id: {
      type: String,
      required: true,
    },
  },
  data: () => ({
    passwordFieldType: 'password',
    passwordHelperShow: true,
  }),
  computed: {
    password: {
      get() {
        return this.value;
      },
      set(value) {
        this.$emit('input', value);
      },
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
  methods: {
    switchVisibility() {
      this.passwordFieldType = this.passwordFieldType === 'password' ? 'text' : 'password';
    },
    passwordBlur() {
      this.passwordHelperShow = false;
      this.v.$touch();
    },
  },
};
</script>

