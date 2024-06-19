<template>
  <div class="profile inner-page" v-if="getUsersInfo">
    <div class="inner-page__main">
      <div class="profile__info">
        <profile-info
          :info="getUsersInfo"
          :blocked="getUsersInfo.isBlocked"
          :friend="getUsersInfo.isFriend"
          :online="getUsersInfo.isOnline"
          :me="myProfile"
          :infoFriend="
            getStatusSub ||
            getStatusFriend ||
            getStatusRequestTo ||
            getStatusRequestFrom ||
            getStatusRequestWatching ||
            getStatusRequestBlocked ||
            getUsersInfo"
        />
      </div>

      <div class="profile__news" id="mypublications">
        <div class="profile__tabs">
          <span class="profile__tab active">
            Публикации {{ getUsersInfo.firstName }} ({{ getWallPagination.totalElements }})
          </span>
        </div>

        <div v-if="getWall && getWall.length > 0" class="profile__news-list">
          <error-block v-if="!loading && error" :message="errorMessage" />

          <news-block v-for="news in filteredWall.posted" :key="news.id" :info="news" />

          <div class="spinner-wrapper" v-if="loading">
            <spinner />
          </div>

          <auto-paginator
            :page="getWallPagination.page"
            :loading="loading"
            :totalPage="getWallPagination.total"
            :action="setThisWall"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ProfileInfo from '@/components/Profile/Info';
import NewsBlock from '@/components/News/Block';
import Spinner from '@/components/Spinner.vue';
import ErrorBlock from '@/components/ErrorBlock.vue';
import AutoPaginator from '@/components/AutoPaginator.vue';
import { mapActions, mapGetters, mapMutations, mapState } from 'vuex';

export default {
  name: 'ProfileId',
  components: { ProfileInfo, NewsBlock, Spinner, ErrorBlock, AutoPaginator },

  data: () => ({
    localFriends: [],
    statusCodes: [
      'REQUEST_FROM',
      'REQUEST_TO',
      'FRIEND',
      'BLOCKED',
      'SUBSCRIBED',
      'WATCHING',
    ],
  }),

  computed: {
    ...mapState('profile/friends', ['friends']),
    ...mapGetters('users/info', ['getUsersInfo', 'getWall', 'getWallPagination']),
    ...mapState('global/status', ['loading', 'error', 'errorMessage']),
    ...mapGetters('profile/info', ['getInfo']),

    myProfile() {
      return this.getInfo?.id === this.getUsersInfo?.id;
    },

    filteredWall() {
      const wall = this.getWall;
      const posted = wall.filter(item => item.type === 'POSTED');
      const queued = wall.filter(item => item.type === 'QUEUED');
      return { posted, queued };
    },

    getStatusSub() {
      let friends = this.localFriends?.SUBSCRIBED;
      if (friends === undefined) return;
      let friend;
      friends.forEach(i => {
        if (i.friendId === this.getUsersInfo.id) {
          friend = i;
        };
      });
      return friend;
    },

    getStatusFriend() {
      let friends = this.localFriends?.FRIEND;
      if (friends === undefined) return;
      let friend;
      friends.forEach(i => {
        if (i.friendId === this.getUsersInfo.id) {
          friend = i;
        };
      });
      return friend;
    },

    getStatusRequestTo() {
      let friends = this.localFriends?.REQUEST_TO;
      if (friends === undefined) return;
      let friend;
      friends.forEach(i => {
        if (i.friendId === this.getUsersInfo.id) {
          friend = i;
        };
      });
      return friend;
    },

    getStatusRequestFrom() {
      let friends = this.localFriends?.REQUEST_FROM;
      if (friends === undefined) return;
      let friend;
      friends.forEach(i => {
        if (i.friendId === this.getUsersInfo.id) {
          friend = i;
        };
      });
      return friend;
    },

    getStatusRequestWatching() {
      let friends = this.localFriends?.WATCHING;
      if (friends === undefined) return;
      let friend;
      friends.forEach(i => {
        if (i.friendId === this.getUsersInfo.id) {
          friend = i;
        };
      });
      return friend;
    },

    getStatusRequestBlocked() {
      let friends = this.localFriends?.BLOCKED;
      if (friends === undefined) return;
      let friend;
      friends.forEach(i => {
        if (i.friendId === this.getUsersInfo.id) {
          friend = i;
        };
      });
      return friend;
    }
  },

  watch: {
    friends() {
      this.localFriends = this.friends;
    },
  },

  mounted() {
    window.scroll(0, 0);
    this.statusCodes.forEach((statusCode) => {
      this.apiFriends({ statusCode });
    });
    this.localFriends = this.friends;
  },

  async created() {
    this.clearWall();
    this.clearPagination();
    await this.userInfoId(this.$route.params.id);
  },
  methods: {
    ...mapActions('users/info', ['userInfoId', 'apiWall']),
    ...mapMutations('users/info', ['clearWall', 'clearPagination']),
    ...mapActions('profile/friends', ['apiFriends', 'apiFriendSearch']),

    setThisWall({ page }) {
      return this.apiWall({
        accountIds: this.getUsersInfo.id,
        author: this.getUsersInfo,
        page,
      });
    },
  },
};
</script>
