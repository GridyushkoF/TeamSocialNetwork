<template>
  <div class="post-reactions">
    <div class="post-reactions__likes">
      <span
        v-for="(react, index) in localReactionsInfo" :key="index"
        :id="react.reactionType"
        v-on="localReaction === react.reactionType ? { click: deletedReaction } : {}"
        class="post-reactions__selected"
        :class="{
          'selected':react.reactionType == isSelect,
          'added':react.reactionType == isSelect && addedClass == true,
          'deleted':react.reactionType == isSelect && deletedClass == true
        }"
      >
        <img
          :src="`/static/img/post-reactions/${react.reactionType}.gif`"
          :alt="react.reactionType"
          ref="reactionImg"
        />
        <span class="post-reactions__quantity">
          {{ react.count }}
        </span>
      </span>
      <span
        v-if="myReactionStatus"
        @click="deletedReaction"
        class="post-reactions__selected selected"
        :class="{'added':addedClass === true, 'deleted':deletedClass === true}"
      >
        <img
          :src="`/static/img/post-reactions/${myCurrentReaction.reactionType}.gif`"
          :alt="myCurrentReaction.reactionType"
          ref="reactionImg"
        />
        <span class="post-reactions__quantity">
          {{ myCurrentReaction.count }}
        </span>
      </span>
    </div>
    <div class="post-reactions__heart">
      <div class="post-reactions__selected" @mouseover="showReactions = true">
        <like-icon/><b class="like-icon__add">+</b>
      </div>
      <div
        class="post-reactions__reactions"
        @mouseleave="showReactions = false"
      >
        <div
          v-for="(reaction, index) in reactions" :key="index"
          class="post-reactions__reaction"
          @click="addReaction(reaction.type)">
          <img
            :title="reaction.label"
            :src="`/static/img/post-reactions/${reaction.type}.gif`"
            :alt="reaction.label"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import LikeIcon from '../Icons/LikeIcon.vue';
export default {
  components: {
    LikeIcon
  },
  props: {
    active: null,
    reactionsInfo: {
      type: Array,
      default: () => [],
    },
    quantity: {
      type: Number,
      required: true
    },
    reaction: String
  },

  data() {
    return {
      reactions: [
        { type: 'heart', label: 'Нравится' },
        { type: 'funny', label: 'Смешно' },
        { type: 'wow', label: 'Ого!' },
        { type: 'delight', label: 'Восторг' },
        { type: 'sadness', label: 'Печаль' },
        { type: 'malice', label: '&$#%!' },
      ],
      showReactions: false,
      localQuantity: 1,
      localActive: null,
      selectedReaction: null,
      localReaction: this.reaction,

      localReactionsInfo: JSON.parse(JSON.stringify(this.reactionsInfo)),


      myCurrentReaction: {
        reactionType: this.reaction,
        count: 1
      },
      myNewReaction: null,

      prevSelected: null,
      currentSelected: null,

      myReactionStatus: null,
      addedClass: null,
      deletedClass: null
    }
  },

  computed: {
    isSelect() {
      return this.localReaction
    }
  },

  watch: {
    quantity(val) {
      this.localQuantity = val;
    },
    active(val) {
      this.localActive = val;
    },
  },

  mounted() {
    this.localActive = this.active;
  },

  methods: {
    addReaction(reactionType) {
      this.selectedReaction = reactionType;

      this.addedClass = true;

      setTimeout(() => {
        this.addedClass = false;
      }, 300);

      if (this.myCurrentReaction.reactionType !== null && this.myCurrentReaction.reactionType !== '') {
        console.log('1)Уже стоит реакция')

        if (this.localReactionsInfo.filter(type => type.reactionType === this.selectedReaction).length === 0) {
          console.log('1.1)Замена')
          this.myReactionStatus = true;

          for (const reaction of this.localReactionsInfo) {
            if (this.prevSelected === reaction.reactionType) {

              reaction.count -= 1;
              this.prevSelected = null;
            };

            if (this.myCurrentReaction.reactionType === reaction.reactionType) {
              reaction.count -= 1;
              this.localReaction = null;

              if (reaction.count === 0) {
                this.localReactionsInfo.splice(this.localReactionsInfo.indexOf(reaction), 1);
              }
            }
          };

          this.myNewReaction = {
            reactionType: this.selectedReaction,
            count: 1
          };

          this.localReaction= null;

          this.myCurrentReaction = {... this.myNewReaction};
          this.$emit('reaction-added', reactionType);

        } else {
          console.log('1.2)Меняем кол-во')
          this.myReactionStatus = false;
          this.localQuantity = 1;

          for (const reaction of this.localReactionsInfo) {
            if (this.selectedReaction === this.localReaction) return


            if (this.prevSelected === reaction.reactionType) {
              reaction.count -= 1;
            }

            if (this.myCurrentReaction.reactionType === reaction.reactionType) {
              reaction.count -= 1;
              if (reaction.count === 0) {
                this.localReactionsInfo.splice(this.localReactionsInfo.indexOf(reaction), 1);
              }
            }

            setTimeout(() => {
              if (this.selectedReaction === reaction.reactionType) {
                reaction.count += 1;
                this.prevSelected = this.selectedReaction;
                this.localReaction= this.selectedReaction;
              }
            }, 0);
          }
          this.$emit('reaction-added', reactionType);
        }
      } else {
        console.log('2)Нету реакции')
        if (this.localReactionsInfo.filter(type => type.reactionType === this.selectedReaction).length >= 1) {
          console.log('2.1)Меняем кол-во')
          for (const reaction of this.localReactionsInfo) {
            this.currentSelected = this.selectedReaction;
            if (this.prevSelected === reaction.reactionType) {
              reaction.count -= 1;
            };

            setTimeout(() => {
              if (this.currentSelected === reaction.reactionType) {
                reaction.count += 1;
                this.prevSelected = this.currentSelected;
                this.localReaction= this.selectedReaction;
              }
            }, 0);
          };
          this.$emit('reaction-added', reactionType);

        } else {

          console.log('2.2)Добавляем новую')

          this.myReactionStatus = true;

          for (const reaction of this.localReactionsInfo) {
            if (this.prevSelected === reaction.reactionType) {
              reaction.count -= 1;
              this.prevSelected = this.selectedReaction;
            };
          };

          this.myNewReaction = {
            reactionType: this.selectedReaction,
            count: 1
          };

          this.myCurrentReaction = {... this.myNewReaction};
          console.log(reactionType)
          this.$emit('reaction-added', reactionType);
        };
      };
    },

    deletedReaction(reactionType) {
      this.deletedClass = true;
      console.log(this.deletedClass)
      setTimeout(() => {
        for (const reaction of this.localReactionsInfo) {
        if (this.myCurrentReaction.reactionType === reaction.reactionType && this.localQuantity !== null) {
          reaction.count -= 1;
          this.localQuantity = null;
          if (reaction.count === 0) {
            this.localReactionsInfo.splice(this.localReactionsInfo.indexOf(reaction), 1);
          }
          this.myCurrentReaction.reactionType = null;
        }

        if (this.prevSelected === reaction.reactionType) {
          reaction.count -= 1;
          this.prevSelected = null;
        }
      }

      this.localReaction = null;

      if (this.myReactionStatus) {
        this.myReactionStatus = false;
      }

      this.$emit('reaction-deleted', reactionType);
      this.deletedClass = false;
      }, 300);

    },

    getReactionCount(reactionType) {
      const reaction = this.reactionsInfo.find((r) => r.reactionType === reactionType);
      return reaction ? reaction.count : 0;
    },
  },
}
</script>

<style lang="stylus">
@import '../assets/stylus/base/vars.styl'

.post-reactions
  display flex
  align-items center
  cursor pointer
  height 32px
  padding 4px 4px
  transition all .2s ease-in-out
  gap 5px

.post-reactions__label
  font-size 10px

.post-reactions__heart
  position relative
  cursor pointer

.post-reactions__reactions
  position absolute
  bottom -1px
  left -10px
  background-color #fff
  padding 5px
  border-radius 16px
  box-shadow 0px 2px 8px rgba(0,0,0,0.3)
  margin-left 10px
  display flex
  flex-direction column-reverse
  gap 5px
  width 100%
  opacity 0
  visibility: hidden

.post-reactions__heart .post-reactions__reactions
  transition all .3s ease-in-out

.post-reactions__heart:hover .post-reactions__reactions
  visibility: visible
  opacity 1

.post-reactions__reaction
  display flex
  flex-direction column
  align-items center
  cursor pointer
  transition all .2s ease-in-out
  &:hover
    img
      transition all .2s ease-in-out
      transform scale(1.3)

.added
  -webkit-animation: grow .3s;
  animation: grow .3s;
.deleted
  -webkit-animation: shrink .3s;
  animation: shrink .3s;

@-webkit-keyframes grow
  0%
    -webkit-transform: scale(0.0, 0.0)
    transform: scale(0.0, 0.0)
  25%
    -webkit-transform: scale(0.5, 0.5)
    transform: scale(0.5, 0.5)
  50%
    -webkit-transform: scale(1.0, 1.0)
    transform: scale(1.0, 1.0)
  75%
    -webkit-transform: scale(1.3, 1.3)
    transform: scale(1.3, 1.3)
  100%
    -webkit-transform: scale(1.0, 1.0)
    transform: scale(1.0, 1.0)

@keyframes grow
  0%
    -webkit-transform: scale(0.0, 0.0)
    transform: scale(0.0, 0.0)
  25%
    -webkit-transform: scale(0.5, 0.5)
    transform: scale(0.5, 0.5)
  50%
    -webkit-transform: scale(1.0, 1.0)
    transform: scale(1.0, 1.0)
  75%
    -webkit-transform: scale(1.3, 1.3)
    transform: scale(1.3, 1.3)
  100%
    -webkit-transform: scale(1.0, 1.0)
    transform: scale(1.0, 1.0)

@-webkit-keyframes shrink
  0%
    -webkit-transform: scale(1.0, 1.0)
    transform: scale(1.0, 1.0)

  25%
    -webkit-transform: scale(1.3, 1.3)
    transform: scale(1.3, 1.3)

  50%
    -webkit-transform: scale(1.0, 1.0)
    transform: scale(1.0, 1.0)

  75%
    -webkit-transform: scale(0.5, 0.5)
    transform: scale(0.5, 0.5)

  100%
    -webkit-transform: scale(0.0, 0.0)
    transform: scale(0.0, 0.0)

@keyframes shrink
  0%
    -webkit-transform: scale(1.0, 1.0)
    transform: scale(1.0, 1.0)

  25%
    -webkit-transform: scale(1.3, 1.3)
    transform: scale(1.3, 1.3)

  50%
    -webkit-transform: scale(1.0, 1.0)
    transform: scale(1.0, 1.0)

  75%
    -webkit-transform: scale(0.5, 0.5)
    transform: scale(0.5, 0.5)

  100%
    -webkit-transform: scale(0.0, 0.0)
    transform: scale(0.0, 0.0)

.post-reactions__reaction img
  width 40px
  height 30px

.post-reactions__likes
  display flex
  align-items center
  margin-right 20px
  gap 5px

.post-reactions__quantity
  font-size 15px
  font-weight 500
  color #818c99

.post-reactions__selected
  padding 5px 10px
  background-color #F0F2F5
  border-radius 32px
  &:hover
    color ui-cl-color-wild-watermelon
    img
      transition all .2s ease-in-out
      transform scale(1.3)

.post-reactions__selected img
  width 24px
  height 24px

.selected
  background-color #8bc49e
  &:hover
    background-color #21a45d

.selected .post-reactions__quantity
  color white

.post-reactions
  &--wow,
  &--funny,
  &--delight
    background-color #FFF2D6
  &--sadness
    background-color #EDF3FA
  &--malice
    background-color #FDEBE8
  &--heart
    background-color #FFEDED

  .like-icon__add
    color #818c99




</style>
