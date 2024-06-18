<!-- eslint-disable vue/html-indent -->
<template>
  <div class="main-layout__sidebar" :class="{ 'sticky': isSticky, 'admin': isAdminPage }" v-scroll="handleScroll">
    <div class="wrapper__sidebar">
      <transition>
        <nav class="main-layout__nav">
          <router-link
            :title="translations === 'Русский' ? item.textEng : item.textEng"
            class="main-layout__link"
            v-for="(item, index) in info"
            :key="index"
            :exact="item.exact"
            :to="item.link"
            :class="{
              'main-layout__link--im': item.link.name === 'Im' && unreadedMessages?.count > 0 || (item.link.name === 'Friends' && requestsCount?.count),
              big: unreadedMessages?.count >= 100,
            }"
            :data-push="
              item.link.name === 'Im'
                ? unreadedMessages?.count
                : item.link.name === 'Friends'
                ? requestsCount?.count
                : false
            "
          >
            <div class="simple-svg-wrapper">
              <sidebar-icons :name="item.icon" />
            </div>
            <span v-if="!isAdminPage" class="sidebar__text">{{ translations === 'Русский' ? item.text : item.textEng }}</span>
            <span v-else>{{ item.text }}</span>
          </router-link>
        </nav>
      </transition>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions, mapState } from 'vuex';
import SidebarIcons from '../../Icons/sidebar/SidebarIcons.vue';

export default {
  name: 'MainLayoutSidebar',
  directives: {
    scroll: {
      inserted: function (el, binding) {
        let f = function (evt) {
          if (binding.value(evt, el)) {
            window.removeEventListener('scroll', f)
          }
        }
        window.addEventListener('scroll', f)
      }
    }
  },
  components: { SidebarIcons },
  data() {
    return {
      isOpen: false,
      isSticky: false,
      scrollPosition: 0,
    }
  },
  computed: {
    ...mapGetters('global/menu', ['getSidebarById']),
    ...mapGetters('profile/dialogs', ['unreadedMessages']),
    ...mapState('profile/friends', ['requestsCount']),

    isAdminPage() {
      return this.$route.path.indexOf('admin') !== -1;
    },
    info() {
      return this.getSidebarById(this.isAdminPage ? 'admin' : 'user');
    },

    translations() {
      return this.$store.state.auth.languages.language.name;
    },

    removeBodyOverflow() {
      const body = document.querySelector('body');
      return {
        computedStyle: () => {
          if (this.isOpen) {
            body.style.overflow = 'hidden';
          } else {
            body.style.overflow = '';
          }
        }
      }
    },

    bodyOverflow() {
      return this.removeBodyOverflow.computedStyle();
    }
  },

  mounted() {
    this.apiRequestsCount();
    this.apiUnreadedMessages();
    this.scrollPosition = window.pageYOffset;
  },

  created() {
    this.$options.directives.scroll = {
      inserted: (el, binding) => {
        const callback = binding.value;
        let options = {};
        if (binding.arg) {
          options = binding.arg;
        }
        el._onScroll = () => {
          callback(el, options);
        };
        window.addEventListener('scroll', el._onScroll);
      },
      unbind: (el) => {
        window.removeEventListener('scroll', el._onScroll);
      }
    };
  },

  destroyed() {
    window.removeEventListener('scroll', this.handleScroll);
  },

  methods: {
    ...mapActions('profile/friends', ['apiRequestsCount']),
    ...mapActions('profile/dialogs', ['apiUnreadedMessages']),

    handleScroll() {
      this.isSticky = window.pageYOffset > 85;
    }
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'

.form-layout__main
  margin auto 0

.wrapper__sidebar
  width 1280px
  padding 0 15px
  margin 0 auto

.main-layout__nav
  display flex
  gap 30px

.main-layout__logotype
  display inline-flex
  align-items center
  user-select none
  pointer-events none
  gap 10px
  width 100%
  max-width unset
  p
    font-size 25px
    font-weight font-weight-medium
    text-transform uppercase
  svg path:nth-child(3)
    animation gradient 1s linear infinite

@keyframes gradient {
  0% {
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
}

.main-layout__sidebar
  position absolute
  top 85px
  left 0
  color ui-cl-color-white-theme
  display flex
  width 100%
  padding 20px 0
  background ui-cl-color-white-theme
  border-bottom 1px solid #cbcbcb
  z-index 7
  transition none

  &.admin
    background ui-cl-color-gun-powder
    padding 0
    top 0
    .main-layout__link
      padding 20px 10px
      color ui-cl-color-white-theme
      transition all .2s ease-in-out
      @media (any-hover: hover)
        &:hover
          background #ffffff14

  &.sticky
    position fixed
    top 0
    transition none

.main-layout__logo
  margin-bottom 100px
  display flex
  align-items center

  &.admin
    max-width 100%

.main-layout__admin-logo
  width 100%
  margin-left 10px

.main-layout__link
  color #6f6f6f
  display flex
  gap 5px
  align-items center
  transition color .2s ease-in-out
  position relative

  &:hover
    color #000


  &--im
    &:after
      content attr(data-push)
      font-weight font-weight-regular
      font-size font-size-super-small
      width 15px
      height 15px
      color ui-cl-color-white-theme
      background-color #E65151
      border-radius border-half
      display flex
      align-items center
      justify-content center
      position absolute
      right -13px
      top 10%
      transform translateY(-50%)

    &.big
      &:after
        width 35px
        height 35px
        right 5px

  &.router-link-active
    color ui-cl-color-eucalypt

  .simple-svg-wrapper
    width 16px
    height 16px
    flex none

    & svg
      vertical-align text-top
      height 100%


.burger-menu__button
  position relative
  display inline-block
  width 30px
  background transparent
  height 20px
  position relative
  cursor pointer
  z-index 150

.burger-menu__button span
  display block
  position absolute
  height 3px
  width 100%
  background-color ui-cl-color-eucalypt
  opacity 1
  left 0
  transform rotate(0deg)
  transition transform 0.25s ease-in-out, opacity 0.25s ease-in-out

.burger-menu__button span:nth-child(1)
  top 0px

.burger-menu__button span:nth-child(2)
  top 8px

.burger-menu__button span:nth-child(3)
  top 16px

.burger-menu__button.active span:nth-child(1),
.burger-menu__button.active span:nth-child(4)
  transform rotate(45deg)
  top: 16px;

.burger-menu__button.active span:nth-child(2)
  opacity 0
  transform rotate(45deg)

.burger-menu__button.active span:nth-child(3)
  transform rotate(-45deg)

</style>
