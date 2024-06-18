<template>
  <div class="admin-panel">
    <h1 class="admin__title">Люди ({{ countUsers }})</h1>
    <div class="admin-panel__content">
      <div class="admin-panel__left">
        <search-input v-on:input-change="handleInputChange" />
        <div class="admin-panel__result">
          <div class="admin-panel__result-block" v-if="filter === 'active'">
            <friends-block admin />
          </div>
          <div class="admin-panel__result-block" v-if="filter === 'blocked'">
            <friends-block admin blocked />
          </div>
        </div>
      </div>
      <div>
        <admin-sidebar v-model="filter" @change-filter="onChangeFilter"/>
      </div>
    </div>
  </div>
</template>

<script>
import SearchInput from '../SearchInput.vue';
import FriendsBlock from '@/components/Friends/Block';
import AdminSidebar from './Sidebar.vue';

export default {
  name: 'UsersPanel',
  components: { SearchInput, FriendsBlock, AdminSidebar },
  data: () => ({
    filter: 'active',
    inputValue: '',
    countUsers: 0
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
