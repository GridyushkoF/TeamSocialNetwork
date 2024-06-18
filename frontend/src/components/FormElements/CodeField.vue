<template>
  <div class="form__group" :class="{ fill: captchaCode.length > 0 }">
    <input
      type="text"
      class="form__input_stylus"
      :id="id"
      v-model="captchaCode"
      name="code"
      :class="{ invalid: (v.$dirty && !v.required) || (v.$dirty && !v.isCode) }"
      @change="v.$touch()"
      autocomplete="off"
    />

    <label :for="id" class="form__label_stylus">{{ translations.codeTitle }}</label>

    <span class="form__error" v-if="v.$dirty && !v.required">{{ translations.requiredField }}</span>

    <span class="form__error" v-else-if="v.$dirty && !v.isCode">{{ translations.codeNoMatch }}</span>
  </div>
</template>

<script>
import translations from '@/utils/lang.js';
export default {
  name: 'CodeField',
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
    captchaCode: {
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
