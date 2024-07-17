<template>
  <ul class="admin__aside">
    <li
      class="admin__aside-item"
      v-for="item in list"
      :key="item.component"
      :class="{ active: item.component === value }"
    >
      <a
        href="#"
        @click.prevent="changeComponent(item)"
        :class="{ active: item.component === value }"
      >
        {{ item.text }}
      </a>
    </li>
  </ul>
</template>

<script>
export default {
  name: 'AdminSidebar',
  props: {
    value: String,
  },
  data: () => ({
    list: [
      { component: 'admin-statistics', text: 'Общая' },
      { component: 'admin-posts', text: 'Публикации' },
      { component: 'admin-users', text: 'Пользователи' },
      { component: 'admin-comments', text: 'Комментарии' },
      { component: 'admin-likes', text: 'Лайки' },
    ],
  }),
  methods: {
    changeComponent(item) {
      this.$emit('change-component', item);
    },
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'

.admin
  &__aside
    display flex
    width 355px
    max-height: 307px
    flex-direction column
    background-color ui-cl-color-white-theme
    border-radius border-small
    box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.08)
    overflow hidden
    &-item
      &:not(:last-child)
        border-bottom 1px solid ui-cl-color-e6e6e6
      a
        display block
        padding 20px 25px
        transition all .2s ease-in-out
        @media (any-hover: hover)
          &:hover
            background-color ui-cl-color-gun-powder
            color ui-cl-color-white-theme
        &.active
          background-color ui-cl-color-gun-powder
          color ui-cl-color-white-theme
</style>
