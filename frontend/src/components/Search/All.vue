<template>
  <div class="search-all">
    <search-block :title="translations.searchUserTitle" id="users" all="all">
      <div class="friends__list">
        <friends-block v-for="user in users" :key="user.id" :info="user" subscribeButton />
      </div>
    </search-block>

    <search-block :title="translations.searchNewsTitle" id="news" all="all">
      <news-block v-for="n in news" :key="n.id" :info="n" />
    </search-block>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import SearchBlock from '@/components/Search/Block';
import FriendsBlock from '@/components/Friends/Block';
import NewsBlock from '@/components/News/Block';
import translations from '@/utils/lang.js';

export default {
  name: 'SearchAll',
  components: { SearchBlock, FriendsBlock, NewsBlock },

  computed: {
    ...mapGetters('global/search', ['getResultById']),

    translations() {
      const lang = this.$store.state.auth.languages.language.name;
      if (lang === 'Русский') {
        return translations.rus;
      } else {
        return translations.eng;
      }
    },

    news() {
      return this.getResultById('news');
    },

    users() {
      return this.getResultById('users');
    },
  },
};
</script>
