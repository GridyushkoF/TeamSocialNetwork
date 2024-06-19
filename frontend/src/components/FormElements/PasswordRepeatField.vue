<template>
  <div class="form__group" :class="{ fill: password.length > 0 }">
    <label :for="id" class="form__label_stylus">{{ translations.repeatPassword }}</label>

    <input
      type="password"
      class="form__input_stylus"
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
    />

    <span class="form__error" v-if="v.$dirty && !v.sameAsPassword">{{ translations.matchPassword }}</span>

    <span class="form__error" v-if="v.$dirty && !v.minLength">
      {{ translations.validatePass1 }} {{ v.$params.minLength.min }} {{ translations.validatePass2 }}
      {{ password.length }}
    </span>
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
};
</script>
