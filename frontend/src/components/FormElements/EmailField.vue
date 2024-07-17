<template>
  <div class="form__group" :class="{ fill: email.length > 0 }">
    <input
      class="form__input_stylus"
      :id="id"
      v-model.trim="email"
      name="email"
      :class="{ invalid: (v.$dirty && !v.required) || (v.$dirty && !v.email) || (/\s/.test(email)) }"
      @change="v.$touch()"
      autocomplete="email"
    />

    <label class="form__label_stylus" :for="id">{{ placeholder }}</label>

    <span class="form__error" v-if="v.$dirty && !v.required"> {{ translations.enterEmail }} </span>

    <span class="form__error" v-else-if="v.$dirty && !v.email"> {{ translations.correctEmail }} </span>
  </div>
</template>

<script>
import translations from '@/utils/lang.js';

export default {
  name: 'EmailField',
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
    placeholder: {
      type: String,
      default: 'E-mail',
    },
  },
  computed: {
    email: {
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
};
</script>
