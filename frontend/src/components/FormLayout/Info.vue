<template>
  <div class="form-layout__main">
    <div class="form-layout__logo">
      <p>Code Lounge</p>
    </div>
    <h1 class="form-layout__title">{{ translations.title }}</h1>
    <p class="form-layout__text">{{ translations.text }}</p>
    <p class="form-layout__descr" v-if="info.descr">{{ translations.descr }}</p>

    <router-link
      v-if="info.btn && info.btn.href"
      class="form-layout__btn"
      tag="button"
      :to="info.btn.href"
    >
      {{ translations.btn.text }}
    </router-link>

    <router-link
      v-else-if="info.btn"
      class="form-layout__btn"
      tag="button"
      :to="{ name: info.btn.link }"
    >
      {{ translations.btn.text }}
    </router-link>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  name: 'FormLayoutInfo',
  data: () => ({
    language: '',
    isOpenLanguageBlock: false,
  }),
  computed: {
    ...mapGetters('auth/info', ['getInfoById']),
    info() {
      return this.getInfoById(this.$route.path.split('/')[1]);
    },

    translations() {
      const lang = this.$store.state.auth.languages.language.name;
      if (lang === 'Русский') {
        return this.info;
      } else {
        return this.info.eng;
      }
    },
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'

.form-layout__main
  margin auto 0
  margin-bottom 30px

.form-layout__logo
  position absolute
  top 30px
  left 30px
  z-index 1000
  display inline-flex
  border-radius border-small
  user-select none
  pointer-events none
  gap 10px
  max-width unset
  p
    color ui-cl-color-white-theme
    font-size 25px
    font-weight font-weight-light
    text-transform uppercase

.form-layout__title
  color ui-cl-color-white-theme
  font-style normal
  font-weight font-weight-light
  font-size 40px
  line-height 53px
  letter-spacing -0.01em
  margin-bottom 20px

.form-layout__text
  font-size font-size-updefault
  line-height 30px
  letter-spacing 0.01em
  color ui-cl-color-jumbo
  margin-bottom 20px


.form-layout__descr
  font-size font-size-downdefault
  letter-spacing 0.01em
  color ui-cl-color-scarpa-flow
  margin-bottom 15px
  margin-top 30px
</style>
