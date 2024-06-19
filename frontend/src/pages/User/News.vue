<template>
  <div class="news inner-page">
    <div class="inner-page__main">
      <div class="inner-page__main__container">
        <div class="news__add">
          <news-add user="user" />
        </div>

        <template v-if="feeds">
          <div class="news__list" v-if="getInfo">
            <news-block
              v-for="feed in filteredWall.posted"
              :key="feed.id"
              :info="feed"
              :queued="false"
              :edit="getInfo.id === feed.author.id"
              :deleted="getInfo.id === feed.author.id"
            />
          </div>
        </template>

        <div class="spinner-wrapper" v-if="loading">
          <spinner />
        </div>

        <error-block v-if="!loading && error" :message="errorMessage" />
      </div>

      <recommend-friend />

      <auto-paginator
        :page="feedsPagination.page || 0"
        :loading="loading"
        :totalPage="feedsPagination.total"
        :action="apiFeeds"
      />
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters, mapMutations, mapState } from 'vuex';
import RecommendFriend from '@/components/RecommendFriend.vue';
import NewsBlock from '@/components/News/Block';
import NewsAdd from '@/components/News/Add';
import Spinner from '@/components/Spinner.vue';
import ErrorBlock from '@/components/ErrorBlock.vue';
import AutoPaginator from '@/components/AutoPaginator.vue';

export default {
  name: 'News',
  components: { NewsBlock, NewsAdd, Spinner, ErrorBlock, AutoPaginator, RecommendFriend },

  data() {
    return {
      frinedsList: null
    }
  },

  computed: {
    ...mapState('global/status', ['loading', 'error', 'errorMessage']),
    ...mapGetters('profile/info', ['getInfo']),
    ...mapState('profile/feeds', ['feeds', 'feedsPagination']),

    filteredWall() {
      const wall = this.feeds;
      const posted = wall.filter(item => item.type === 'POSTED');
      const queued = wall.filter(item => item.type === 'QUEUED');
      return { posted, queued };
    }
  },

  async created() {
    this.clearFeeds();
  },

  methods: {
    ...mapActions('profile/feeds', ['apiFeeds']),
    ...mapMutations('profile/feeds', ['clearFeeds', 'setFeeds']),
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'

.news-block__changed-time
  top 15px

.news-block .edit
  top 50px

.edit--small
  top 25px !important

.news__add
  border-radius border-small
  margin-bottom 30px

.news-add
  border-radius border-small

.news__list
  display flex
  flex-direction column
  gap 20px
  margin-bottom 30px

@media (min-width: 320px) and (max-width: 768px)
  .inner-page
    &__main__container
      margin-right 0 !important

</style>

<style lang="stylus" scoped>
.spinner-wrapper
  width 100px
  height 100px
  margin 0 auto
  padding 50px
</style>
