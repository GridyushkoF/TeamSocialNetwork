<template>
  <div class="friends inner-page">
    <div class="inner-page__main">
      <div class="friends__header">
        <div class="friends__header-left">
          <h2
            class="friends__title"
            :style="
              friends.length === 0 ||
                activeTab === 'no-data' ||
                activeTab === 'no-data-users' ?
                  'margin-bottom: 0' : ''"
          >
            {{ translations.friendsTitle }}
          </h2>
          <div
            class="friends__search"
            v-if="friends.length !== 0 &&
              activeTab !== 'no-data' &&
              activeTab !== 'no-data-users'"
          >
            <div class="friends__search-icon">
              <search-icon />
            </div>

            <input
              class="friends__search-input"
              :placeholder="translations.friendsSearchPlaceholder"
              v-model="firstName"
              @keydown.enter="searchFriends"
            />
          </div>
        </div>
        <ul class="friends__tabs">
          <li class="friends__tabs__item" v-if="localFriends.REQUEST_FROM">
            <button
              :class="{ 'friends__tabs__link active': activeTab === 'REQUEST_FROM' }"
              @click.prevent="setActive('REQUEST_FROM')"
            >
              {{ `${translations.friendsRequestsFrom}` + ' ' + `(${paginations.REQUEST_FROM.totalElements || 0})` }}
            </button>
          </li>

          <li class="friends__tabs__item" v-if="localFriends.REQUEST_TO">
            <button
              :class="{ 'friends__tabs__link active': activeTab === 'REQUEST_TO' }"
              @click.prevent="setActive('REQUEST_TO')"
            >

              {{  `${translations.friendsRequestsTo}` + ' ' + `(${paginations.REQUEST_TO.totalElements || 0})` }}
            </button>
          </li>

          <li class="friends__tabs__item" v-if="localFriends.FRIEND">
            <button
              :class="{ 'friends__tabs__link active': activeTab === 'FRIEND' }"
              @click.prevent="setActive('FRIEND')"
            >
              {{ `${translations.sidebarFriend}` + ' ' + `(${paginations.FRIEND.totalElements || 0})` }}
            </button>
          </li>

          <li class="friends__tabs__item" v-if="localFriends.SUBSCRIBED">
            <button
              :class="{ 'friends__tabs__link active': activeTab === 'SUBSCRIBED' }"
              @click.prevent="setActive('SUBSCRIBED')"
            >
              {{ `${translations.friendsMySubscribe}` + ' ' + `(${paginations.SUBSCRIBED.totalElements || 0})` }}
            </button>
          </li>

          <li class="friends__tabs__item" v-if="localFriends.BLOCKED">
            <button
              :class="{ 'friends__tabs__link active': activeTab === 'BLOCKED' }"
              @click.prevent="setActive('BLOCKED')"
            >
              {{ `${translations.friendsBlocked}` + ' ' + `(${paginations.BLOCKED.totalElements || 0})` }}
            </button>
          </li>

          <li class="friends__tabs__item" v-if="localFriends.WATCHING">
            <button
              :class="{ 'friends__tabs__link active': activeTab === 'WATCHING' }"
              @click.prevent="setActive('WATCHING')"
            >
              {{ `Подписки` + ' ' + `(${paginations.WATCHING.totalElements || 0})` }}
              <!-- {{ `${translations.friendsMyPesonalSubscribed}` + ' ' + `(${paginations.WATCHING.totalElements || 0})` }} -->
            </button>
          </li>
        </ul>
      </div>

      <div class="friends__content">
        <div class="friends__list">
          <!-- Запросы в друзья / поиск реализован -->
          <div v-if="activeTab === 'REQUEST_FROM'" class="friends_group active">
            <h3
              class="friends_group_title friends__title"
            >
              {{ translations.friendsRequestsFrom }}
            </h3>

            <div v-if="friendSearch !== null && friendSearch.content.length !== 0">
              <div class="friend__search__resultats">
                <p class="friend__search-title">
                  {{ translations.friendsSearchDescription }} "{{ localFirstName }}":
                </p>
                <button
                  class="friend__search-clear"
                  @click.prevent="resetFriendSearch"
                >
                  {{ translations.friendsSearchResultClear }}
                  <span>✕</span>
                </button>
              </div>

              <search-block
                v-for="user in friendSearch.content"
                :key="user.id"
                :info="user"
              />
            </div>

            <div v-if="friendSearch === null || friendSearch.content.length === 0">
              <friends-block
                friend="friend"
                v-for="friend in localFriends?.REQUEST_FROM"
                :key="friend.id"
                :info="friend"
                accept-button
              />
              <button
                class="friends-btn__more"
                @click.prevent="loadMoreFriends('REQUEST_FROM')"
                v-if="localFriends?.REQUEST_FROM.length !== paginations.REQUEST_FROM.totalElements || paginations.REQUEST_FROM.totalElements === 0"
              >
                {{ translations.friendsShowMore }}
              </button>
            </div>
          </div>
          <!-- Исходящие запросы / поиск реализован -->
          <div v-if="activeTab === 'REQUEST_TO'" class="friends_group active">
            <h3
              class="friends_group_title friends__title"
            >
              {{ translations.friendsRequestsTo }}
            </h3>

            <div v-if="friendSearch !== null && friendSearch.content.length !== 0">
              <div class="friend__search__resultats">
                <p class="friend__search-title">
                  {{ translations.friendsSearchDescription }} "{{ localFirstName }}":
                </p>
                <button
                  class="friend__search-clear"
                  @click.prevent="resetFriendSearch"
                >
                  {{ translations.friendsSearchResultClear }}
                  <span>✕</span>
                </button>
              </div>

              <search-block
                v-for="user in friendSearch.content"
                :key="user.id"
                :info="user"
              />
            </div>

            <div v-if="friendSearch === null || friendSearch.content.length === 0">
              <friends-block
                friend="friend"
                v-for="friend in localFriends?.REQUEST_TO"
                :key="friend.id"
                :info="friend"
              />
              <button
                class="friends-btn__more"
                @click.prevent="loadMoreFriends('REQUEST_TO')"
                v-if="localFriends?.REQUEST_TO.length !== paginations.REQUEST_TO.totalElements || paginations.REQUEST_TO.totalElements === 0"
              >
                {{ translations.friendsShowMore }}
              </button>
            </div>
          </div>
          <!-- Друзья / поиск реализован -->
          <div v-if="activeTab === 'FRIEND'" class="friends_group active">
            <h3
              class="friends_group_title friends__title"
            >
              {{ translations.sidebarFriend }}
            </h3>

            <div v-if="friendSearch !== null && friendSearch.content.length !== 0">
              <div class="friend__search__resultats">
                <p class="friend__search-title">
                  {{ translations.friendsSearchDescription }} "{{ localFirstName }}":
                </p>
                <button
                  class="friend__search-clear"
                  @click.prevent="resetFriendSearch"
                >
                  {{ translations.friendsSearchResultClear }}
                  <span>✕</span>
                </button>
              </div>

              <search-block
                v-for="user in friendSearch.content"
                :key="user.id"
                :info="user"
              />
            </div>

            <div v-if="friendSearch === null || friendSearch.content.length === 0">
              <friends-block
                friend="friend"
                v-for="friend in localFriends?.FRIEND"
                :key="friend.id"
                :info="friend"
              />
              <button
                class="friends-btn__more"
                @click.prevent="loadMoreFriends('FRIEND')"
                v-if="localFriends?.FRIEND.length !== paginations.FRIEND.totalElements || paginations.FRIEND.totalElements === 0"
              >
                {{ translations.friendsShowMore }}
              </button>
            </div>
          </div>
          <!-- Подписчики / поиск реализован -->
          <div v-if="activeTab === 'SUBSCRIBED'" class="friends_group active">
            <h3
              class="friends_group_title friends__title"
            >
              {{ translations.friendsMySubscribe }}
            </h3>

            <div v-if="friendSearch !== null && friendSearch.content.length !== 0">
              <div class="friend__search__resultats">
                <p class="friend__search-title">
                  {{ translations.friendsSearchDescription }} "{{ localFirstName }}":
                </p>
                <button
                  class="friend__search-clear"
                  @click.prevent="resetFriendSearch"
                >
                  {{ translations.friendsSearchResultClear }}
                  <span>✕</span>
                </button>
              </div>

              <search-block
                v-for="user in friendSearch.content"
                :key="user.id"
                :info="user"
              />
            </div>

            <div v-if="friendSearch === null || friendSearch.content.length === 0">
              <friends-block
                friend="friend"
                v-for="friend in localFriends?.SUBSCRIBED"
                :key="friend.id"
                :info="friend"
              />
              <button
                class="friends-btn__more"
                @click.prevent="loadMoreFriends('SUBSCRIBED')"
                v-if="localFriends?.SUBSCRIBED.length !== paginations.SUBSCRIBED.totalElements || paginations.SUBSCRIBED.totalElements === 0"
              >
                {{ translations.friendsShowMore }}
              </button>
            </div>
          </div>
          <!-- Заблокированные пользователи / поиск реализован -->
          <div v-if="activeTab === 'BLOCKED'" class="friends_group active">
            <h3
              class="friends_group_title friends__title"
            >
              {{ translations.friendsBlocked }}
            </h3>

            <div v-if="friendSearch !== null && friendSearch.content.length !== 0">
              <div class="friend__search__resultats">
                <p class="friend__search-title">
                  {{ translations.friendsSearchDescription }} "{{ localFirstName }}":
                </p>
                <button
                  class="friend__search-clear"
                  @click.prevent="resetFriendSearch"
                >
                  {{ translations.friendsSearchResultClear }}
                  <span>✕</span>
                </button>
              </div>

              <search-block
                v-for="user in friendSearch.content"
                :key="user.id"
                :info="user"
              />
            </div>

            <div v-if="friendSearch === null || friendSearch.content.length === 0">
              <friends-block
                friend="friend"
                v-for="friend in localFriends?.BLOCKED"
                :key="friend.id"
                :info="friend"
                :blocked="true"
              />
              <button
                class="friends-btn__more"
                @click.prevent="loadMoreFriends('BLOCKED')"
                v-if="localFriends?.BLOCKED.length !== paginations.BLOCKED.totalElements || paginations.BLOCKED.totalElements === 0"
              >
                {{ translations.friendsShowMore }}
              </button>
            </div>
          </div>
          <!-- Подписан(а) / поиск реализован -->
          <div v-if="activeTab === 'WATCHING'" class="friends_group active">
            <h3
              class="friends_group_title friends__title"
            >
              Подписки
              <!-- {{ translations.friendsMyPesonalSubscribed }} -->
            </h3>

            <div v-if="friendSearch !== null && friendSearch.content.length !== 0">
              <div class="friend__search__resultats">
                <p class="friend__search-title">
                  {{ translations.friendsSearchDescription }} "{{ localFirstName }}":
                </p>
                <button
                  class="friend__search-clear"
                  @click.prevent="resetFriendSearch"
                >
                  {{ translations.friendsSearchResultClear }}
                  <span>✕</span>
                </button>
              </div>

              <search-block
                v-for="user in friendSearch.content"
                :key="user.id"
                :info="user"
              />
            </div>

            <div v-if="friendSearch === null || friendSearch.content.length === 0">
              <friends-block

                v-for="friend in localFriends?.WATCHING"
                :key="friend.id"
                :info="friend"
              />
              <button
                class="friends-btn__more"
                @click.prevent="loadMoreFriends('WATCHING')"
                v-if="localFriends?.WATCHING.length !== paginations.WATCHING.totalElements || paginations.WATCHING.totalElements === 0"
              >
                {{ translations.friendsShowMore }}
              </button>
            </div>
          </div>
          <!-- Нет информации -->
          <div v-if="activeTab === 'no-data-users'" class="friends_group-nodata active">
            <h3
              class="friends_group_title no-data"
            >
              {{ translations.friendsNoResult }}
            </h3>
            <router-link class="friends_group__search" href="#" :to="{ name: 'FriendsFind' }">
              {{ translations.recommendBlockBtn }}
            </router-link>
          </div>
          <!-- Нет информации -->
          <div v-if="activeTab === 'no-data'" class="friends_group-nodata active">
            <h3
              class="friends_group_title no-data"
            >
              {{ translations.friendsNoActiveTab }}
            </h3>
          </div>
        </div>

        <div class="inner-page__aside">
          <recommend-friend />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';
import RecommendFriend from '@/components/RecommendFriend.vue';
import FriendsBlock from '@/components/Friends/Block';
import SearchBlock from '@/components/Friends/BlockSearch'
import SearchIcon from '@/Icons/SearchIcon.vue';
import translations from '@/utils/lang.js';

export default {
  name: 'Friends',
  components: { RecommendFriend, FriendsBlock, SearchIcon, SearchBlock },

  data: () => ({
    firstName: '',
    localFirstName: '',
    localFriends: [],
    searchResult: null,
    statusCodes: [
      'REQUEST_FROM',
      'REQUEST_TO',
      'FRIEND',
      'BLOCKED',
      'SUBSCRIBED',
      'WATCHING',
    ],
    activeTab: 'no-data',
  }),

  computed: {
    ...mapState('profile/friends', ['friends', 'paginations', 'friendSearch']),

    calculateTabSelect() {
      if (this.localFriends.REQUEST_FROM && this.localFriends.REQUEST_FROM.length > 0) {
        return 'REQUEST_FROM'
      } else if (this.localFriends.REQUEST_TO && this.localFriends.REQUEST_TO.length > 0) {
        return 'REQUEST_TO'
      } else if (this.localFriends.FRIEND && this.localFriends.FRIEND.length > 0) {
        return 'FRIEND'
      } else if (this.localFriends.BLOCKED && this.localFriends.BLOCKED.length > 0) {
        return 'BLOCKED'
      } else if (this.localFriends.SUBSCRIBED && this.localFriends.SUBSCRIBED.length > 0) {
        return 'SUBSCRIBED'
      } else if (this.localFriends.WATCHING && this.localFriends.WATCHING.length > 0) {
        return 'WATCHING'
      } else if (this.localFriends && this.localFriends.length === 0) {
        return 'no-data'
      } else {
        return 'no-data-users'
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
    friends() {
      this.localFriends = this.friends;
    },
  },

  mounted() {
    this.statusCodes.forEach((statusCode) => {
      this.apiFriends({ statusCode });
    });
    this.localFriends = this.friends;
  },

  created() {
    this.activeTab = this.calculateTabSelect;
  },

  beforeRouteUpdate(next) {
    this.activeTab = this.calculateTabSelect;
    next();
  },

  methods: {
    ...mapActions('profile/friends', ['apiFriends', 'apiFriendSearch']),

    setActive(tabName) {
      this.activeTab = tabName;
      this.firstName = '';
      this.localFirstName = '';
      this.$store.commit('profile/friends/resetFriendSearch');
    },

    loadMoreFriends(statusCode) {
      this.apiFriends({ statusCode, loadMore: true });
    },

    resetFriendSearch() {
      this.$store.commit('profile/friends/resetFriendSearch');
      this.localFirstName = '';
    },

    searchFriends() {
      this.apiFriendSearch({ firstName: this.firstName, statusCode: this.activeTab })
      .then(response => {
        this.searchResult = response.data;
      })
      this.localFirstName = this.firstName;
      this.firstName = '';
    },
  },
};
</script>

<style scoped lang="stylus">

.friends_group_title
  margin-bottom 15px
  font-weight font-weight-bold
  font-size 30px
  color #000

.friends_group
  display none
  &.active
    display block
  &:not(:last-child)
    margin-bottom 40px
.friends__content
  position relative
  width 100%
  display grid
  grid-template-columns 1fr 430px
  gap 30px
  padding-bottom 30px

  .inner-page__aside
    max-width 100%
    margin-top 54px

.friends__list
  display flex
.inner-page
  &__main
    max-width none



.friends-possible
  margin-top 0

@media (max-width 892px)
  .friends__content
    flex-direction column
    align-items stretch
  .inner-page__aside
    position static
    max-width none
    width 100%
    margin-bottom 40px
    order -1
  .friends__list
    margin-right 0

  .friends_group:not(:last-child)
    margin-bottom 40px
</style>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'

.friends .inner-page__main
  width 100%
.friend__search__resultats
  display flex
  gap 10px
  align-items center
  margin-top 30px
  margin-bottom 20px

.friend__search-clear
  display flex
  align-items center
  gap 10px
  background ui-cl-color-eucalypt
  padding 5px
  color ui-cl-color-white-theme
  border-radius border-super-small
  transition all .2s ease-in-out
  span
    font-size font-size-small-medium
    line-height 0
  @media (any-hover: hover)
    &:hover
      background #2fba6e

.friend__search-title
  font-size 20px
  line-height 0

.friends_group_title.no-data
  font-size font-size-updefault
  font-weight font-weight-light
  text-align center
  margin-bottom 20px

.friends_group-nodata
  display flex
  flex-direction column
  align-items center
  justify-content center

.friends_group__search
  display flex
  justify-content center
  padding 10px 20px
  font-size font-size-updefault
  border 1px solid ui-cl-color-eucalypt
  color ui-cl-color-eucalypt
  border-radius border-small
  transition all .2s ease-in-out
  @media (any-hover: hover)
    &:hover
      background ui-cl-color-eucalypt
      color ui-cl-color-white-theme

.friends-btn__more
  display block
  background transparent
  padding 15px
  font-size font-size-updefault
  user-select none
  border 3px solid ui-cl-color-eucalypt
  color ui-cl-color-eucalypt
  border-radius border-small
  margin 0 auto
  transition all .2s ease-in-out
  @media (any-hover: hover)
    &:hover
      background ui-cl-color-eucalypt
      color ui-cl-color-white-theme
</style>
