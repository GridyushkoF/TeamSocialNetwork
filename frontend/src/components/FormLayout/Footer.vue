<template>
  <div class="form-layout__footer">
    <a
      href="#"
      v-if="noUseContent"
      class="form-layout__footer-support"
    >
      {{ translations.support }}
    </a>

    <div
      class="form-layout__footer-language"
      :class="{'footer__auth': showLanguage}"
    >
      <span>{{ translations.langSelect }}</span>
      <span
        class="active"
        @click.prevent="toggleLanguageList"
      >
        {{ activeLanguage === 'Русский' ? translations.lang : translations.lang }}
        <arrow-bottom />
      </span>
      <transition name="fade">
        <ul class="form-layout-list__language" v-if="languageShow" v-click-outside="closeLanguageList">
          <li
            v-for="lang in languageList"
            :key="lang.id"
            @click="selectLanguage(lang)"
            class="form-layout-list__language-item"
            :class="{'active__lang': activeLanguage === lang.name}"
          >
            <span>{{ lang.name }}</span>
            <span>{{ lang.desc }}</span>
          </li>
        </ul>
      </transition>
    </div>

    <span v-if="noUseContent" class="form-layout__footer-copyright"> © Copyright {{ year }} Code Lounge. </span>
  </div>
</template>

<script>
import vClickOutside from 'v-click-outside';
import translations from '@/utils/lang.js';
import { mapMutations } from 'vuex';
import ArrowBottom from '../../Icons/ArrowBottom.vue';

export default {
  name: 'FormLayoutFooter',
  directives: {
    clickOutside: vClickOutside.directive
  },
  components: {
    ArrowBottom
  },

  props: {
    showLanguage: {
      type: Boolean,
      default: true
    },

    noUseContent: {
      type: Boolean,
      default: true
    },
  },

  data: () => ({
    languageShow: false,
    selectedLanguage: { id: 1, name: 'Русский', desc: 'RU' },
  }),

  computed: {
    activeLanguage() {
      return this.$store.state.auth.languages.language.name;
    },

    translations() {
      const lang = this.$store.state.auth.languages.language.name;
      if (lang === 'Русский') {
        return translations.rus;
      } else {
        return translations.eng;
      }
    },

    year() {
      let date = new Date();
      return date.getFullYear();
    },

    languageList() {
      return [
        { id: 1, name: 'Русский', desc: 'RU' },
        { id: 2, name: 'Английский', desc: 'EN' },
      ];
    }
  },

  methods: {
    ...mapMutations('auth/languages', ['setLanguage']),
    closeLanguageList() {
      this.languageShow = false
    },

    selectLanguage(lang) {
      this.selectedLanguage = lang;
      this.languageShow = false;
      this.setLanguage(lang)
    },

    toggleLanguageList() {
      this.languageShow = !this.languageShow;
    },
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'

.form-layout__footer
  display flex
  align-items center
  flex-wrap wrap
  color ui-cl-color-gun-powder
  font-size font-size-small
  line-height 18px

.form-layout__footer-support
  color ui-cl-color-comet
  margin-right 8.3%

.form-layout-list__language
  position absolute
  display flex
  flex-direction column
  gap 3px
  top 35px
  right -35%
  padding 10px
  z-index 10
  background-color ui-cl-color-comet
  border-radius border-super-small
  &.fade-enter-active,
  &.fade-leave-active
    transition all .2s ease-in-out
  &.fade-enter,
  &.fade-leave-to
    opacity 0
  &:before
    content ""
    border solid transparent
    position absolute
    bottom 100%
    left 50%
    border-bottom-color ui-cl-color-comet
    border-width 9px
    margin-right -1px
  &-item
    display flex
    color #afadde
    align-items center
    font-weight font-weight-medium
    border-radius border-super-small
    gap 10px
    justify-content space-between
    padding 8px
    cursor pointer
    transition all .2s ease-in-out
    span:nth-child(2)
      color ui-cl-color-light-grey
    @media (any-hover: hover)
      &:hover
        background-color #4f4f65
        color ui-cl-color-white-theme
        span:nth-child(2)
          color ui-cl-color-white-theme
    &.active__lang
      background-color #4f4f65
      color ui-cl-color-white-theme
      span:nth-child(2)
        color ui-cl-color-white-theme


.form-layout__footer-language
  position relative
  margin-right 5.8%

  .active
    font-weight font-weight-bold
    cursor pointer
    transition all 0.2s

    &:hover
      color ui-cl-color-white-theme
</style>
