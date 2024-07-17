<template>
  <div class="news-add" :class="{ open: isOpen }">
    <div class="news-add__mask" v-if="!isOpen" @click.prevent="openForm">
      <template v-if="getInfo">
        <div class="news-add__pic imgDiv" v-if="user">
          <img
            v-if="getInfo.photo"
            class="avatar"
            :src="getInfo.photo"
            :alt="getInfo.firstName[0] + ' ' + getInfo.lastName[0]"
          />

          <div class="avatar" v-else>
            <unknow-user />
          </div>
        </div>
      </template>

      <span class="news-add__placeholder">{{ translations.newsAddPlaceholder }}</span>

      <div class="news-add__block add" @click.prevent="openForm">
        <add-icon />
      </div>
    </div>

    <add-form v-else @submit-complete="closeForm" @close-form="closeForm" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import AddIcon from '@/Icons/AddIcon.vue';
import AddForm from '@/components/News/AddForm';
import UnknowUser from '../../Icons/UnknowUser.vue';
import translations from '@/utils/lang.js';

export default {
  name: 'NewsAdd',
  components: { AddForm, AddIcon, UnknowUser },
  props: {
    user: Boolean,
  },
  data: () => ({
    isOpen: false,
  }),
  computed: {
    ...mapGetters('profile/info', ['getInfo']),
    translations() {
      const lang = this.$store.state.auth.languages.language.name;
      if (lang === 'Русский') {
        return translations.rus;
      } else {
        return translations.eng;
      }
    },
  },
  methods: {
    openForm() {
      this.isOpen = true;
    },
    closeForm() {
      this.isOpen = false;
    },
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'

@media (min-width: 320px) and (max-width: 768px)
  .news-add
    &__mask
      padding 10px 20px
      height unset
    &__text-title
      font-size font-size-updefault
      padding-bottom 10px
</style>

<style lang="stylus" scoped>
.avatar
  display flex
  align-items center
  justify-content center
  width 100%
  height 100%
  object-fit cover

.imgDiv
  background-color #bcdfc7
</style>
