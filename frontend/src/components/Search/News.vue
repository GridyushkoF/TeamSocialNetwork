<template>
  <div class="search-news" v-if="news.length > 0">
    <div>
      <search-block :title="translations.searchNewsTitle" id="news">
        <news-block v-for="n in filteredWall.posted" :key="n.id" :info="n" :queued="false" />
      </search-block>
    </div>
    <pagination
      v-if="getNewsPagination.totalElements > 5"
      :count="getNewsPagination.totalElements"
      v-model="page"
      :per-page="size"
    />
  </div>
  <div class="search-news__nonews" v-else>
    {{ translations.searchNewsEmpty }}
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import SearchBlock from '@/components/Search/Block';
import NewsBlock from '@/components/News/Block';
import Pagination from '@/components/Pagination.vue';
import translations from '@/utils/lang.js';

export default {
  name: 'SearchNews',
  components: { SearchBlock, NewsBlock, Pagination },

  data() {
    return {
      page: 1,
      size: 5,
      total: 20,
    };
  },

  computed: {
    ...mapGetters('global/search', ['getResultByIdSearch', 'getNewsQueryParams', 'getNewsPagination']),
    news() {
      return this.getResultByIdSearch('news');
    },

    translations() {
      const lang = this.$store.state.auth.languages.language.name;
      if (lang === 'Русский') {
        return translations.rus;
      } else {
        return translations.eng;
      }
    },

    filteredWall() {
      const wall = this.news;
      const posted = wall.filter(item => item.type === 'POSTED');
      const queued = wall.filter(item => item.type === 'QUEUED');
      return { posted, queued };
    }
  },

  watch: {
    page() {
      let searchQuery = Object.assign({}, this.getNewsQueryParams, {
        page: this.page - 1,
        size: this.size,
      });
      this.searchNews(searchQuery);
    },

    getNewsPagination() {
      // this.page = this.getNewsPagination.page;
      // this.perPage = this.getNewsPagination.perPage;
      this.total = this.getNewsPagination.total;
    },
  },

  mounted() {
    const { tags } = this.$route.query;
    let searchQuery = Object.assign({}, this.getNewsQueryParams, {
      page: this.page - 1,
      size: this.size,
    });
    if (tags) searchQuery.tags = tags;
    if (this.news.length === 0) {
      this.searchNews(searchQuery);
    }
  },

  methods: {
    ...mapActions('global/search', ['searchNews']),
  },
};
</script>
