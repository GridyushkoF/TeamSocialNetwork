<template>
  <div class="add-tags" ref="searchBox">
    <div class="add-tags__form">
      <input
        class="add-tags__input"
        type="text"
        :placeholder="translations.newsTagPlaceholder"
        v-model="tag"
        @input="searchTags"
        @keydown.enter="addTag(tag)"
        v-touppercase="10"
        ref="searchInput"
      />

      <div
        v-if="tag.length > 0"
        class="add-tags__btn"
        @click="addTag(tag)"
      >
        <simple-svg class="accept" :filepath="'/static/img/add.svg'" />
      </div>
    </div>
    <div class="add-tags__block">
      <div class="add-tags__item" v-for="(tag, index) in updateTags" :key="index">
        #{{ tag.name }}
        <span class="add-tags__delete" @click="deleteTag(index)"> &#10005; </span>
      </div>
    </div>
    <transition name="fade">
      <div class="add-tags__search-results fade-in" v-if="searchResults.length > 0 || tag.length === 0">
        <div
          class="add-tags__search-item"
          v-for="(result, index) in searchResults"
          :key="index"
          @click="addTagFromSearch(result)"
        >
          #{{ result.name }}
          <progress-tag v-if="index < 2" />
          <progress-tag v-else :stroke-color="`#D69A02`" />
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import Vue from 'vue';
import axios from 'axios';
import { debounce } from 'lodash';
import ProgressTag from '../../Icons/ProgressTag.vue';
import translations from '@/utils/lang.js';

Vue.directive('touppercase', {
  update (el, binding) {
    const sourceValue = el.value;
	  const maxLength = parseInt(binding.value);
	  el.maxLength = maxLength;

    const newValue = sourceValue
    .toLowerCase() // все вводимые знаки с маленькой буквы.
		.replace(/ /g, '_') // заменяем пробелы на нижние подчёркивания. Подмена автоматический.
    .replace(/[^a-zA-Zа-яА-ЯёЁ0-9_]/g, '') // убираем знаки препиния, кирилица/латиница/0-9 - разрешены.
		.substring(0, maxLength); // ограничиваем колличество вводимых знаков, дублируя ограничение атрибутом.

    if (sourceValue !== newValue) {
      el.value = newValue;
      el.dispatchEvent(new Event('input', { bubbles: true }));
    }
  },
})

export default {
  name: 'AddTags',

  components: {
    ProgressTag
  },

  props: {
    tags: Array,
    allowManualAddition: {
      type: Boolean,
      default: true,
    },
  },

  data: () => ({
    tagsList: [],
    tag: '',
    searchResults: [],
  }),

  computed: {
    updateTags() {
      return this.tags;
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

  mounted() {
    setTimeout(() => {
      this.tagsList = this.tags;
    }, 200);
    document.addEventListener('click', this.handleClickOutside);
  },

  beforeDestroy() {
    document.removeEventListener('click', this.handleClickOutside);
  },

  methods: {
    deleteTag(index) {
      this.tagsList = this.tags.filter((tag) => tag !== this.tags[index]);
      this.$emit('change-tags', this.tagsList);
    },

    searchTagsApi(tag) {
      axios.get(`/tag?name=${tag}`)
        .then(response => {
          this.searchResults = response.data.slice(0, 5);
        })
        .catch(error => console.error(error));
    },

    searchTags: debounce(function() {
      if (this.tag.length < 2) {
        this.searchResults = [];
        return;
      }
      this.searchTagsApi(this.tag);
    }, 1000),

    addTagFromSearch(tag) {
      if (this.tagsList.find((t) => t.name === tag.name)) {
        this.$store.dispatch('global/alert/setAlert', {
          status: 'response',
          text: 'Такой тег уже есть',
        });
        return;
      }
      if (this.tagsList.length >= 10) {
        this.$store.dispatch('global/alert/setAlert', {
          status: 'response',
          text: 'Можно добавить максимум 10 тэгов',
        });
        console.log('Я сработал в методе addTagFromSearch')
        return;
      }
      this.tag = ''; // очищаем поле ввода
      this.tagsList.push(tag);
      this.searchResults = [];
      this.$emit('change-tags', this.tagsList);
      this.$nextTick(() => {
        this.$refs.searchInput.focus();
      });
    },

    addTag() {
      if (!this.allowManualAddition) return;
      if (this.tag.length <= 0) return;
      const existingTag = this.tags.find((tag) => tag.name === this.tag);
      if (existingTag) {
        this.addTagFromSearch(existingTag);
        return;
      }

      if (this.tagsList.some((tag) => tag.name === this.tag)) {
        this.$store.dispatch('global/alert/setAlert', {
          status: 'response',
          text: 'Тэг уже добавлен',
        });
        return;
      }

      if (this.tagsList.length >= 10) {
        this.$store.dispatch('global/alert/setAlert', {
          status: 'response',
          text: 'Можно добавить максимум 10 тэгов',
        });
        return;
      }

      const newTag = {
        name: this.tag,
      };
      console.log('Я сработал в методе addTag()')
      this.tagsList.push(newTag);
      this.tag = '';
      this.$emit('change-tags', this.tagsList);
      this.$nextTick(() => {
        this.$refs.searchInput.focus();
      });
    },

    handleClickOutside(event) {
      const isChild = this.$refs.searchBox.contains(event.target);
      if (!isChild) {
        this.tag = '';
        this.searchResults = [];
      }
    },
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'
.add-tags
  position relative

.add-tags__search-results
  position: absolute
  top 24px
  left 0
  background-color #fbfbfb
  border-radius: 0 0 border-super-small border-super-small
  width 86%
  max-height 250px
  overflow-y auto
  box-shadow 0px 4px 4px rgba(0, 0, 0, 0.15)
  transition all .2s ease-in-out
  z-index 100

.add-tags__search-item
  display flex
  justify-content space-between
  align-items center
  font-family "Open Sans"
  padding 10px
  cursor pointer
  font-size font-size-small
  transition background-color .2s ease-in-out
  @media (any-hover: hover)
    &:hover
      background-color ui-cl-color-white-bright-second

.add-tags__form
  display flex

.add-tags__input
  border-bottom 1px solid rgba(0, 0, 0, 0.12)
  padding-bottom 5px
  font-size font-size-small
  color ui-cl-color-steel-gray
  margin-bottom 15px
  margin-right 20px
  width 130px
  &::placeholder
    color #B0B0BC

.add-tags__btn path
  transition fill .3s

.add-tags__btn
  width 20px
  cursor pointer
  &:hover path
    fill #21a45d

.add-tags__block
  margin 0 -4px 10px
  min-height 32px

.add-tags__item
  display inline-block
  align-items center
  color ui-cl-color-eucalypt
  font-size font-size-small
  background-color #F5F7FB
  padding 5px
  margin 0 5px 10px
  border-radius border-super-small

.add-tags__delete
  margin-left 5px
  color #B0B0BC
  font-size font-size-super-upsmall
  font-weight font-weight-bold
  cursor pointer

.fade-enter-active, .fade-leave-active
  transition opacity .5s

.fade-enter, .fade-leave-to
  opacity: 0


</style>
