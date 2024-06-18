<template>
  <div class="aside-filter">
    <h2 class="aside-filter__title">{{ translations.sidebarSetting }}</h2>

    <ul class="aside-filter__list">
      <li
        class="aside-filter__item"
        v-for="item in list"
        :key="item.component"
        :class="{ active: item.component === value }"
        @click="changeComponent(item)"
      >
        {{ currentTranslations === 'Русский' ? item.text : item.textEng }}
      </li>
    </ul>
  </div>
</template>

<script>
import translations from '@/utils/lang.js';
export default {
  name: 'SettingsSidebar',
  props: {
    value: String,
  },

  data: () => ({
    list: [
      { component: 'settings-main', text: 'Основные', textEng: 'Main' },
      { component: 'settings-push', text: 'Настройка оповещений', textEng: 'Setting up alerts' },
      { component: 'settings-security', text: 'Безопасность', textEng: 'Security' },
      { component: 'settings-delete', text: 'Удалить профиль', textEng: 'Delete profile' },
    ],
  }),

  computed: {
    currentTranslations() {
      return this.$store.state.auth.languages.language.name;
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

  methods: {
    changeComponent(item) {
      this.$emit('change-component', item);
    },
  },
};
</script>
