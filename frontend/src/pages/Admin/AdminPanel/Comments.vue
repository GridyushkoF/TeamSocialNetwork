<template>
  <div class="admin-panel">
    <h2 class="admin__title">Комментарии ({{ countComments }})</h2>
    <div class="panel-publications admin-panel__content">
      <div class="admin-panel__left">
        <search-input v-on:input-change="handleInputChange" />
        <div class="admin-panel__result">
          <div class="admin-panel__result-block" v-if="filter === 'active'">
            <comments-block admin />
          </div>
          <div class="admin-panel__result-block" v-if="filter === 'blocked'">
            <comments-block admin blocked />
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
import CommentsBlock from '@/components/Comments/Block'
export default {
  name: 'CommentsPanel',
  components: { SearchInput, AdminSidebar, CommentsBlock },

  data: () => ({
    filter: 'all',
    inputValue: '',
    countComments: 0
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
