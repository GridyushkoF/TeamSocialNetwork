<template>
  <div class="search-users" v-if="users.length > 0">
    <search-block :title="translations.searchUserTitle" id="users">
      <div class="friends__list">
        <block-search
          v-for="user in users"
          :key="user.id"
          :info="user"
          subscribe-button
        />
      </div>

      <pagination
        :count="getUsersPagination.totalElements"
        v-model="page"
        :per-page="size"
      />
    </search-block>
  </div>
  <div class="search-news__nonews" v-else>
    {{ translations.searchUserEmpty }}
  </div>
</template>

<script>
import { mapGetters, mapActions, mapMutations } from 'vuex';
import SearchBlock from '@/components/Search/Block';
import BlockSearch from '@/components/Friends/BlockSearch.vue';
import Pagination from '@/components/Pagination.vue';
import translations from '@/utils/lang.js';

export default {
  name: 'SearchUsers',
  components: { SearchBlock, BlockSearch, Pagination },
  data() {
    return {
      page: 1,
      size: 5,
      total: 20,
    };
  },

  computed: {
    ...mapGetters('global/search', ['getResultByIdSearch', 'getUsersQueryParams', 'getUsersPagination']),

    users() {
      return this.getResultByIdSearch('users');
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
    page() {
      let searchQuery = Object.assign({}, this.getUsersQueryParams, {
        page: this.page - 1,
        size: this.size,
      });
      this.searchUsers(searchQuery);
    },
    getUsersPagination() {
      this.total = this.getUsersPagination.total;
    },
  },

  mounted() {
    let searchQuery = Object.assign({}, this.getUsersQueryParams, {
      page: this.page - 1,
      size: this.size,
    });
    if (this.users.length === 0) {
      this.searchUsers(searchQuery);
    }
  },

  methods: {
    ...mapActions('global/search', ['searchUsers']),
    ...mapMutations('global/search', ['setUsersPagination']),
  },
};
</script>
