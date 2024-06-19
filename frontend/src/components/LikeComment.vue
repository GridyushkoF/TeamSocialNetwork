<template>
  <div class="like-comment" :class="{ active, fill, 'comment-icon': comment }">
    <template v-if="comment">
      <comment-icon v-if="quantity >= 1" :class="{ 'stroke-active': quantity >= 1 }" />
      <comment-icon v-else-if="quantity === 0" :class="{ 'stroke-null': quantity === 0 }" />

      <span v-if="quantity >= 1" :style="{ 'font-size': fontSize, color: color }">
        {{ quantity }}
      </span>
    </template>

    <div class="like-comment__checkbox" v-else>
      <input type="checkbox" :checked="active" :id="id" @change="onChange" />

      <label :for="id" :class="{ 'like-amount': localQuantity >= 1 }">
        <like-icon />
        <template v-if="localQuantity >= 1">
          {{ localQuantity }}
        </template>
      </label>
    </div>
  </div>
</template>

<script>
import CommentIcon from '../Icons/CommentIcon.vue';
import LikeIcon from '../Icons/LikeIcon.vue';
export default {
  name: 'Like',
  components: { CommentIcon, LikeIcon },
  props: {
    quantity: {
      type: Number,
      required: true
    },

    active: null,
    fill: Boolean,
    width: {
      type: String,
      default: '12px',
    },
    height: {
      type: String,
      default: '12px',
    },
    fontSize: {
      type: String,
      default: '12px',
    },
    color: {
      type: String,
      default: '#aeaebd',
    },
    comment: Boolean,
    id: String,
  },

  data: () => ({
    localQuantity: null,
    localActive: null,
  }),

  watch: {
    quantity(val) {
      this.localQuantity = val;
    },
    active(val) {
      this.localActive = val;
    },
  },

  mounted() {
    this.localQuantity = this.quantity;
    this.localActive = this.active;
  },

  methods: {
    onChange() {
      this.$emit('liked', this.localActive);
      if (this.localActive) this.localQuantity--;
      else this.localQuantity++;
      this.localActive = !this.localActive;
    },
  },
};
</script>

<style lang="stylus">
@import '../assets/stylus/base/vars.styl'

.like-amount
  color #FF3347
  font-weight font-weight-bold
  svg
    color #FF3347

.stroke-active svg path
  fill #1C9252

.comment-icon
  &:hover
    color #000 !important
  span
    color #000 !important
    font-weight font-weight-medium

.yes-comment
  color #000

.like-comment
  display flex
  align-items center
  cursor pointer
  font-size font-size-small
  height 32px
  background-color #F0F2F5
  padding 4px 20px
  border-radius 32px
  transition all .2s ease-in-out
  gap 5px
  &:hover
    color ui-cl-color-wild-watermelon
    background-color ui-cl-color-d2d2d2

.like-comment__checkbox
  input
    width 0.1px
    height 0.1px
    opacity 0
    overflow hidden
    position absolute
    z-index -1

    &:checked
      & + label
        svg
          color ui-cl-color-wild-watermelon

  label
    display flex
    gap 5px
    font-size font-size-small
    align-items center
    cursor pointer
</style>
