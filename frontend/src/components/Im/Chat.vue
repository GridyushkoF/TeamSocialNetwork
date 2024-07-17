<template>
  <div class="im-chat" v-if="filteredUserInfo && filteredUserInfo.length > 0">
    <div class="im-chat__user">
      <router-link
        class="im-chat__user-pic"
        :to="{
          name: 'ProfileId',
          params: { id: filteredUserInfo && filteredUserInfo[0]?.id },
        }"
      >
        <div class="main-layout__user-pic">
          <img
            v-if="filteredUserInfo && filteredUserInfo[0]?.photo"
            :src="filteredUserInfo && filteredUserInfo[0]?.photo"
            :alt="
              filteredUserInfo && filteredUserInfo[0]?.firstName[0] + ' ' + filteredUserInfo && filteredUserInfo[0]?.firstName[0]
            "
          />

          <div v-else>
            <unknow-user />
          </div>
        </div>
      </router-link>

      <router-link
        class="im-chat__user-name"
        :to="{
          name: 'ProfileId',
          params: { id: filteredUserInfo && filteredUserInfo[0]?.id },
        }"
      >
        {{ filteredUserInfo && filteredUserInfo[0]?.firstName }} {{ filteredUserInfo && filteredUserInfo[0]?.lastName }}
      </router-link>

      <div>
        <span
          class="user-status chat-isonline-lasttime"
          v-if="filteredUserInfo[0]?.lastOnlineTime === null"
        >
          {{ translations.messageStatusLongAgo }}
        </span>
        <span
          class="user-status chat-isonline-isonline-online"
          v-else-if="filteredUserInfo[0]?.isOnline"
        >
          {{ translations.profileInfoStatusOnline }}
        </span>
        <span
          class="user-status chat-isonline-isonline-lasttime"
          v-else
        >
          {{ translations.messageStatusWasOnline }} {{ filteredUserInfo[0]?.lastOnlineTime | moment('from') }}
        </span>
      </div>
    </div>

    <div class="im-chat__infitite_list_wrapper chat-message">
      <virtual-list
        class="im-chat__infitite_list scroll-touch"
        :size="60"
        :keeps="120"
        :data-key="'id'"
        :data-sources="messageDialog"
        :data-component="itemComponent"
        :wrap-class="'im-chat__message'"
        :root-tag="'section'"

        @scroll.passive="onScroll"
        @tobottom="onScrollToBottom"
        ref="vsl"
      >
    </virtual-list>
      <div class="im-chat__loader" slot="header" v-show="fetching">
        <div class="spinner" v-show="!isHistoryEndReached()" />
        <div class="finished" v-show="isHistoryEndReached()">{{ translations.messageHistoryIsFinal }}</div>
      </div>
    </div>

    <form class="im-chat__enter" action="#" @submit.prevent="onSubmitMessage">
      <input
        class="im-chat__enter-input"
        type="text"
        :placeholder="translations.messageInputPlaceholder"
        v-model="mes"
      />
      <button v-if="mes.length > 0" class="im-chat__enter-submit" @click.prevent="onSubmitMessage">
        <submit-icon />
      </button>
    </form>
  </div>
</template>

<script>
import { mapActions, mapGetters, mapMutations } from 'vuex';
import UnknowUser from '../../Icons/UnknowUser.vue';
import axios from 'axios';
import ChatMessage from '@/components/Im/ChatMessage.vue'
import VirtualList from 'vue-virtual-scroll-list';
import translations from '@/utils/lang.js';
import SubmitIcon from '../../Icons/SubmitIcon.vue';

const makeHeader = (msgDate) => {
  return { id: `group-${msgDate}`, stubDate: true, date: msgDate };
};

export default {
  name: 'ImChat',
  components: {
    UnknowUser,
    SubmitIcon,
    VirtualList
  },

  props: {
    info: Object,
    messages: Array,
    online: Boolean,
    userInfo: Array
  },

  data: () => ({
    mes: '',
    isUserViewHistory: false,
    itemComponent: ChatMessage,
    fetching: false,
    lastId: -1,
    infoChatUser: null,
    messageDialog: []
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

    messagesGrouped() {
      let groups = [];
      let headerDate = null;

      for (let i = 0; i < this.messages.length; i++) {
        const msg = this.messages[i];
        const msgDate = new Date(msg.time).toDateString();
        if (msgDate !== headerDate) {
          headerDate = msgDate;
          groups.push(makeHeader(headerDate));
        }
        msg.isSentByMe = msg.authorId === this.getInfo.id;
        msg.id = `message-${i}`; // добавляем уникальный ключ
        groups.push(msg);
      }
      return groups;
    },

    getInfoConversationPartner() {
      return this.info?.conversationPartner1 === this.getInfo?.id ? this.info?.conversationPartner2 :
           this.info?.conversationPartner2 === this.getInfo?.id ? this.info?.conversationPartner1 :
           null;
    },

    filteredUserInfo() {
      return this.userInfo?.filter(user => user.id === this.getInfoConversationPartner);
    }
  },

  watch: {
    messages() {
      if (this.follow) this.setVirtualListToBottom();
    },

    async getInfoConversationPartner() {
      this.getMessageChat()
    }
  },

  async mounted() {
    this.follow = true;
    if (this.follow) this.setVirtualListToBottom();
    await axios.put(`dialogs/${this.info.id}`)
    if (!this.getInfo) {
      await this.apiInfo();
      this.getInfoChat();
    }

    this.$nextTick(() => {
      this.$el.scrollTop = this.$el.scrollHeight;
    });

    await this.$socket.connect();
    this.$socket.subscribe('socket event', (messagePayload) => {
      this.newMessage(messagePayload);
    });
  },

  created() {
    this.follow = true;
    if (this.follow) this.setVirtualListToBottom();
    this.getMessageChat();
  },

  methods: {
    ...mapActions('profile/dialogs', ['postMessage', 'fetchMessages', 'loadOlderMessages', 'markReadedMessages']),
    ...mapGetters('profile/dialogs', ['isHistoryEndReached', 'getDialogs']),
    ...mapMutations('profile/dialogs', ['addOneMessage']),
    ...mapActions('profile/info', ['apiInfo']),

    getInfoChat() {
      const conversationPartnerId = this.info.conversationPartner1 === this.getInfo.id ? this.info.conversationPartner2 :
            this.info.conversationPartner2 === this.getInfo.id ? this.info.conversationPartner1 :
            null;
      console.log(conversationPartnerId)
      const user = this.userInfo.find(user => user.id === conversationPartnerId);
      this.infoChatUser = user;
    },

    onSubmitMessage() {
      if (!this.mes.trim()) return;
      const payload = {
        type: 'MESSAGE',
        recipientId: this.info.conversationPartner2,
        data: {
          time: null,
          conversationPartner1: this.info.conversationPartner1,
          conversationPartner2: this.info.conversationPartner2,
          messageText: this.mes,
          readStatus: null,
          dialogId: this.info.id
        },
      };
      this.addOneMessage(payload.data);
      this.getMessageChat();
      this.$socket.sendMessage(payload);
      this.lastId -= 1;
      this.mes = '';
    },

    newMessage(message) {
      const payload = {
        type: 'MESSAGE',
        recipientId: this.info.conversationPartner2,
        data: {
          time: null,
          conversationPartner1: this.info.conversationPartner1,
          conversationPartner2: this.info.conversationPartner2,
          messageText: message.data.messageText,
          readStatus: null,
          dialogId: this.info.id
        },
      };
      this.addOneMessage(payload.data);
      this.getMessageChat();
      this.lastId -= 1;
      this.mes = '';
    },

    async onScrollToTop() {
      if (this.$refs.vsl) {
        if (!this.isHistoryEndReached()) {
          let [oldest] = this.messagesGrouped;

          this.fetching = true;
          await this.loadOlderMessages();
          this.setVirtualListToOffset(1);

          this.$nextTick(() => {
            let offset = 0;
            for (const groupedMsg of this.messagesGrouped) {
              if (groupedMsg.id === oldest.id) break;
              offset += this.$refs.vsl.getSize(groupedMsg.id);
            }

            this.setVirtualListToOffset(offset);
            this.fetching = false;
          });
        }
      }
    },

    getMessageChat() {
      axios.get(`dialogs/messages?recipientId=${this.getInfoConversationPartner}&page=0&sort=time,asc`)
      .then(response => {
        this.messageDialog = response.data.content
      })
      .catch(error => {
        console.log(error)
      })
    },

    onScroll() {
      this.follow = true;
    },

    onScrollToBottom() {
      this.follow = true;
    },

    setVirtualListToOffset(offset) {
      if (this.$refs.vsl) {
        this.$refs.vsl.scrollToOffset(offset);
      }
    },

    setVirtualListToBottom() {
      setTimeout(() => {
        if (this.$refs.vsl) {
          this.$refs.vsl.scrollToBottom();
        }
      }, 100);
    }
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'

.im-chat
  position relative
  display flex
  flex-direction column
  height 100%

.isonline-online
  color ui-cl-color-white-bright-second
  background-color ui-cl-color-eucalypt

.im-chat__user
  display flex
  align-items center
  height 40px
  padding 30px 20px
  font-size font-size-small
  background-color ui-cl-color-white-bright-second
  border-bottom 1px solid #d2d4d7

  .user-status
    padding 4px
    font-size font-size-super-upsmall
    line-height 100%

.im-chat__user-pic
  width 40px
  height 40px
  border-radius border-half
  overflow hidden
  margin-right 10px
  .main-layout__user-pic
    width 40px
    height 40px

.im-chat__user-name
  font-weight font-weight-bold
  font-size font-size-default
  color ui-cl-color-steel-gray
  margin-right auto

.im-chat__user-status
  color #9EA4AD

  &.online
    color ui-cl-color-eucalypt

.im-chat__infitite_list_wrapper
  padding 20px
  position relative
  overflow-y auto
  flex 1
  &.div:not(:last-child)
    margin-bottom 20px

.im-chat__infitite_list
  position absolute
  top 0
  bottom 0
  left 0
  right 0
  overflow-y auto

.im-chat__message > div
  padding 20px

active
  &:after
    content attr(data-push)
    font-weight font-weight-regular
    font-size font-size-super-small
    width 15px
    height 15px
    color ui-cl-color-white-theme
    background-color #E65151
    border-radius border-half
    display flex
    align-items center
    justify-content center
    position relative
    right 0px
    bottom -7px
    transform translateY(-50%)

.im-chat__enter
  position relative
  display block
  width 100%

.im-chat__enter-input
  width 100%
  background-color ui-cl-color-363636
  padding 0px 80px 0px 40px
  font-size font-size-default
  color ui-cl-color-white-theme
  height 80px

  &::placeholder
    color #B0B0BC

.im-chat__enter-submit
  position absolute
  top 20px
  right 20px
  background-color transparent
  color #bdcdd6
  :hover
    color ui-cl-color-white-theme

.im-chat__enter-submit > svg
  height 40px
  width 40px

.im-chat__loader
  padding 1em

  .finished
    font-size font-size-small
    text-align center
    color #bfbfbf

  .spinner
    font-size font-size-super-upsmall
    margin 0 auto
    text-indent -9999em
    width 25px
    height 25px
    border-radius border-half
    background ui-cl-color-white-theme
    background linear-gradient(to right, ui-cl-color-eucalypt 10%, rgba(255, 255, 255, 0) 42%)
    position relative
    animation load3 1.4s infinite linear
    transform translateZ(0)

  .spinner:before
    width 50%
    height 50%
    background ui-cl-color-eucalypt
    border-radius 100% 0 0 0
    position absolute
    top 0
    left 0
    content ''

  .spinner:after
    background #f8fafd
    width 75%
    height 75%
    border-radius border-half
    content ''
    margin auto
    position absolute
    top 0
    left 0
    bottom 0
    right 0

  @-webkit-keyframes load3
    0%
      transform rotate(0deg)
    100%
      transform rotate(360deg)

  @keyframes load3
    0%
      transform rotate(0deg)
    100%
      transform rotate(360deg)
</style>
