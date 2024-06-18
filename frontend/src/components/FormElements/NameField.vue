<template>
  <div class="form__group" :class="{ fill: name.length > 0 }">
    <input
      type="text"
      class="form__input_stylus"
      :id="id"
      v-model="name"
      name="name"
      :class="{ invalid: (v.$dirty && !v.required) || (v.$dirty && !v.minLength) }"
      @change="v.$touch()"
      v-pattern
    />

    <label :for="id" class="form__label_stylus">{{ label }}</label>

    <span class="form__error" v-if="v.$dirty && !v.required">{{ translations.requiredField }}</span>

    <span class="form__error" v-else-if="v.$dirty && !v.minLength">
      {{ translations.minimumNumberOfCharacters }} {{ v.minLength }}
    </span>
  </div>
</template>

<script>
import Vue from 'vue';
import translations from '@/utils/lang.js';

Vue.directive( 'pattern', {
  update (el) {
    const sourceValue = el.value;

    const newValue = sourceValue
    .replace(/[^a-zA-Zа-яА-ЯёЁ_]/g, '') // убираем знаки препиния, кирилица/латиница/

    if (sourceValue !== newValue) {
      el.value = newValue;
      el.dispatchEvent(new Event('input', { bubbles: true }));
    }
  },
})

export default {
  name: 'NameField',
  props: {
    value: {
      type: String,
      default: '',
    },
    v: {
      type: Object,
      required: true,
    },
    label: {
      type: String,
      default: 'Имя',
    },
    id: {
      type: String,
      required: true,
    },
  },
  computed: {
    name: {
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
