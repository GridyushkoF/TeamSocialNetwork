<template>
  <div class="admin-panel">
    <h2 class="admin__title">Посты ({{ countPosts }})</h2>
    <div class="panel-publications admin-panel__content">
      <div class="admin-panel__left">
        <search-input v-on:input-change="handleInputChange" />
        <div class="admin-panel__result">
          <div class="admin-panel__result-block" v-if="filter === 'active'">
            <news-block admin />
          </div>
          <div class="admin-panel__result-block" v-if="filter === 'blocked'">
            <news-block admin blocked />
          </div>
        </div>
      </div>
      <admin-sidebar v-model="filter" @change-filter="onChangeFilter"/>
    </div>
  </div>
</template>

<script>
import SearchInput from '../SearchInput.vue';
import AdminSidebar from './Sidebar.vue';
import NewsBlock from '@/components/News/Block.vue';

export default {
  name: 'PublicationPanel',
  components: { SearchInput, AdminSidebar, NewsBlock },

  data: () => ({
    filter: 'all',
    inputValue: '',
    countPosts: 0
  }),

  methods: {
    onChangeFilter(id) {
      this.filter = id;
    },

    handleInputChange(value) {
      this.inputValue = value;
    },
  },
}
</script>
