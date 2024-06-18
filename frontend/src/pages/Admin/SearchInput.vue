<template>
  <div class="admin-panel__search" v-bind:class="{ active: isActive }">
    <search-icon />
    <input
      class="admin-panel__input"
      type="text" placeholder="Начните набирать, что бы найти..."
      v-model="inputValue" @input="updateValue"
      v-on:focus="isActive = true" v-on:blur="isActive = false"
    >
  </div>
</template>

<script>
import SearchIcon from '@/Icons/SearchIcon.vue';

export default {
  name: 'SearchInput',

  components: { SearchIcon },

  data: () => ({
    filter: 'all',
    inputValue: '',
    isActive: false
  }),

  methods: {
    updateValue() {
      this.$emit('input-change', this.inputValue);
    },
  },
}

</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'

.admin-panel__search .admin-panel__input:hover + *
  background-color lightblue

.admin-panel
  width 100%

  &__content
    display grid
    grid-template-columns 1fr 430px
    gap 20px
    align-items flex-start
    justify-content space-between
  &__search
    display flex
    align-items center
    padding 20px 40px
    background ui-cl-color-white-theme
    transition all .3s ease-in-out
    border-radius border-small
    box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.08);
    &.active
      background ui-cl-color-gun-powder
      input
        color ui-cl-color-white-theme
        &::placeholder
          color ui-cl-color-white-theme
      svg
        stroke #fff
    svg
      margin-right 15px
  &__input
    width 100%
    background transparent

  &__result
    margin-top 30px
    &-block
      display grid
      grid-template-columns repeat(2, 1fr)
      gap 22px
      .friends-block
       margin-bottom 0
</style>
