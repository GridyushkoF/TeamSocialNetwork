<template>
  <div class="push" :class="{ open: isOpen }">
    <div class="push__overlay" @click.stop="closePush" />

    <div class="push__wrap" :class="{ open: isOpen }" ref="wrap">
      <div class="push__list" ref="list">
        <div v-if="visibleNotifications.length > 0">
          <div class="push__item" v-for="info in visibleNotifications" :key="info.data.id">
            <div class="main-layout__user-pic" style="background-color: #8bc49e">
              <div class="push__img" v-if="info?.data?.author?.photo">
                <img :src="info?.data?.author?.photo" :alt="info?.data?.author?.firstName" />
              </div>
              <div v-else>
                {{ info?.data?.author?.firstName[0] + ' ' + info?.data?.author?.lastName[0] }}
              </div>
            </div>
            <p class="push__content">
              <router-link class="push__content-name" :to="getRouteByNotification(info?.data?.authorId)">
                <div
                  v-if="
                    info?.data?.notificationType !== (
                      'FRIEND_REQUEST' ||
                      'FRIEND_BIRTHDAY' ||
                      'FRIEND_APPROVE' ||
                      'FRIEND_BLOCKED' ||
                      'FRIEND_UNBLOCKED' ||
                      'FRIEND_SUBSCRIBE'
                    )
                  "
                >
                  <span class="push__content-preview">
                    {{ info?.data?.author?.firstName + ' ' + info?.data?.author?.lastName }}
                  </span>
                  {{ getNotificationsTextType(info?.data?.notificationType) }}
                  {{ info?.data?.content }}
                </div>

                <div v-else-if="info?.data?.notificationType !== ('USER_BIRTHDAY' || 'SEND_EMAIL_MESSAGE')">
                  {{ getNotificationsTextType(info?.data?.notificationType) }}
                  {{ info?.data?.content }}
                  <span class="push__content-preview">
                    {{ info?.data?.author?.firstName + ' ' + info?.data?.author?.lastName }}
                  </span>
                </div>

                <div v-else>
                  {{ getNotificationsTextType(info?.data?.notificationType) }}
                  {{ info?.data?.content }}
                </div>

              </router-link>
              <span class="push__time">{{ info.data.sentTime | moment('from') }}</span>
            </p>
          </div>
        </div>
        <div v-else>
          <div>
            <p class="no__notifications">{{ translations.notNotification }}</p>
          </div>
        </div>
      </div>
      <button
        class="push__btn"
        @click.prevent="showMore"
        v-if="showButtonMore"
      >
        {{ translations.showmoreNotification }}
      </button>
      <button
        class="push__btn"
        @click.prevent="readedButton"
        v-if="showButtonReaded"
      >
        {{ translations.readedNotification }}
      </button>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions, mapMutations } from 'vuex';
import { getRouteByNotification } from '@/utils/notifications.utils.js';
import translations from '@/utils/lang.js';
export default {
  name: 'Push',
  props: {
    isOpen: Boolean,
  },
  data() {
    return {
      visibleNotifications: [],
      showCount: 3,
      isClickedButton: false
    }
  },
  computed: {
    ...mapGetters('profile/notifications', [
      'getNotifications',
      'getNotificationsLength',
      'getNotificationsTextType',
    ]),
    shouldUpdateVisibleNotifications() {
      return this.getNotifications.length === 0;
    },

    showButtonMore() {
      if (this.visibleNotifications.length !== this.getNotifications.length) {
        return true
      } else if (this.getNotifications.length === 0) {
        return false
      } else {
        return false
      }
    },

    showButtonReaded() {
      if (this.getNotifications.length === 0) {
        return false
      } else if (this.getNotifications.length === this.visibleNotifications.length) {
        return true
      } else {
        return false
      }
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

  watch: {
    isOpen(val) {
      if (val) {
      this.fetchNotifications()
        .then(() => {
          this.loadVisibleNotifications();
      });
      if (this.getNotifications.length === 0) {
        this.fetchNotifications()
      }
        this.$refs.list.scrollTop = 0;
      } else {
        this.fetchNotificationsLength();
        this.visibleNotifications = [];
      }
    },
    shouldUpdateVisibleNotifications(newValue, oldValue) {
      if (newValue !== oldValue) {
        this.visibleNotifications = [...this.getNotifications];
      }
    }
  },

  mounted() {

    if (this.getNotificationsLength === 0) {
      this.fetchNotificationsLength();
    }
    if (
      window.innerHeight -
        this.$refs.wrap.getBoundingClientRect().top -
        this.$refs.wrap.offsetHeight <
      0
    ) {
      this.$refs.wrap.style.maxHeight = `${
        window.innerHeight - this.$refs.wrap.getBoundingClientRect().top
      }px`;
    }
    // window.onscroll = () => {
    //   this.closePush();
    // };

  },

  methods: {
    ...mapActions('profile/notifications', ['fetchNotifications', 'fetchNotificationsLength', 'readedNotifications']),
    ...mapMutations('profile/notifications', ['incrementOffset']),
    getRouteByNotification,

    closePush() {
      if (!this.isOpen) return;
      this.$emit('close-push');
    },

    readedButton() {
      return this.readedNotifications();
    },

    loadVisibleNotifications() {
      this.visibleNotifications = this.getNotifications.slice(0, this.showCount);
    },

    showMore() {
      const startIndex = this.visibleNotifications.length;
      const newVisibleNotifications = this.getNotifications.slice(startIndex, startIndex + this.showCount);
      this.visibleNotifications = [...this.visibleNotifications, ...newVisibleNotifications];
    }
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'

.no__notifications
  color ui-cl-color-full-black
  padding 15px 0
  text-align center
  cursor default

.push
  .push__overlay
    display none

  &.open
    .push__overlay
      display block

.push__overlay
  position fixed
  top 0
  left 0
  right 0
  bottom 0
  z-index -1
  cursor default

.push__wrap
  position absolute
  background ui-cl-color-white-theme
  box-shadow box-shadow-main
  right 0px
  top 65px
  width 100%
  border-radius border-small
  max-width 470px
  max-height 675px
  min-width 400px
  z-index 100
  opacity 0
  visibility hidden
  transition all 0.2s
  overflow-y auto

  &.open
    opacity 1
    visibility visible

.push__list
  overflow-y auto
  padding 10px
  max-height 450px

.push__item
  display flex
  align-items center
  padding 15px 5px
  overflow hidden
  transition all .2s ease-in-out

  @media (any-hover: hover)
    &:hover
      background-color ui-cl-color-white-bright
      border-radius border-super-small
      &+&
        border-top 0

  &+&
    border-top 1px solid ui-cl-color-white-bright-second

.push__btn
  display flex
  width 100%
  background transparent
  align-items center
  justify-content center
  font-weight font-weight-bold
  font-size font-size-downdefault
  letter-spacing 0.01em
  color ui-cl-color-eucalypt
  border-top 1px solid ui-cl-color-white-bright-second
  height 55px
  transition all .2s ease-in-out
  @media (any-hover: hover)
    &:hover
      background-color ui-cl-color-white-bright


.main-layout__user-pic
  width 50px
  height 50px
  border-radius border-half
  overflow hidden
  margin-right 15px
  flex none
  background-color ui-cl-color-light-eucalypt

  div
    display flex
    align-items center
    justify-content center
    width 100%
    height 100%
    object-fit cover

  img
    display flex
    align-items center
    justify-content center
    width 100%
    height 100%
    object-fit cover

@media (max-width 992px)
  .push__wrap
    right 0
    left 120px
    width auto
</style>
