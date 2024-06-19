<template>
  <div class="form__group last-block_reg">
    <input
      type="checkbox"
      name="confirm"
      class="form__checkbox"
      v-model="confirm"
      :id="id"
      :class="{ invalid: v.$dirty && !v.sameAs }"
    />

    <label :for="id" class="form__checkbox-label">
      <span>{{ translations.confirmComponent1 }}</span>
      <a href="#">{{ translations.confirmComponent2 }}</a>
      <span>{{ translations.confirmComponent3 }}</span>
      <a href="">{{ translations.confirmComponent4 }}</a>
    </label>
  </div>
</template>

<script>
import translations from '@/utils/lang.js';
export default {
  name: 'ConfirmField',
  props: {
    value: {
      type: Boolean,
      default: false,
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
    confirm: {
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
