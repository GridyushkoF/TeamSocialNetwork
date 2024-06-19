<template>
  <div class="success-register">
    <h4 class="success-register__title">{{ translations.registerSucces }}</h4>
    <simple-svg :filepath="'/static/img/success-register.svg'" />
  </div>
</template>

<script>
import translations from '@/utils/lang.js';
export default {
  name: 'RegisterSuccess',
  computed: {
    translations() {
      const lang = this.$store.state.auth.languages.language.name;
      if (lang === 'Русский') {
        return translations.rus;
      } else {
        return translations.eng;
      }
    },
  }
};
</script>

<style lang="stylus">
.success-register
  height 100%
  display flex
  flex-direction column
  justify-content center
  align-items center
  &__title
    color #fff
    font-weight 500
    margin-bottom 30px
    font-size 40px

  .simple-svg-wrapper
    width 130px
</style>
