<template>
  <div class="im">
    <div class="im__dialogs" v-if="dialogs.length !== 0">
      <div
        class="im-dialog"
        v-for="dialog in dialogs"
        :key="dialog.id"
        @click="clickOnDialog(dialog)"
        :class="{
          active: dialog.conversationPartner1 !== info.id
          ? activeDialogId === dialog.conversationPartner1
          : activeDialogId === dialog.conversationPartner2
        }"
      >
        <a
          class="im-dailog__pic"
          href="#"
        >
          <div class="main-layout__user-pic">
            <img
              v-if="dialog.conversationPartner1 === info.id
                ? users?.find(user => user.id === dialog.conversationPartner2)?.photo
                : dialog.conversationPartner2 === info.id
                  ? users?.find(user => user.id === dialog.conversationPartner1)?.photo
                  : null"
              :src="dialog.conversationPartner1 === info.id
                ? users?.find(user => user.id === dialog.conversationPartner2)?.photo
                : dialog.conversationPartner2 === info.id
                  ? users?.find(user => user.id === dialog.conversationPartner1)?.photo
                  : null"
              alt="Аватар"
            />

            <div v-else>
              <unknow-user />
            </div>
          </div>
        </a>

        <div class="im-dialog-contents">
          <div class="im-dialog__info" v-if="users">
            <a class="im-dialog__name" href="#">
              {{
                dialog.conversationPartner1 === info.id
                  ? (users?.find(user => user.id === dialog.conversationPartner2)?.firstName || '...') +
                  ' ' + (users.find(user => user.id === dialog.conversationPartner2)?.lastName || '...')
                  : dialog.conversationPartner2 === info.id
                    ? (users?.find(user => user.id === dialog.conversationPartner1)?.firstName || '...') +
                     ' ' + (users.find(user => user.id === dialog.conversationPartner1)?.lastName || '...')
                    : null
              }}
            </a>
              <div v-for="(message, index) in dialog.lastMessage" :key="index">
                <span
                  v-if="(index + 1) === dialog.unreadCount && message.conversationPartner1 !== info.id && message.readStatus === 'SENT'"
                  :data-push="message.conversationPartner1 !== info.id && message.readStatus === 'SENT' ? index + 1 : null"
                  :class="{'im-dialog__unread-count--count': dialog.unreadCount > 0}"
                >
                </span>
              </div>
          </div>
          <div class="im-dialog__content">
            <p class="im-dialog__last" v-if="dialog.lastMessage">
              <span v-if="dialog.lastMessage">{{ dialog.lastMessage && dialog.lastMessage[0]?.messageText }}</span>
              <span class="im-dialog__last-time">{{ dialog.lastMessage && dialog.lastMessage[0]?.time | moment('from') }}</span>
            </p>
          </div>
          <!-- <span class="im-dialog__push" v-if="push > 0">{{ push }}</span> -->
        </div>
      </div>
    </div>

    <div class="im__chat" v-if="activeDialog && messagesLoaded">
      <im-chat :user-info="users" :info="activeDialog" :messages="messages" />
    </div>

    <div v-else class="no-dialog">{{ translations.messageDialogNotSelected }}</div>
  </div>
</template>

<script>
import { mapActions, mapState, mapMutations, mapGetters } from 'vuex';
// import ImDialog from '@/components/Im/Dialog';
import UnknowUser from '../../Icons/UnknowUser.vue';
import ImChat from '@/components/Im/Chat';
import dialogsApi from '@/requests/dialogs';
import translations from '@/utils/lang.js';

export default {
  name: 'Im',
  components: { ImChat, UnknowUser },
  props: {
    activeDialogId: {
      type: String,
      required: false,
    },
  },

  data() {
    return {
      activeDialog: null,
      messagesLoaded: false,
    };
  },

  computed: {
    ...mapState('profile/dialogs', ['dialogs', 'messages', 'newMessage']),
    ...mapGetters('global/search', ['getResultByIdSearch', 'getUsersQueryParams']),
    ...mapState('profile/info', ['info']),

    translations() {
      const lang = this.$store.state.auth.languages.language.name;
      if (lang === 'Русский') {
        return translations.rus;
      } else {
        return translations.eng;
      }
    },

    users() {
      return this.getResultByIdSearch('users');
    },

    currentActiveDialogId() {
      return this.$route.params.activeDialogId;
    },

    conversationPartners() {
      return this.dialogs.filter(dialog => {
        return dialog.conversationPartner1 === this.info.id || dialog.conversationPartner2 === this.info.id;
      }).map(dialog => {
        return dialog.conversationPartner1 === this.info.id ? dialog.conversationPartner2 : dialog.conversationPartner1;
      });
    },
  },

  watch: {
    activeDialogId: {
      immediate: true,
      async handler(value) {
        if (value) {
          await this.newDialogs(this.currentActiveDialogId);
        }
        if (value) {
          this.messagesLoaded = false;
          await this.fetchMessages(this.currentActiveDialogId);
          this.messagesLoaded = true;
          const newActiveDialog = this.fetchDialogs().length ? this.fetchDialogs().filter((d) => d.id === this.currentActiveDialogId) : [];
          if (newActiveDialog.length > 0) {
            [this.activeDialog] = newActiveDialog;
            this.activeDialog.unreadCount = 0;
          } else {
            const response = await dialogsApi.newDialogs(this.currentActiveDialogId);
            const dialogData = response.data;
            this.activeDialog = this.generateNewDialog(dialogData)
            console.log(dialogData);
          }
        }
      },
    },

    newMessage: {
      handler(message) {
        if (this.activeDialogId && message.conversationPartner1 === this.activeDialogId) {
          message.isSentByMe = false;
          this.addOneMessage(message);
          this.markReadedMessages(message.conversationPartner1);
        }
      },
    },
  },

  created() {
    console.log(this.activeDialogId)
  },


  beforeMount() {
    setTimeout(() => {
      this.onSearchUsers();
    }, 1000)
  },

  methods: {
    ...mapActions('profile/dialogs', ['newDialogs','fetchDialogs', 'fetchMessages', 'markReadedMessages']),
    ...mapActions('global/search', ['searchUsers']),
    ...mapMutations('profile/dialogs', [
      'addOneMessage',
      'setUnreadedMessages',
      'setNewMessage',
      'setActiveDialogId',
    ]),
    ...mapMutations('profile/dialogs', ['setDialogs']),
    ...mapGetters('users/info', ['getUsersInfo']),
    ...mapGetters('profile/dialogs', ['getDialogs']),
    ...mapGetters('profile/info', ['getInfo']),

    onSearchUsers() {
      const searchQuery = Object.assign({}, this.getUsersQueryParams, {
        ids: this.conversationPartners,
      });
      this.searchUsers({ payload: searchQuery });
    },

    generateNewDialog(dialogData) {
      return {
        id: dialogData.id,
        unreadCount: dialogData.unreadCount,
        conversationPartner1: this.info.id,
        conversationPartner2: this.currentActiveDialogId,
        lastMessage: {
          time: dialogData.lastMessage && dialogData.lastMessage[0]?.time,
          messageText: dialogData.lastMessage && dialogData.lastMessage[0]?.messageText,
          authorId: dialogData.lastMessage && dialogData.lastMessage[0]?.authorId,
        },
      };
    },

    countPush() {
      // return unread > 0 ? unread : null;
    },

    clickOnDialog(dialog) {
      const partnerId = dialog.conversationPartner1 !== this.info.id ? dialog.conversationPartner1 : dialog.conversationPartner2;
      console.log(partnerId)
      this.$router.push({
        name: 'ImChat',
        params: { activeDialogId: partnerId },
      });
      // this.apiUnreadedMessages();
    }

  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'


.im-dialog__last
  font-size font-size-super-medium-small
  display flex
  align-items center
  gap 5px
  &-time
    color #979797

.im-dialog__info
  margin-bottom 5px

.im-dialog
  display flex
  align-items center
  padding 15px 10px
  cursor pointer
  border-radius border-super-small
  border 1px solid #b9b9b9
  background-color #c9c9c9
  transition all .2s ease-in-out
  &:not(:last-child)
    margin-bottom 10px
  @media (any-hover: hover)
    &:hover
      background-color #c3c3c3
  &.active
    background-color #e1e1e1
    outline 3px solid #21a45d;

.im-dialog__name
  margin-right 5px

.im-dialog-online
  font-size font-size-super-upsmall
  background-color ui-cl-color-gun-powder
  border-radius 3px
  color ui-cl-color-white-theme
  padding 3px

.no-dialog
  display flex
  width 100%
  height 100%
  justify-content:center
  align-items center
  color #666

.im
  display flex
  width 100%
  height calc(100vh - 175px)
  margin-bottom 30px

.im__dialogs
  width 100%
  max-width 35%
  overflow-y auto
  max-height 100%
  padding 15px
  height 100%
  border-radius 10px 0 0 10px
  background-color #d2d4d7

.im__chat
  width 100%
  flex auto
  height 100%
  background-color ui-cl-color-white-theme
  border-radius 0 10px 10px 0
  overflow hidden

.im-dialog__info
  display flex
  align-items center
  justify-content start


.im-dialog__unread-count
  &--count
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
</style>
