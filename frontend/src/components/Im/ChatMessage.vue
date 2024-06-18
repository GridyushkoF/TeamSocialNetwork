<template>
  <div>
    <!--  Please avoid to use margin between -->
    <!--  messages block or it will glitter on-->
    <!--  scroll-->
    <h5 class="im-chat__message-title" v-if="source.stubDate">
      {{ source.date }}
    </h5>

    <div v-else class="im-chat__message-block" :class="{ me: isSentByMe }">
      <p class="im-chat__message-text">{{ source.messageText }}</p>
      <span class="im-chat__message-time">{{ source.time | moment('YYYY-MM-DD hh:mm') }}</span>
    </div>
  </div>
</template>


<script>
import { mapState, mapGetters } from 'vuex';

export default {
  name: 'InfiniteLoadingItem',
  props: {
    index: { // index of current item
      type: Number
    },
    source: {
      type: Object,
      default() {
        return {};
      },
    },
  },

  computed: {
    ...mapState('profile/info', ['info']),

    messageConversation() {
      return this.source?.conversationPartner1 === this.info?.id ? this.source?.conversationPartner2 :
           this.source?.conversationPartner2 === this.info?.id ? this.source?.conversationPartner1 :
           null;
    },

	 isSentByMe() {
		return this.source?.conversationPartner1 === this.info?.id ? true :
			this.source?.conversationPartner2 === this.info?.id ? false :
			false
	 }
  },

  methods: {
    ...mapGetters('profile/info', ['getInfo']),
  }
};
</script>

<style lang="stylus" scoped>
@import '../../assets/stylus/base/vars.styl'

.im-chat__message-block:not(:last-child) {
  margin-bottom 20px
}
.im-chat__message-day
  &+&
    margin-top 50px

.im-chat__message-title
  color #8A94A4
  font-size font-size-downdefault
  text-align center
  display block
  width 100%

.im-chat__message-block
  display flex
  align-items center

  &+&
    margin-top 20px

  &.me + &
    margin-top 30px

  &.me
    flex-direction row-reverse
    max-width 650px
    margin-left auto

    .im-chat__message-text
      background-color ui-cl-color-eucalypt
      color ui-cl-color-white-theme
      box-shadow 0px 4px 15px #D4E8DD

    .im-chat__message-time
      margin-right 15px
      margin-left 0

.im-chat__message-text
  word-wrap break-word
  background-color ui-cl-color-white-theme
  box-shadow 0px 4px 14px #e1e1e1
  border-radius border-big-radius
  padding 15px 20px
  color ui-cl-color-steel-gray
  font-size font-size-downdefault
  line-height 23px
  overflow-x hidden

.im-chat__message-time
  min-width 105px
  font-size font-size-small
  color #BAC3D0
  margin-left 15px
</style>
