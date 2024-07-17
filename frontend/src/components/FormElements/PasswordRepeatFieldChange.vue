<template>
  <div class="change-password__group" :class="{ fill: password.length > 0 }">

    <input
      class="change-password__input"
      :type="passwordFieldType"
      :id="id"
      v-model.trim="password"
      name="password"
      :class="{
        invalid:
          (v.$dirty && !v.required) ||
          (v.$dirty && !v.minLength) ||
          (v.$dirty && !v.sameAsPassword),
      }"
      @change="v.$touch()"
      placeholder="Повторите пароль"
    />
    <span class="change-password__error" v-if="v.$dirty && !v.required">Обязательно для заполнения</span>

    <span class="change-password__error" v-if="v.$dirty && !v.sameAsPassword && v.required">{{ translations.matchPassword }}</span>

    <span class="change-password__error" v-if="v.$dirty && !v.minLength && v.sameAsPassword">
      {{ translations.validatePass1 }} {{ v.$params.minLength.min }} {{ translations.validatePass2 }}
      {{ password.length }}
    </span>

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
  name: 'PasswordRepeatField',
  props: {
    value: {
      type: String,
      default: '',
    },
    v: {
      type: Object,
      required: true,
    },
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
    }
  }
};
</script>

