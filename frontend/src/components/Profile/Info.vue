<template>
  <div>
    <div class="profile-info" v-if="info">
      <div class="profile-info__overlay" v-if="showEmojiStatus" @click="showEmojiStatus = false"></div>
      <transition name="fade">
        <div class="profile-info__emoji" v-if="showEmojiStatus" v-click-outside="closeEmojiList">
          <div v-if="info.emojiStatus === '0'" class="profile-info__emoji-item">
            <img src="/static/img/user/status_guru.png" />
            <span>{{ info.firstName }} {{ translations.profileEmojiStatus1 }}</span>
            <p>
              {{ translations.profileEmojiStatusDisclamer1 }}
            </p>
            <ul class="profile-info__emoji__list" v-if="usersByEmojiStatus[0]">
              <li class="profile-info__emoji__item" v-for="(user, index) in usersByEmojiStatus[0]" :key="user.id">
                <div class="profile-info__emoji-img" v-if="index < 3">
                  <img :title="user.firstName + ' ' + user.lastName" v-if="user.photo" :src="user.photo" />
                  <div :title="user.firstName + ' ' + user.lastName" v-else>
                    <unknow-user />
                  </div>
                </div>
              </li>
              <li class="profile-info__emoji-img img-emoji__more" v-if="usersByEmojiStatus[0].length > 3">+{{ usersByEmojiStatus[0].length - 3 }}</li>
            </ul>
            <span
              class="profile-info__emoji-quantity"
              v-if="usersByEmojiStatus[0]"
            >
              <div class="profile-info__emoji-quantity-all">
                {{ usersByEmojiStatus[0].length }}
                <span v-if="usersByEmojiStatus[0].length === 1">человек</span>
                <span v-else-if="usersByEmojiStatus[0].length > 1 && usersByEmojiStatus[0].length < 5">человека</span>
                <span v-else>человек</span>
                <span v-if="usersByEmojiStatus[0].length === 1"> установил</span>
                <span v-else> установили</span>
                статус
              </div>
            </span>
          </div>
          <div v-else-if="info.emojiStatus === '1'" class="profile-info__emoji-item">
            <img src="/static/img/user/status_new.png" />
            <span>{{ info.firstName }} {{ translations.profileEmojiStatus2 }}</span>
            <p>
              {{ translations.profileEmojiStatusDisclamer2 }}
            </p>
            <ul class="profile-info__emoji__list" v-if="usersByEmojiStatus[1]">
              <li class="profile-info__emoji__item" v-for="(user, index) in usersByEmojiStatus[1]" :key="user.id">
                <div class="profile-info__emoji-img" v-if="index < 3">
                  <img :title="user.firstName + ' ' + user.lastName" v-if="user.photo" :src="user.photo" />
                  <div :title="user.firstName + ' ' + user.lastName" v-else>
                    <unknow-user />
                  </div>
                </div>
              </li>
              <li class="profile-info__emoji-img img-emoji__more" v-if="usersByEmojiStatus[1].length > 3">+{{ usersByEmojiStatus[1].length - 3 }}</li>
            </ul>
            <span
              class="profile-info__emoji-quantity"
              v-if="usersByEmojiStatus[1]"
            >
              <div class="profile-info__emoji-quantity-all">
                {{ usersByEmojiStatus[1].length }}
                <span v-if="usersByEmojiStatus[1].length === 1">человек</span>
                <span v-else-if="usersByEmojiStatus[1].length > 1 && usersByEmojiStatus[1].length < 5">человека</span>
                <span v-else>человек</span>
                <span v-if="usersByEmojiStatus[1].length === 1"> установил</span>
                <span v-else> установили</span>
                статус
              </div>
            </span>
          </div>
          <div v-else-if="info.emojiStatus === '2'" class="profile-info__emoji-item">
            <img src="/static/img/user/status_escp.png" />
            <span>{{ info.firstName }} {{ translations.profileEmojiStatus3 }}</span>
            <p>
              {{ translations.profileEmojiStatusDisclamer3 }}
            </p>
            <ul class="profile-info__emoji__list" v-if="usersByEmojiStatus[2]">
              <li class="profile-info__emoji__item" v-for="(user, index) in usersByEmojiStatus[2]" :key="user.id">
                <div class="profile-info__emoji-img" v-if="index < 3">
                  <img :title="user.firstName + ' ' + user.lastName" v-if="user.photo" :src="user.photo" />
                  <div :title="user.firstName + ' ' + user.lastName" v-else>
                    <unknow-user />
                  </div>
                </div>
              </li>
              <li class="profile-info__emoji-img img-emoji__more" v-if="usersByEmojiStatus[2].length > 3">+{{ usersByEmojiStatus[2].length - 3 }}</li>
            </ul>
            <span
              class="profile-info__emoji-quantity"
              v-if="usersByEmojiStatus[2]"
            >
              <div class="profile-info__emoji-quantity-all">
                {{ usersByEmojiStatus[2].length }}
                <span v-if="usersByEmojiStatus[2].length === 1">человек</span>
                <span v-else-if="usersByEmojiStatus[2].length > 1 && usersByEmojiStatus[2].length < 5">человека</span>
                <span v-else>человек</span>
                <span v-if="usersByEmojiStatus[2].length === 1"> установил</span>
                <span v-else> установили</span>
                статус
              </div>
            </span>
          </div>
          <div v-else-if="info.emojiStatus === '3'" class="profile-info__emoji-item">
            <img src="/static/img/user/status_teacher.png" />
            <span>{{ info.firstName }} {{ translations.profileEmojiStatus4 }}</span>
            <p>
              {{ translations.profileEmojiStatusDisclamer4 }}
            </p>
            <ul class="profile-info__emoji__list" v-if="usersByEmojiStatus[3]">
              <li class="profile-info__emoji__item" v-for="(user, index) in usersByEmojiStatus[3]" :key="user.id">
                <div class="profile-info__emoji-img" v-if="index < 3">
                  <img :title="user.firstName + ' ' + user.lastName" v-if="user.photo" :src="user.photo" />
                  <div :title="user.firstName + ' ' + user.lastName" v-else>
                    <unknow-user />
                  </div>
                </div>
              </li>
              <li class="profile-info__emoji-img img-emoji__more" v-if="usersByEmojiStatus[3].length > 3">+{{ usersByEmojiStatus[3].length - 3 }}</li>
            </ul>
            <span
              class="profile-info__emoji-quantity"
              v-if="usersByEmojiStatus[3]"
            >
              <div class="profile-info__emoji-quantity-all">
                {{ usersByEmojiStatus[3].length }}
                <span v-if="usersByEmojiStatus[3].length === 1">человек</span>
                <span v-else-if="usersByEmojiStatus[3].length > 1 && usersByEmojiStatus[3].length < 5">человека</span>
                <span v-else>человек</span>
                <span v-if="usersByEmojiStatus[3].length === 1"> установил</span>
                <span v-else> установили</span>
                статус
              </div>
            </span>
          </div>
          <div v-else-if="info.emojiStatus === '4'" class="profile-info__emoji-item">
            <img src="/static/img/user/status_student.png" />
            <span>{{ info.firstName }} {{ translations.profileEmojiStatus5 }}</span>
            <p>
              {{ translations.profileEmojiStatusDisclamer5 }}
            </p>
            <ul class="profile-info__emoji__list" v-if="usersByEmojiStatus[4]">
              <li class="profile-info__emoji__item" v-for="(user, index) in usersByEmojiStatus[4]" :key="user.id">
                <div class="profile-info__emoji-img" v-if="index < 3">
                  <img :title="user.firstName + ' ' + user.lastName" v-if="user.photo" :src="user.photo" />
                  <div :title="user.firstName + ' ' + user.lastName" v-else>
                    <unknow-user />
                  </div>
                </div>
              </li>
              <li class="profile-info__emoji-img img-emoji__more" v-if="usersByEmojiStatus[4].length > 3">+{{ usersByEmojiStatus[5].length - 3 }}</li>
            </ul>
            <span
              class="profile-info__emoji-quantity"
              v-if="usersByEmojiStatus[4]"
            >
              <div class="profile-info__emoji-quantity-all">
                {{ usersByEmojiStatus[4].length }}
                <span v-if="usersByEmojiStatus[4].length === 1">человек</span>
                <span v-else-if="usersByEmojiStatus[4].length > 1 && usersByEmojiStatus[4].length < 5">человека</span>
                <span v-else>человек</span>
                <span v-if="usersByEmojiStatus[4].length === 1"> установил</span>
                <span v-else> установили</span>
                статус
              </div>
            </span>
          </div>
          <div v-else-if="info.emojiStatus === '5'" class="profile-info__emoji-item">
            <img src="/static/img/user/status_love.png" />
            <span>{{ info.firstName }} {{ translations.profileEmojiStatus6 }}</span>
            <p>
              {{ translations.profileEmojiStatusDisclamer6 }}
            </p>
            <ul class="profile-info__emoji__list" v-if="usersByEmojiStatus[5]">
              <li class="profile-info__emoji__item" v-for="(user, index) in usersByEmojiStatus[5]" :key="user.id">
                <div class="profile-info__emoji-img" v-if="index < 3">
                  <img :title="user.firstName + ' ' + user.lastName" v-if="user.photo" :src="user.photo" />
                  <div :title="user.firstName + ' ' + user.lastName" v-else>
                    <unknow-user />
                  </div>
                </div>
              </li>
              <li class="profile-info__emoji-img img-emoji__more" v-if="usersByEmojiStatus[5].length > 3">+{{ usersByEmojiStatus[5].length - 3 }}</li>
            </ul>
            <span
              class="profile-info__emoji-quantity"
              v-if="usersByEmojiStatus[5]"
            >
              <div class="profile-info__emoji-quantity-all">
                {{ usersByEmojiStatus[5].length }}
                <span v-if="usersByEmojiStatus[5].length === 1">человек</span>
                <span v-else-if="usersByEmojiStatus[5].length > 1 && usersByEmojiStatus[5].length < 5">человека</span>
                <span v-else>человек</span>
                <span v-if="usersByEmojiStatus[5].length === 1"> установил</span>
                <span v-else> установили</span>
                статус
              </div>
            </span>
          </div>
          <router-link
            class="profile-info__emoji-set"
            tag="button"
            :to="{ name: 'Settings', params: { showEmojiPicker: true } }"
          >
            {{ translations.settingsSetEmojiStatus }}
          </router-link>
        </div>
      </transition>
      <div
        class="profile-info__background"
        :style="{ 'background-image': `url(${info.profileCover ? info.profileCover : '/static/img/no_cover.svg'})` }"
      >
        <div class="qr">
          <button @click="togglePopup" class="profile-info__shared-btn">
            <shared-account />
          </button>
          <transition name="fade">
            <div v-if="showPopup" class="profile-info__share-popup" v-click-outside="closePopup">
              {{ translations.profileSharedAccTitle }}
              <div class="qr-code" v-html="qrCode"></div>
              <div class="profile-info__share-link">
                <input
                  class="profile-info__link-profile"
                  ref="text" @click="copyText"
                  :value="currentUrl"
                />
                <button class="profile-info__link-btn" @click="copyToClipboard">
                  <span v-if="!copied"><copied-icon /></span>
                  <span v-else-if="copied"><done-copied /></span>
                </button>
              </div>
            </div>
          </transition>
        </div>
      </div>
      <div class="profile-info__bottom">
        <div class="profile-info__left">
          <a
            href="#"
            class="profile-info__link"
            @click.prevent="scrollToPosts"
          >
            <span v-if="me">{{ translations.profileMyPublishText }}</span>
            <span v-else>{{ translations.profileMyPublishNoMe }}</span>
          </a>
          <router-link class="profile-info__link" v-if="me" :to="{ name: 'Friends' }">
            {{ translations.sidebarFriend }}
          </router-link>
        </div>
        <transition name="fade">
          <div v-if="showAvatar" class="profile-info__avatar-container" @click="showAvatar = false">
            <div class="profile-info__avatar-background">
              <img :src="info.photo" class="profile-info__avatar-loupe">
            </div>
          </div>
        </transition>
        <div class="profile-info__image">
          <div
            class="profile-info__avatar"
            :class="{ offline: !online && !me }"
          >
            <img
              v-if="info.photo"
              :src="info.photo"
              :alt="info.firstName[0] + ' ' + info.lastName[0]"
              @click="showAvatar = true"
              @mouseover="showLoupe = true"
              @mouseleave="showLoupe = false"
            />
            <img v-else src="/static/img/avatar.png" alt="avatar placeholder" />
            <transition name="fade">
              <div v-if="showLoupe" class="profile-info__avatar-zoom">
                <plus-icon />
              </div>
            </transition>
          </div>
          <div class="profile-info__names">
            <div class="profile-info__name">
              <strong>{{ info.firstName + ' ' + info.lastName }}</strong>
              <div class="profile-info__emoji-name" @click="toggleEmojiStatus">
                <div v-if="info.emojiStatus === '0'">
                  <img class="profile-info__emoji-status" src="/static/img/user/status_guru.png" />
                </div>
                <div v-else-if="info.emojiStatus === '1'">
                  <img class="profile-info__emoji-status" src="/static/img/user/status_new.png" />
                </div>
                <div v-else-if="info.emojiStatus === '2'">
                  <img class="profile-info__emoji-status" src="/static/img/user/status_escp.png" />
                </div>
                <div v-else-if="info.emojiStatus === '3'">
                  <img class="profile-info__emoji-status" src="/static/img/user/status_teacher.png" />
                </div>
                <div v-else-if="info.emojiStatus === '4'">
                  <img class="profile-info__emoji-status" src="/static/img/user/status_student.png" />
                </div>
                <div v-else-if="info.emojiStatus === '5'">
                  <img class="profile-info__emoji-status" src="/static/img/user/status_love.png" />
                </div>
              </div>
            </div>
            <span class="profile-info__cities" v-if="residenceText">{{ residenceText }}</span>
            <span class="profile-info__val" v-else>{{ translations.profileNotFilled }}</span>
            <div class="profile-info__status">
              <span class="user-status">
                <span class="isonline-lasttime" v-if="info.lastOnlineTime === null">был(а) в сети давно</span>
                <span class="isonline-online" v-else-if="info.isOnline">{{ translations.profileInfoStatusOnline }}</span>
                <span class="isonline-lasttime" v-else>Был(а) в сети {{ info.lastOnlineTime | moment('from') }}</span>
              </span>
            </div>
          </div>
        </div>
        <div class="profile-info__right">
          <router-link class="profile-info__link" v-if="me" :to="{ name: 'Settings' }">
            {{ translations.profileEditingText }}
          </router-link>
          <button class="profile-info__showmore" v-if="!me" @click.prevent="actionsShow">
            {{ translations.profileActionsUser }} <arrow-buttom />
          </button>
          <transition name="fade">
            <div v-if="showActions" class="profile-info__actions" v-click-outside="closeActions">
              <!-- Подписка/отписка/принимаем запрос -->
              <a
                v-if="infoFriend.statusCode === 'SUBSCRIBED' && infoFriend.statusCode !== 'FRIEND'"
                href="#"
                class="profile-info__btn btn-send__message"
                @click.prevent="cancelApiRequests(info.id)">
                <simple-svg :filepath="'/static/img/delete.svg'" />
                {{ translations.profileAccountUnsubscribe }}
              </a>
              <a
                v-else-if="infoFriend.statusCode !== 'FRIEND' &&
                  infoFriend.statusCode !== 'REQUEST_TO' &&
                  infoFriend.statusCode !== 'BLOCKED' &&
                  infoFriend.statusCode !== 'REQUEST_FROM' &&
                  infoFriend.statusCode !== 'SUBSCRIBED'"
                href="#"
                class="profile-info__btn subscribe btn-send__message"
                @click.prevent="subscribe(info.id)">
                <simple-svg :filepath="'/static/img/sidebar/admin/comments.svg'" />
                {{ translations.profileAccountSubscribe }}
              </a>
              <a
                v-if="infoFriend.statusCode === 'REQUEST_FROM'"
                href="#"
                class="profile-info__btn btn-send__message"
                @click.prevent="acceptFriendRequest(info.id)">
                <simple-svg class="accept" :filepath="'/static/img/add.svg'" />
                {{ translations.profileAccountAcceptRequests }}
              </a>
              <!-- Сообщение -->
              <a
                v-if="infoFriend.statusCode !== 'BLOCKED'"
                href="#"
                class="profile-info__btn btn-send__message"
                @click.prevent="onSentMessage">
                <simple-svg :filepath="'/static/img/sidebar/im.svg'" />
                {{ translations.profileAccountSendMessage }}
              </a>
              <!-- Блокировка/разблокировка -->
              <a
                v-if="blocked || info.isBlocked || infoFriend.statusCode === 'BLOCKED'"
                href="#"
                class="profile-info__btn btn-send__message"
                @click.prevent="unBlockedUser(info.id)">
                <simple-svg class="filter-green" :filepath="'/static/img/security-system-unlock.svg'" />
                {{ translations.profileAccountUnblocking }}
              </a>
              <a
                v-else
                href="#"
                class="profile-info__btn block btn-send__message"
                @click.prevent="blockedUser(info.id)">
                <simple-svg :filepath="'/static/img/unblocked.svg'" />
                {{ translations.profileAccountBlocking }}
              </a>
              <!-- Добавление в друзья/отмена -->
              <a
                v-if="infoFriend.statusCode === 'FRIEND'"
                href="#"
                class="profile-info__btn btn-send__message"
                @click.prevent="cancelApiRequests(info.id)">
                <simple-svg  :filepath="'/static/img/delete.svg'" />
                {{ translations.profileAccountDeleteFriend }}
              </a>
              <a
                v-if="infoFriend.statusCode === 'REQUEST_TO'"
                href="#"
                class="profile-info__btn btn-send__message"
                @click.prevent="cancelApiRequests(info.id)">
                <simple-svg :filepath="'/static/img/delete.svg'" />
                {{ translations.profileAccountCancelFriend }}
              </a>
              <a
                v-if="infoFriend.statusCode !== 'WATCHING' &&
                  infoFriend.statusCode !== 'REQUEST_TO' &&
                  infoFriend.statusCode !== 'BLOCKED' &&
                  infoFriend.statusCode !== 'REQUEST_FROM' &&
                  infoFriend.statusCode !== 'SUBSCRIBED' &&
                  infoFriend.statusCode !== 'FRIEND'"
                href="#"
                class="profile-info__btn btn-send__message"
                @click.prevent="addToFriend(info.id)">
                <simple-svg class="accept" :filepath="'/static/img/add.svg'" />
                {{ translations.profileAccountAddFriend }}
              </a>
            </div>
          </transition>
        </div>
      </div>
      <div class="showmore-info">
        <div class="profile-info__show">
          <div class="profile-info__value">
            <span class="profile-info__title">
              <gift-icon />
              {{ translations.profileInfoBirthday }}
            </span>
            <span class="profile-info__val" v-if="info.birthDate">
              {{
                new Date(info.birthDate).toLocaleString('ru', {
                  year: 'numeric',
                  month: 'long',
                  day: 'numeric',
                })
              }}
            </span>
            <span class="profile-info__val" v-else>{{ translations.profileNotFilled }}</span>
          </div>
          <div class="profile-info__value">
            <span class="profile-info__title">
              <phone-icon />
              {{ translations.profileInfoPhone }}
            </span>
            <a class="profile-info__val" v-if="info.phone" :href="`tel: +7${info.phone}`">
              {{ info.phone | phone }}
            </a>
            <a class="profile-info__val" v-else>{{ translations.profileNotFilled }}</a>
          </div>
          <div class="profile-info__value">
            <span class="profile-info__title">
              <home-icon />
              {{ translations.profileInfoCountry }}
            </span>
            <span class="profile-info__val" v-if="residenceText">
              {{ residenceText }}
            </span>
            <span class="profile-info__val" v-else>{{ translations.profileNotFilled }}</span>
          </div>
          <div class="profile-info__value">
            <span class="profile-info__title">
              <about-icon />
              {{ translations.profileInfoAbout }}
            </span>
            <span class="profile-info__val info-about__text" v-if="info.about"> {{ info.about }}</span>
            <span class="profile-info__val" v-else>{{ translations.profileNotFilled }}</span>
          </div>
        </div>
        <div class="profile-info__weather" v-if="me">
          <weather-block />
        </div>
      </div>
      <modal v-model="modalShow">
        <p v-if="modalText">{{ modalText }}</p>
        <template slot="actions">
          <button-hover @click.native.prevent="onConfirm">Да</button-hover>
          <button-hover variant="red" bordered="bordered" @click.native="closeModal">
            Отмена
          </button-hover>
        </template>
      </modal>
    </div>
    <div class="shine profile-info__not-info" v-else>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import AboutIcon from '../../Icons/AboutIcon.vue';
import GiftIcon from '../../Icons/GiftIcon.vue';
import HomeIcon from '../../Icons/HomeIcon.vue';
import PhoneIcon from '../../Icons/PhoneIcon.vue';
import SharedAccount from '../../Icons/SharedAccount.vue';
import ArrowButtom from '../../Icons/ArrowBottom.vue';
import CopiedIcon from '../../Icons/CopiedIcon.vue';
import DoneCopied from '../../Icons/DoneCopied.vue';
import PlusIcon from '../../Icons/PlusIcon.vue';
import UnknowUser from '../../Icons/UnknowUser.vue';
import WeatherBlock from '@/components/WeatherBlock.vue';
import Modal from '@/components/Modal';
import QRCode from 'qrcode-generator';
import vClickOutside from 'v-click-outside';
import translations from '@/utils/lang.js';

export default {
  name: 'ProfileInfo',
  directives: {
    clickOutside: vClickOutside.directive
  },

  components: {
    GiftIcon,
    PhoneIcon,
    HomeIcon,
    AboutIcon,
    SharedAccount,
    CopiedIcon,
    DoneCopied,
    ArrowButtom,
    PlusIcon,
    UnknowUser,
    Modal,
    WeatherBlock
  },

  props: {
    me: Boolean,
    online: Boolean,
    blocked: Boolean,
    friend: Boolean,
    info: Object,
    text: String,
    infoFriend: Object,
  },

  data: () => ({
    modalShow: false,
    modalText: '',
    modalType: 'deleteFriend',
    showInfo: false,
    showInfoWeather: false,
    showEmojiStatus: false,
    showPopup: false,
    qrCode: '',
    copied: false,
    showLoupe: false,
    showAvatar: false,
    showActions: false,
  }),

  computed: {
    ...mapGetters('users/info', ['getUsersInfo', 'getWall', 'getWallPagination']),
    ...mapGetters('profile/dialogs', ['dialogs']),
    ...mapGetters('global/search', ['getResultByIdSearch']),

    users() {
      return this.getResultByIdSearch('users');
    },

    usersByEmojiStatus() {
      return this.users.reduce((acc, user) => {
        const { emojiStatus } = user;
        if (acc[emojiStatus]) {
          acc[emojiStatus].push(user);
        } else {
          acc[emojiStatus] = [user];
        }
        return acc;
      }, {});
    },

    translations() {
      const lang = this.$store.state.auth.languages.language.name;
      if (lang === 'Русский') {
        return translations.rus;
      } else {
        return translations.eng;
      }
    },

    residenceText() {
      let country = this.info?.country;
      let city = this.info?.city;
      if (!country && !city) {
        return null;
      }
      return `${country + ', ' || ''} ${city || ''}`;
    },

    currentUser() {
      return this.$store.getters.getUser;
    },

    currentUrl() {
      if (this.me === true) {
        return `${window.location.origin}/profile/` + this.currentUser;
      } else {
        return window.location.href;
      }
    },

    statusText() {
      return this.online ? this.translations.profileInfoStatusOnline : this.translations.profileInfoStatusOffline;
    },
  },

  mounted() {
    let searchQuery = Object.assign({}, this.getUsersQueryParams, {
      page: this.page - 1,
      size: this.size,
    });
    if (this.users.length === 0) {
      this.searchUsers(searchQuery);
    }
  },

  methods: {
    ...mapActions('users/actions', ['apiBlockedUser', 'apiUnblockUser']),
    ...mapActions('profile/friends', ['apiAddFriends', 'apiDeleteFriends', 'apiSubscribe']),
    ...mapActions('profile/dialogs', ['createDialogWithUser', 'apiLoadAllDialogs']),
    ...mapActions('global/search', ['searchUsers']),
    ...mapActions('users/info', ['userInfoId']),
    ...mapGetters('profile/info', ['getInfo']),


    closeEmojiList() {
      this.showEmojiStatus = false
    },

    toggleEmojiStatus() {
      this.showEmojiStatus = !this.showEmojiStatus;
    },

    getAgeAsString(age) {
      const ages = ['год', 'года', 'лет'];
      const n = parseInt(age, 10);
      return ages[n % 100 > 4 && n % 100 < 20 ? 2 : [2, 0, 1, 1, 1, 2][n % 10 < 5 ? n % 10 : 5]];
    },

    copyText() {
      const range = document.createRange();
      range.selectNode(this.$refs.text);
      window.getSelection().removeAllRanges();
      window.getSelection().addRange(range);
    },

    copyToClipboard() {
      const range = document.createRange();
      range.selectNode(this.$refs.text);
      window.getSelection().removeAllRanges();
      window.getSelection().addRange(range);
      document.execCommand('copy');
      this.copied = true;
    },

    actionsShow() {
      this.showActions = !this.showActions;
    },

    closeActions() {
      this.showActions = false;
    },

    togglePopup() {
      const url = this.currentUrl;
      this.qrCode = this.generateQRCode(url);
      this.showPopup = !this.showPopup;
    },

    closePopup() {
      window.getSelection().removeAllRanges();
      this.copied = false;
      this.showPopup = false;
    },

    generateQRCode(url) {
      const qr = QRCode(0, 'M');
      qr.addData(url);
      qr.make();
      return qr.createImgTag();
    },

    scrollToPosts() {
      const posts = document.getElementById('mypublications');
      posts.scrollIntoView({ behavior: 'smooth' });
      this.activeLink = 'link1';
    },

    showMoreInfo() {
      this.showInfo = !this.showInfo;
    },

    showWeather() {
      this.showInfoWeather = !this.showInfoWeather;
    },

    addToFriend(id) {
      if (this.infoFriend.statusCode === 'REQUEST_TO') {
        this.$store.dispatch('global/alert/setAlert', {
          status: 'action',
          text: 'Вы уже отправляли запрос на добавления в друзья этому пользователю!',
        });
        return;
      }
      if (this.infoFriend.statusCode === 'FRIEND') {
        this.$store.dispatch('global/alert/setAlert', {
          status: 'action',
          text: 'Этот пользователь уже является вашим другом!',
        });
        return;
      }
      if (this.infoFriend.statusCode === 'BLOCKED') {
        this.$store.dispatch('global/alert/setAlert', {
          status: 'action',
          text: 'Вы заблокировали этого пользователя!',
        });
        return;
      }
      if (this.infoFriend.statusCode === 'SUBSCRIBED') {
        this.$store.dispatch('global/alert/setAlert', {
          status: 'action',
          text: 'Этот пользователь среди ваших подписчиков, для добавления в друзья необходимо удалить его из списка подписчиков!',
        });
        return;
      }
      if (
        this.infoFriend.statusCode !== 'NONE' &&
        this.infoFriend.statusCode !== null &&
        this.infoFriend.statusCode == 'WATCHING'
      ) {
        this.$store.dispatch('global/alert/setAlert', {
          status: 'action',
          text: 'У вас уже есть отношения с этим пользователем, чтобы подписаться необходимо остановить другие отношения!',
        });
        return;
      }
      this.apiAddFriends({ id });
      this.locationReload();
    },

    locationReload() {
      setTimeout(() => {
        location.reload()
      }, 300)
    },

    cancelApiRequests(id) {
      this.apiDeleteFriends(id);
      this.locationReload();
    },

    acceptFriendRequest(id) {
      if (this.infoFriend.statusCode === 'FRIEND') {
        this.$store.dispatch('global/alert/setAlert', {
          status: 'action',
          text: 'Этот пользователь уже является вашим другом!',
        });
        return;
      }
      this.apiAddFriends({ id, isApprove: true });
      this.locationReload()
    },

    subscribe(id) {
      if (this.infoFriend.statusCode === 'SUBSCRIBED') {
        this.$store.dispatch('global/alert/setAlert', {
          status: 'action',
          text: 'Вы уже подписаны на этого пользователя!',
        });
        return;
      }
      if (
        this.infoFriend.statusCode === 'FRIEND'
      ) {
        this.$store.dispatch('global/alert/setAlert', {
          status: 'action',
          text: 'У вас уже есть отношения с этим пользователем, чтобы подписаться необходимо остановить другие отношения!',
        });
        return;
      }
      this.apiSubscribe(id);
      this.locationReload();
    },

    blockedUser(id) {
      this.apiBlockedUser(id);
      this.locationReload();
    },

    unBlockedUser(id) {
      this.apiUnblockUser(id);
      this.locationReload();
    },

    closeModal() {
      this.modalShow = false;
    },

    onSentMessage() {
      if (this.blocked) return false;
      this.$router.push({
        name: 'ImChat',
        params: { activeDialogId: this.info.id.toString() },
      });
    },
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'
@import '../../assets/stylus/base/settings.styl'

  .isonline-online
    background unset
    color ui-cl-color-eucalypt

  .isonline-lasttime
    color #6c6c6c

  .profile-info__emoji__list
    display flex
    align-items center
    margin-bottom 16px

  .profile-info__val
    font-size font-size-small

  .profile-info__emoji__item
    margin-left -12px

  .profile-info__emoji-quantity-all
    color ui-cl-color-santas-gray
    span
      font-size font-size-small !important

  .img-emoji__more
    background-color ui-cl-color-light-eucalypt !important
    color ui-cl-color-white-theme !important

  .profile-info__emoji-img
    display flex
    align-items center
    justify-content center
    width 50px
    height 50px
    border-radius 50%
    overflow hidden
    background-color #d9d9d9
    color ui-cl-color-dark-grey
    font-weight font-weight-medium
    img
      display flex
      align-items center
      justify-content center
      width 100%
      height 100%
      -o-object-fit cover
      object-fit cover
      margin-bottom 0 !important

  .profile-info__emoji-quantity,
  .profile-info__emoji-quantity-user
    font-size font-size-small !important


  .profile-info__emoji
    position fixed
    display flex
    flex-direction column
    justify-content center
    max-width 450px
    width 100%
    top 50%
    left 50%
    transform translate(-50%, -50%)
    z-index 10
    background ui-cl-color-white-theme
    border-radius border-big-radius
    box-shadow 0px 0px 33px rgba(0,0,0,0.42)
    padding 50px 20px
    &.fade-enter-active,
    &.fade-leave-active
      transition all .2s ease-in-out
    &.fade-enter,
    &.fade-leave-to
      opacity 0

  .profile-info__emoji-name
    cursor pointer

  .profile-info__emoji-set
    display flex
    align-items center
    align-self center
    padding 10px
    border-radius border-super-small
    background-color #F0F0F0
    transition all .2s ease-in-out
    &:hover
      background-color #dbd2d2

  .profile-info__emoji-item
    display flex
    flex-direction column
    align-items center
    justify-content center
    text-align center
    span
      font-weight font-weight-medium
      font-size 19px
      margin-bottom 16px
    p
      font-size font-size-default
      line-height 19px
      margin-bottom 25px
    img
      max-width 80px
      margin-bottom 20px

  .profile-info__overlay
    position fixed
    top 0
    left 0
    width 100%
    height 100%
    background-color rgba(0, 0, 0, 0.5)
    z-index 1

  .v-enter-active,
  .v-leave-active
    transition opacity .3s ease-in-out

  .showmore-info
    display grid
    align-items center
    grid-template-columns 65% 1fr
    background ui-cl-color-white-theme
    gap 70px
    &.not-me
      justify-content flex-start

  .profile-info
    background ui-cl-color-white-theme
    border-radius border-small
    box-shadow box-shadow-main
    overflow hidden
    width 1250px
    max-width 100%

    &__value .profile-info__val
      font-size font-size-small-medium

    &__not-info
      width 1250px
      max-width 100%
      height 300px
      border-radius border-small
      overflow hidden

    &__btn
      display flex
      align-items center
      gap 5px
      color ui-cl-color-grey-color
      white-space nowrap
      font-size font-size-small
      padding 10px
      border-radius border-small
      transition all .2s ease-in-out
      &:hover
        background-color #f5f6f8

      &.block
        svg
          path
            fill unset
            stroke #222
            stroke-width 1px
            stroke-opacity 1
          line
            stroke #222

      &.subscribe
        svg path
          fill unset
          stroke #222
          stroke-opacity 1

      svg
        width 13px
        height 13px
        path
          fill #222

        rect
          fill #222


    &__showmore
      display flex
      align-items center
      gap 5px
      background-color ui-cl-color-eucalypt
      border-radius border-super-small
      font-size font-size-downdefault
      padding 7px
      color ui-cl-color-white-theme
      transition background-color .2s ease-in-out
      @media (any-hover: hover)
        &:hover
          background-color #167240

    &__actions
      display flex
      flex-direction column
      position absolute
      padding 10px
      top 40px
      right 0
      background-color ui-cl-color-white-theme
      box-shadow box-shadow-main
      border-radius border-small

      &.fade-enter-active,
      &.fade-leave-active
        transition all .2s ease-in-out
      &.fade-enter,
      &.fade-leave-to
        opacity 0


    &__background
      position relative
      height 250px
      width 100%
      background-size cover
      object-fit cover
      background-size cover
      background-repeat no-repeat
      background-position center center
      background-color #ededed
    &__left,
    &__right
      display flex
      align-items center
      justify-content space-between
    &__right
      position relative
    &__link
      text-transform uppercase
      font-size font-size-downdefault
      font-weight font-weight-bold
      color #707070
      transition all .2s ease-in-out
      @media (any-hover: hover)
        &:hover
          color ui-cl-color-grey-color
      &.active
        color ui-cl-color-bright-light-blue
        border-bottom 3px solid ui-cl-color-bright-light-blue
        &:hover
          color ui-cl-color-bright-light-blue
      &:not(:last-child)
        margin-right 20px
    &__image
      display flex
      flex-direction column
      justify-content center
      align-items center
      position absolute
      top -50px
      left 50%
      transform: translate(-50%, -35%);
    &__avatar
      position relative
      width 150px
      height 150px
      border-radius border-half
      border 6px solid ui-cl-color-eucalypt
      overflow hidden
      margin-bottom 10px
      img
        display flex
        align-items center
        justify-content center
        width 100%
        height 100%
        object-fit cover
        cursor pointer
      &.offline
        border-color ui-cl-color-light-grey
        img
          cursor default
      &-zoom
        position: absolute
        top: 50%
        left: 50%
        transform: translate(-50%, -50%)
        width: 100%
        height: 100%
        opacity: 0.5
        color ui-cl-color-white-theme
        font-size 45px
        background ui-cl-color-full-black
        pointer-events none
        display flex
        justify-content center
        align-items center

        &.fade-enter-active,
        &.fade-leave-active
          transition all .2s ease-in-out
        &.fade-enter,
        &.fade-leave-to
          opacity 0

      &-background
        background-color ui-cl-color-white-theme
        padding 10px
        border-radius border-small

      &-container
        position fixed
        top 0
        left 0
        width 100%
        height 100%
        background-color rgba(0,0,0,0.8)
        display flex
        justify-content center
        align-items center
        z-index 1000

        &.fade-enter-active,
        &.fade-leave-active
          transition all .2s ease-in-out
        &.fade-enter,
        &.fade-leave-to
          opacity 0

      &-loupe
        max-width 700px
        max-height 700px
        border-radius border-small


    &__bottom
      position relative
      padding 50px 50px
      display flex
      align-items center
      justify-content space-between
      background ui-cl-color-white-bright
      border-bottom 1px solid ui-cl-color-e2e2e2
    &__show
      padding 50px 0 50px 50px
    &__weather
      padding 50px 50px 50px 0
    &__names
      color ui-cl-color-full-black
      text-align center
    &__name
      display flex
      gap 10px
      align-items center
      justify-content center
      text-transform uppercase
      font-size 20px
      margin-bottom 5px
    &__emoji-status
      max-width 25px
    &__cities
      color ui-cl-color-949494
    &__value
      margin-bottom 10px
    &__title
      color ui-cl-color-bright-light-blue
      gap 5px
      font-weight font-weight-bold
    &__status
      display flex
      justify-content center
      margin-top 8px
    &__shared-btn
      position absolute
      top 10px
      right 10px
      z-index 100
      background ui-cl-color-grey-color
      padding 5px
      border-radius border-super-small
      box-shadow box-shadow-main
    &__share-popup
      display flex
      flex-direction column
      align-items center
      justify-content center
      position absolute
      top 50px
      right 10px
      padding 15px
      background-color ui-cl-color-white-theme
      z-index 100
      border-radius border-small 0 border-small border-small
      box-shadow box-shadow-main

      &.fade-enter-active,
      &.fade-leave-active
        transition all .2s ease-in

      &.fade-enter,
      &.fade-leave-to
        opacity 0

    &__share-link
      display flex
    &__link-profile
      display block
      overflow hidden
      border-left 1px solid ui-cl-color-d2d2d2
      border-top 1px solid ui-cl-color-d2d2d2
      border-bottom 1px solid ui-cl-color-d2d2d2
      border-radius border-super-small 0 0 border-super-small
      padding 10px
    &__link-btn
      display inline-block
      background ui-cl-color-eucalypt
      padding 0 10px
      border-radius 0 border-super-small border-super-small 0
      color ui-cl-color-white-theme
      font-weight font-weight-bold
    .qr-code
      img
        width 130px
        height 130px


</style>
