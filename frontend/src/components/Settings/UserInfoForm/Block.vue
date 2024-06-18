<template>
  <div class="settings-main-input">
    <span class="user-info-form__label_stylus">{{ label }}</span>

    <div class="user-info-form__wrap">
      <textarea
        class="user-info-form__input_stylus user-info-form__input_stylus--textarea"
        :value="value"
        @input="$emit('input', $event.target.value)"
        v-if="about"
        maxlength="200"
      />

      <input
        class="user-info-form__input_stylus"
        type="text"
        v-pattern="20"
        :value="value"
        :placeholder="placeholder"
        :required="required"
        @input="$emit('input', $event.target.value)"
        v-if="text"
      />

      <input
        class="user-info-form__input_stylus"
        type="text"
        :value="value"
        :placeholder="placeholder"
        @input="$emit('input', $event.target.value)"
        :ref="phone && 'phone'"
        v-if="phone"
      />
    </div>
  </div>
</template>

<script>
import Inputmask from 'inputmask';
import Vue from 'vue';

Vue.directive( 'pattern', {
  update (el, binding) {
    const sourceValue = el.value;
    const maxLength = parseInt(binding.value);
	  el.maxLength = maxLength;

    const newValue = sourceValue
    .replace(/[^a-zA-Zа-яА-ЯёЁ_]/g, '') // убираем знаки препиния, кирилица/латиница/
    .substring(0, maxLength); // ограничиваем колличество вводимых знаков, дублируя ограничение атрибутом.

    if (sourceValue !== newValue) {
      el.value = newValue;
      el.dispatchEvent(new Event('input', { bubbles: true }));
    }
  },
})

export default {
  name: 'UserInfoFormBlock',

  props: {
    label: String,
    placeholder: String,
    value: String,
    phone: Boolean,
    about: Boolean,
    text: String,
    required: Boolean,
  },

  mounted() {
    if (this.phone) {
      var im = new Inputmask('+7 (999) 999-99-99');
      im.mask(this.$refs.phone);
    }
  },

  methods: {
    validate() {
      if (!this.value) {
        return false;
      } else {
        return true;
      }
    }
  }
};
</script>

<style lang="stylus">
.settings-main-input
  &:not(:last-child)
    margin-bottom 15px
</style>
