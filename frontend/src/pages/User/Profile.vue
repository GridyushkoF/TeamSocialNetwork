<template>
  <div class="profile inner-page" ref="page">
    <div class="inner-page__main">
      <div class="profile__info-top">
        <div class="profile__info">
          <profile-info me="me" online="online" :info="getInfo" />
        </div>
      </div>

      <div class="profile__news" id="mypublications">
        <div class="profile__tabs">
          <a
            href="#"
            class="profile__tab"
            @click.prevent="setActive('publications')"
            :class="{ 'friends__tabs__link active': isActive('publications') }"
          >
            {{ translations.profileBlockMyPublished }} ({{ filteredWall.posted.length || 0 }})
          </a>

          <a
            href="#"
            class="profile__tab"
            @click.prevent="setActive('queuedPublications')"
            :class="{ 'friends__tabs__link active': isActive('queuedPublications') }"
          >
            {{ translations.profileBlockMyQueued }} ({{ getWallQueuedLength }})
          </a>
        </div>

        <div class="profile__add">
          <news-add />
        </div>


        <div class="profile__news-list">
          <error-block v-if="!loading && error" :message="errorMessage" />

          <div
            class="profile__news-list"
            :class="{ 'active': isActive('publications') }"
            v-if="activeItem === 'publications'"
          >
            <news-block
              edit="edit"
              deleted="deleted"
              :queued="false"
              v-for="news in filteredWall.posted"
              :key="news.id"
              :info="news"
            />
          </div>

          <div
            class="profile__news-list"
            :class="{ 'active': isActive('queuedPublications') }"
            v-if="activeItem === 'queuedPublications'"
          >
            <news-block
              edit="edit"
              deleted="deleted"
              :queued="true"
              v-for="news in filteredWall.queued"
              :key="news.id"
              :info="news"
            />
          </div>

        </div>

        <div class="profile__news-list">

          <div class="spinner-wrapper" v-if="loading">
            <spinner />
          </div>

          <auto-paginator
            :page="getWallPagination.page"
            :loading="loading"
            :totalPage="getWallPagination.total"
            :action="setMyWall"
          />
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import ProfileInfo from '@/components/Profile/Info';
import NewsAdd from '@/components/News/Add';
import NewsBlock from '@/components/News/Block';
import Spinner from '@/components/Spinner.vue';
import ErrorBlock from '@/components/ErrorBlock.vue';
import AutoPaginator from '@/components/AutoPaginator.vue';
import translations from '@/utils/lang.js';
import { mapGetters, mapActions, mapState, mapMutations } from 'vuex';

export default {
  name: 'Profile',
  components: { ProfileInfo, NewsAdd, NewsBlock, Spinner, ErrorBlock, AutoPaginator },

  data: () => ({
    activeItem: 'publications',
  }),

  computed: {
    ...mapGetters('profile/info', ['getInfo']),
    ...mapGetters('users/info', ['getWall', 'getWallQueuedLength', 'getWallPagination']),
    ...mapState('global/status', ['loading', 'error', 'errorMessage']),

    translations() {
      const lang = this.$store.state.auth.languages.language.name;
      if (lang === 'Русский') {
        return translations.rus;
      } else {
        return translations.eng;
      }
    },

    filteredWall() {
      const wall = this.getWall;
      const posted = wall.filter(item => item.type === 'POSTED');
      const queued = wall.filter(item => item.type === 'QUEUED');
      return { posted, queued };
    }
  },

  async created() {
    this.clearWall();
    this.clearPagination();
    if (!this.getInfo) {
      await this.apiInfo();
    }

    if (!this.getWall.length) {
      this.setMyWall({ page: this.getWallPagination.page - 1 || 0 });
    }
  },

  methods: {
    ...mapActions('users/info', ['apiWall']),
    ...mapActions('profile/info', ['apiInfo']),
    ...mapMutations('users/info', ['clearWall', 'clearPagination']),

    setMyWall({ page }) {
      if (typeof this.getInfo?.id === 'undefined') return;
      return this.apiWall({
        accountIds: this.getInfo.id,
        author: this.getInfo,
        page,
      });
    },

    isActive (menuItem) {
      return this.activeItem === menuItem
    },
    setActive (menuItem) {
      this.activeItem = menuItem
    },
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'

.profile
  .inner-page__main
    max-width 100%
    margin-right 0
  &__title
    font-family 'Exo', Arial, sans-serif
    font-weight 200
    font-size 30px
    color ui-cl-color-steel-gray
  &__tabs__title
    cursor pointer
    font-family 'Exo', Arial, sans-serif
    font-weight font-weight-bold
    font-size 30px
    margin-bottom 20px
    color ui-cl-color-full-black

.profile__info-top
  display grid
  gap 50px
  margin-bottom 30px

.profile__news-list
  display flex
  flex-direction column
  gap 20px

@media (min-width: 320px) and (max-width: 768px)
  .profile
    &-info
      padding 15px
      flex-direction column
      &__pic
        margin-right 0
        padding-left 0
        padding-right 0
        border-right 0
      &__main
        padding-top 10px
      &__img
        width 100px
        height 100px
      &__name
        margin-bottom 0
        font-size 23px
      &__header
        padding-bottom 10px
        justify-content center
        border-bottom 1px solid ui-cl-color-e6e6e6
        margin-bottom 15px
      &__block
        font-size font-size-small
      &__title
        max-width 125px
      &__block:last-child
        margin-top 20px
    &__info
      margin-bottom 0
      &-top
        grid-template-columns unset
        gap 25px
        margin-bottom 0
    &__news
      margin-top 20px
    &__tabs
      &__title
        font-size font-size-small-medium
        margin-bottom 15px
    &__tab
      font-size font-size-small

</style>
