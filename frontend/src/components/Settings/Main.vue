<template>
  <div class="settings-main">
    <div
      v-if="srcCover"
      :style="{'background-image': `url(${srcCover})`}"
      class="settings-main__cover"
    >
    </div>
    <div class="settings-main-photoblock">
      <div class="user-info-form__block user-info-form__block--photo">
        <div class="settings-main__top">
          <div class="main-layout__user-pic" v-if="src">
            <img :src="src" :alt="firstName[0]" />
          </div>
          <div class="main-layout__user-pic" style="background-color: #333;" v-else>
            <img
              src="https://yastatic.net/s3/yandex-id-static/yandex-id/_/_next/static/media/avatar-placeholder.f27a38d6.png"
              :alt="translations.settingMainPhotoAlt"
            />
          </div>
        </div>

        <div class="user-info-form__wrap">
          <div class="user-info-form__photo-wrap">
            <input
              class="user-info-form__input_stylus-photo"
              type="file"
              ref="photo"
              id="photo"
              @change="processFile($event)"
              accept="image/*"
            />
            <input
              class="user-info-form__input_stylus-photo"
              type="file"
              ref="photoCover"
              id="photoCover"
              @change="processFileCoverProfile($event)"
              accept="image/*"
            />
            <!-- Аватарка -->
            <button class="setting-main__buttons" @click.prevent="loadPhoto">
              <load-photo />
              <span v-if="src">{{ translations.settingsMainEditPhoto }}</span>
              <span v-else>{{ translations.settingMainAddPhoto }}</span>
            </button>
            <div v-if="src" class="settings-main__top--delete" @click="deletePhoto">
              <div class="settings-main__top--delete-icon">
                <delete-icon />
              </div>
            </div>
            <!-- Обложка профиля -->
            <div class="settings-main-actions__cover">
              <button class="settings-main-actions-load__cover" @click.prevent="loadPhotoCover">
                <load-photo />
                <span v-if="srcCover">{{ translations.settingsMainEditCover }}</span>
                <span v-else>{{ translations.settingsMainAddCover }}</span>
              </button>
              <button v-if="srcCover" class="settings-main-actions-delete__cover" @click.prevent="deletePhotoCover">
                <div>
                  <delete-icon />
                </div>
              </button>
            </div>
            <div>
              <div class="settings-emoji__buttons">
                <button
                  class="settings-emoji__btn"
                  @click.prevent="toggleEmojiStatus"
                >
                  <span v-if="emojiStatus">{{ translations.settingsUnknowEmojiStatus }}</span>
                  <span v-else>{{ translations.settingsSetEmojiStatus }}</span>
                </button>
              </div>
              <div class="overlay-emoji" v-if="showEmojiStatus" @click="showEmojiStatus = false"></div>
              <transition name="fade">
                <div class="settings-emoji" v-if="showEmojiStatus" v-click-outside="closeEmojiList">
                  <div class="settings-emoji-top">
                    <div class="settings-emoji-top__left">
                      <h2 class="settings-emoji__title">{{ translations.settingsEmojiTitle }}</h2>
                      <span class="settings-emoji__desclamer">{{ translations.settingsEmojiDisclamer }}</span>
                    </div>
                    <div class="settings-emoji-top__right">
                      <button class="settings-emoji-top__btn settings-main-actions-delete__cover" @click.prevent="deleteEmojiStatus">
                        <delete-icon />
                        {{ translations.settingsEmojiDelete }}
                      </button>
                    </div>
                  </div>
                  <ul class="settings-emoji__list">
                    <li
                      class="settings-emoji__item"
                      :class="{ 'selected': index.toString() === emojiStatus }"
                      v-for="(item, index) in itemsEmojiStatus"
                      :key="index"
                      @click="currentEmojiStatus(index)"
                    >
                      <div v-if="typeof item === 'string'">{{ item }}</div>
                      <div v-else-if="typeof item === 'object' && item.type === 'image'">
                        <p class="settings-emoji__subtitle">{{ currentTranslations === 'Русский' ? item.text : item.textEng }}</p>
                        <img class="settings-emoji__img" :src="item.src" :alt="currentTranslations === 'Русский' ? item.alt : item.altEng">
                        <p class="settings-emoji__desc">{{ currentTranslations === 'Русский' ? item.desc : item.descEng }}</p>
                      </div>
                    </li>
                  </ul>
                </div>
              </transition>
            </div>
          </div>
        </div>
      </div>
    </div>

    <user-info-form-block
      :label="translations.createAccNameField1"
      :placeholder="translations.searchUserNameInput"
      text="firstName"
      v-model="firstName"
      ref="firstName"
    />

    <user-info-form-block
      :label="translations.createAccNameField2"
      :placeholder="translations.searchUserSurnameInput"
      text="lastName"
      v-model="lastName"
      ref="lastName"
    />

    <user-info-form-block
      :label="translations.profileInfoPhone"
      :placeholder="translations.settingMainPlaceholderPhone"
      v-model="phone"
      phone="phone"
    />

    <div class="settings-main-input">
      <span class="user-info-form__label_stylus">{{ translations.settingMainCountry }}</span>

      <div class="user-info-form__wrap">
        <v-select
          :placeholder="translations.settingMainPlaceholderCountry"
          class="country"
          v-model="country"
          :options="countryNames"
          :key="countries.id"
        />
      </div>
    </div>

    <div class="settings-main-input">
      <span class="user-info-form__label_stylus">{{ translations.settingMainCity }}</span>

      <div class="user-info-form__wrap">
        <v-select
          :placeholder="translations.settingMainPlaceholderCity"
          class="country"
          v-model="city"
          :options="cityNames"
          :key="cities.id"
        />
      </div>
    </div>

    <div class="settings-main-input">
      <span class="user-info-form__label_stylus">{{ translations.profileInfoBirthday }}</span>

      <div class="user-info-form__wrap">
        <select class="select user-info-form__select day" v-model="day">
          <option :value="null">{{ translations.settingMainUnknow }}</option>
          <option v-for="d in days" :key="d">{{ d }}</option>
        </select>

        <select class="select user-info-form__select month" v-model="month">
          <option :value="null">none</option>
          <option v-for="month in months" :key="month.val" :value="month">
            {{ currentTranslations === 'Русский' ? month.text : month.textEng }}
          </option>
        </select>

        <select class="select user-info-form__select year" v-model="year">
          <option :value="null">{{ translations.settingMainUnknow }}</option>
          <option v-for="i in years" :key="i">{{ i }}</option>
        </select>
      </div>
    </div>

    <user-info-form-block :label="translations.settingMainAbout" v-model="about" about="about" />

    <div class="user-info-form__block user-info-form__block--actions">
      <div class="user-info-form__wrap">
        <button class="setting-main__buttons button-submit" @click.prevent="submitHandler">
          {{ translations.settingMainSave }}
        </button>
        <router-link :to="{ name: 'Profile' }" tag="button" class="setting-main__buttons canceled">
          {{ translations.settingMainCancel }}
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from 'vue';
import { mapGetters, mapActions, mapMutations } from 'vuex';
import DeleteIcon from '../../Icons/DeleteIcon.vue';
import LoadPhoto from '@/Icons/LoadPhoto.vue';
import VSelect from 'vue-select'
import 'vue-select/dist/vue-select.css'
import translations from '@/utils/lang.js';
import vClickOutside from 'v-click-outside';
// import moment from 'moment';
import UserInfoFormBlock from '@/components/Settings/UserInfoForm/Block.vue';
import axios from 'axios';


Vue.component('VSelect', VSelect)
export default {
  name: 'SettingsMain',
  directives: {
    clickOutside: vClickOutside.directive
  },
  components: { UserInfoFormBlock, DeleteIcon, LoadPhoto },

  data: () => ({
    emojiStatus: '',
    firstName: '',
    lastName: '',
    phone: '',
    about: '',
    day: 1,
    month: { val: 1, text: 'Января', textEng: 'January' },
    year: 2000,
    months: [
      { val: 0, text: 'Января', textEng: 'January' },
      { val: 1, text: 'Февраля', textEng: 'February' },
      { val: 2, text: 'Марта', textEng: 'March' },
      { val: 3, text: 'Апреля', textEng: 'April' },
      { val: 4, text: 'Мая', textEng: 'May' },
      { val: 5, text: 'Июня', textEng: 'June' },
      { val: 6, text: 'Июля', textEng: 'July' },
      { val: 7, text: 'Августа', textEng: 'August' },
      { val: 8, text: 'Сентября', textEng: 'September' },
      { val: 9, text: 'Октября', textEng: 'October' },
      { val: 10, text: 'Ноября', textEng: 'November' },
      { val: 11, text: 'Декабря', textEng: 'December' },
    ],
    fileName: '',
    src: '',

    showEmojiStatus: false,
    itemsEmojiStatus: [
        {
          type: 'image',
          src: '/static/img/user/status_guru.png',
          alt: 'Гуру кода',
          altEng: 'Code guru',
          text: 'Гуру кода',
          textEng: 'Code guru',
          desc: 'Вы уже обладаете высоким уровнем знаний и опыта в программировании.',
          descEng: 'You already have a high level of knowledge and experience in programming.'
        },
        {
          type: 'image',
          src: '/static/img/user/status_new.png',
          alt: 'Новичок',
          altEng: 'Beginner',
          text: 'Новичок',
          textEng: 'Beginner',
          desc: 'Вы только начинаете изучать программирование и имеете базовые знания в этой области.',
          descEng: 'You are just starting to learn programming and have basic knowledge in this field.'
        },
        {
          type: 'image',
          src: '/static/img/user/status_escp.png',
          alt: 'Экспериментатор',
          altEng: 'The experimenter',
          text: 'Экспериментатор',
          textEng: 'The experimenter',
          desc: 'Вы любите экспериментировать с новыми технологиями и методами программирования.',
          descEng: 'You like to experiment with new technologies and programming methods.'
        },
        {
          type: 'image',
          src: '/static/img/user/status_teacher.png',
          alt: 'Учитель',
          altEng: 'Teacher',
          text: 'Учитель',
          textEng: 'Teacher',
          desc: 'Вы уже имеете опыт и знания в программировании и помогаете другим изучать эту область.',
          descEng: 'You already have experience and knowledge in programming and are helping others to study this field.'
        },
        {
          type: 'image',
          src: '/static/img/user/status_student.png',
          alt: 'Фрилансер',
          altEng: 'Freelancer',
          text: 'Фрилансер',
          textEng: 'Freelancer',
          desc: 'Ваша работа основана на свободе и творчестве, что позволяет вам достигать успеха в своей области и наслаждаться жизнью.',
          descEng: 'Your work is based on freedom and creativity, which allows you to achieve success in your field and enjoy life.'
        },
        {
          type: 'image',
          src: '/static/img/user/status_love.png',
          alt: 'Любитель',
          altEng: 'Lover',
          text: 'Любитель',
          textEng: 'Lover',
          desc: 'Вы увлекаетесь программированием и имеете базовые знания в этой области, но не являетесь профессионалом.',
          descEng: 'You are interested in programming and have basic knowledge in this field, but you are not a professional.'
        },
      ],
    selectedEmojiStatus: null,

    srcCover: '',
    profileCover: '',

    country: '',
    city: '',
    countries: [],
    cities: [],
    currentCountry: {},
  }),

  computed: {
    ...mapGetters('global/storage', ['getStorage']),
    ...mapGetters('profile/info', ['getInfo']),

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

    phoneNumber() {
      return this.phone.replace(/\D+/g, '');
    },

    years() {
      return Array.from({ length: 61 }, (value, index) => 1962 + index);
    },

    countryNames () {
      return this.countries.map(country => country.title)
    },

    cityNames () {
      return this.cities.map(city => city.title)
    },

    days() {
      return this.month.val === 1
        ? this.year & 3 || (!(this.year % 25) && this.year & 15)
          ? 28
          : 29
        : 30 + ((this.month.val + (this.month.val >> 3)) & 1);
    },
  },

  watch: {
    getInfo(value) {
      if (!value) {
        return;
      }
      this.setInfo();
    },
    country: {
      immediate: true,
      handler(newValue) {
        if (newValue && newValue !== '') {
          setTimeout(() => {
          this.currentCountry = this.countries.find(country => country.title === newValue);
          this.loadCities(this.currentCountry.id);

            if (this.city && this.currentCountry && this.city !== '' && this.currentCountry.id !== this.cities.find(city => city.title === this.city).countryId) {
              this.city = '';
              this.country = '';
            }
          }, 500)
        } else {
          this.city = '';
          this.cities = [];
          this.country = '';
        }
      }
    }
  },

  mounted() {
    this.loadCountries();

    if (this.$route.params.showEmojiPicker) {
      this.showEmojiStatus = true;
    }
  },

  methods: {
    ...mapActions('global/storage', ['apiStorage']),
    ...mapActions('profile/info', ['apiChangeInfo']),
    ...mapMutations('global/storage', ['setStorage']),

    loadCountries() {
      axios
        .get('/geo/country')
        .then((response) => {
          this.countries = response.data;
          if (this.getInfo) {
            this.setInfo();
          }
        })
        .catch(() => {});
      return;
    },

    closeEmojiList() {
      this.showEmojiStatus = false
    },

    toggleEmojiStatus() {
      this.showEmojiStatus = !this.showEmojiStatus;
    },

    currentEmojiStatus(index) {
      this.selectedEmojiStatus = index.toString();
      this.emojiStatus = index.toString();
      this.showEmojiStatus = false;
    },

    deleteEmojiStatus() {
      this.emojiStatus = '';
      this.selectedEmojiStatus = '';
      this.$store.dispatch('global/alert/setAlert', {
          status: 'response',
          text: 'Статус удалён, вы всегда сможете поставить его заново',
        });
      this.showEmojiStatus = false;
    },

    loadCities(countryId) {
      if (!countryId) {
        this.city = null;
        return;
      }
      axios.get(`/geo/country/${countryId}/city`).then((response) => {
        this.cities = response.data;
      });
      return countryId;
    },

    async submitHandler() {
      let _birthDate = 'none';
        if (this.year && this.month && this.day) {
          _birthDate = new Date(this.year, this.month.val, this.day).toISOString();
        }

        if (!this.$refs.firstName.validate() && !this.$refs.lastName.validate()) {
        this.$store.dispatch('global/alert/setAlert', {
          status: 'error',
          text: 'Поля "Имя" и "Фамилия" являются обязательными',
        });
        return
        } else if (!this.$refs.firstName.validate()) {
          this.$store.dispatch('global/alert/setAlert', {
            status: 'error',
            text: 'Поле "Имя" является обязательным',
          });
          return;
        } else if (!this.$refs.lastName.validate()) {
          this.$store.dispatch('global/alert/setAlert', {
            status: 'error',
            text: 'Поле "Фамилия" является обязательным',
          });
          return;
        } else if (this.lastName.length < 3 && this.firstName.length < 3) {
          this.$store.dispatch('global/alert/setAlert', {
            status: 'error',
            text: 'Имя и Фамилия не могут быть короче 3-ёх символов',
          });
          return;
        } else if (this.firstName.length < 3) {
          this.$store.dispatch('global/alert/setAlert', {
            status: 'error',
            text: 'Имя не может быть короче 3-ёх символов',
          });
          return;
        } else if (this.lastName.length < 3) {
          this.$store.dispatch('global/alert/setAlert', {
            status: 'error',
            text: 'Фамилия не может быть короче 3-ёх символов',
          });
          return;
        }

        if (this.fileName) {
          await this.apiStorage(this.fileName).then((response) => {
            this.fileName = response.data.fileName;
          });
        }

        if (this.profileCover) {
          await this.apiStorage(this.profileCover).then((response) => {
            this.profileCover = response.data.fileName;
          });
        }

        await this.apiChangeInfo({
          firstName: this.firstName,
          lastName: this.lastName,
          birthDate: _birthDate,
          phone: this.phoneNumber,
          about: this.about,
          country: this.country,
          city: this.city,
          emojiStatus: this.selectedEmojiStatus === '' ? this.emojiStatus : this.selectedEmojiStatus,
          photo: this.fileName === '' ? this.src : this.fileName,
          profileCover: this.profileCover === '' ? this.srcCover : this.profileCover,
        }).then(() =>
        this.$router.push('/profile'))
      },

    // Аватарка:
    processFile(event) {
      [this.fileName] = event.target.files;
      const reader = new window.FileReader();
      reader.onload = (e) => {
        this.src = e.target.result;
      };
      reader.readAsDataURL(this.fileName);
    },

    loadPhoto() {
      this.$refs.photo.click();
    },

    deletePhoto() {
      this.fileName = '';
      this.src = '';
      this.setStorage('');
    },

    // Обложка
    processFileCoverProfile(event) {
      [this.profileCover] = event.target.files;
      const reader = new window.FileReader();
      reader.onload = (e) => {
        this.srcCover = e.target.result;
      };
      reader.readAsDataURL(this.profileCover);
    },

    loadPhotoCover() {
      this.$refs.photoCover.click();
    },

    deletePhotoCover() {
      this.profileCover = '';
      this.srcCover = '';
      this.setStorage('');
    },

    setInfo() {
      this.firstName = this.getInfo.firstName;
      this.lastName = this.getInfo.lastName;
      this.src = this.getInfo.photo;
      this.srcCover = this.getInfo.profileCover;
      this.emojiStatus = this.getInfo.emojiStatus;

      if (this.getInfo.phone) {
        this.phone = this.getInfo.phone.replace(/^[+]?[78]/, '');
      } else this.phone = '';

      if (this.getInfo.birthDate) {
        const birthDate = new Date(this.getInfo.birthDate);
        this.day = birthDate.getDate();
        this.month = this.months[birthDate.getMonth()];
        this.year = birthDate.getFullYear();
      }
      this.about = this.getInfo.about;
      if (this.getInfo.country) {
        this.country = this.getInfo.country;
        this.currentCountry = this.countries.find((country) => country.title === this.country);
        this.loadCities(this.currentCountry?.id);
      }
      if (this.getInfo.city) {
        this.city = this.getInfo.city;
      }
    },
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'

.vt-notification-container {
  display block !important
}

.settings-emoji__buttons
  display flex
  gap 10px
  align-items center
  position absolute
  top 20px
  left 20px

.settings-emoji-top
  display flex
  align-items flex-start
  justify-content space-between
  border-bottom 1px solid #e3e3e3
  padding-bottom 20px
  margin-bottom 20px

  &__right
    display flex
    flex-direction column

  &__btn
    padding 5px 10px !important

.settings-emoji__btn
  display flex
  align-items center
  gap 5px
  font-size font-size-small-medium
  color ui-cl-color-white-theme
  font-weight font-weight-regular
  background rgba(0, 0, 0, 0.36)
  padding 5px
  border-radius border-small
  transition all .2s ease-in-out
  &:hover
    background rgba(0, 0, 0, 0.5)

.overlay-emoji
  position fixed
  top 0
  left 0
  width 100%
  height 100%
  background-color rgba(0, 0, 0, 0.5)
  z-index 1

.settings-emoji
  position fixed
  display flex
  flex-direction column
  justify-content center
  max-width 100%
  top 50%
  left 50%
  transform translate(-50%, -50%)
  z-index 10
  background ui-cl-color-white-theme
  border-radius border-big-radius
  box-shadow 0px 0px 33px rgba(0,0,0,0.42)
  padding 20px
  &.fade-enter-active,
  &.fade-leave-active
    transition all .2s ease-in-out
  &.fade-enter,
  &.fade-leave-to
    opacity 0

  &__list
    display grid
    grid-template-columns repeat(6, 180px)
    gap 20px
    align-items flex-start
  &__item

    height 100%
    padding 10px
    border-radius border-small
    transition all .2s ease-in-out
    cursor pointer
    &:hover
      background-color #efefef
    &.selected
      background-color ui-cl-color-eucalypt
      color ui-cl-color-white-theme
  &__img
    max-width 40px
    margin-bottom 10px
  &__title
    font-size 23px
    margin-bottom 5px
  &__desclamer
    font-size font-size-super-medium-small
    color ui-cl-color-medium-grey-light
  &__subtitle
    font-weight font-weight-bold
    margin-top 0
    margin-bottom 10px
  &__desc
    font-size font-size-super-medium-small


.settings-main
  position relative
  background ui-cl-color-white-theme
  width 100%
  box-shadow box-shadow-main
  padding 40px 20px
  border-radius border-big-radius
  overflow hidden
  z-index 10

  &-actions-load__cover,
  &-actions-delete__cover
    display flex
    align-items center
    gap 5px
    font-size font-size-small-medium
    color ui-cl-color-white-theme
    font-weight font-weight-regular
    background rgba(0, 0, 0, 0.36)
    padding 5px
    border-radius border-small
    transition all .2s ease-in-out
    &:hover
      background rgba(0, 0, 0, 0.5)

  &-actions__cover
    display flex
    gap 10px
    align-items center
    position absolute
    top 20px
    right 20px

  &__cover
    position absolute
    top 0
    left 0
    width 100%
    height 230px
    background-size cover
    background-repeat no-repeat
    background-position center center
    z-index -1
    opacity 0.8
    &::after
      content ""
      position absolute
      bottom 0
      left 0
      right 0
      height 150px
      background-image linear-gradient(to bottom, rgba(255,255,255,0), rgba(255,255,255,1))

  .user-info-form__label_stylus
    white-space pre-wrap

.settings-main__back
  margin-left 20px

.setting-main__delete-icon
  position absolute
  right 10px
  top 12px
  width 12px
  height 12px
  transition all .2s ease-in-out

.country
  width 100%

.user-info-form__block
  display flex
  justify-content center
  flex-direction column
  .main-layout__user-pic
    width 96px
    height 96px
    border 2px solid grey
    margin-right 0
  img
    width 100%
    height 100%

.settings-main__top
  display flex
  align-items center
  gap 15px
  justify-content center
  margin-bottom 15px
  &--delete
    display flex
    flex-direction column
    font-size 11px
    cursor pointer
    &-icon
      display inline-flex
      justify-content center
      align-items center
      background ui-cl-color-e9e9e9
      padding 9px 10px
      border-radius border-fullhalf
      margin-left 10px
      transition all .2s ease-in-out
      @media (any-hover: hover)
        &:hover
          background ui-cl-color-c5c5c5

.setting-main__buttons
  display flex
  align-items center
  justify-content center
  gap 5px
  background ui-cl-color-e9e9e9
  padding 10px
  border-radius border-big-radius
  font-size font-size-small-medium
  transition all .2s ease-in-out
  box-shadow box-shadow-main
  &.button-submit
    min-width 150px
  @media (any-hover: hover)
    &:hover
      background ui-cl-color-c5c5c5
  &.canceled
    margin-left 10px
    background transparent
    border: 1px solid ui-cl-color-c5c5c5
    color ui-cl-color-8e8e8e
    @media (any-hover: hover)
      &:hover
        background ui-cl-color-c85252
        border-color ui-cl-color-c85252
        color ui-cl-color-white-theme

.settings-main-photoblock
  margin-bottom 25px

.settings-main-input
  display flex
  flex-direction column
  align-items flex-start

.user-info-form__select
  width 100%
  border 1px solid ui-cl-color-e3e3e3
  padding 15px 20px
  font-size font-size-downdefault
  color ui-cl-color-full-black
  display flex
  height auto
  align-items center
  white-space nowrap
  overflow hidden
  position relative
  border-radius border-small

.vs__dropdown-toggle
  width 100%
  border 1px solid ui-cl-color-e3e3e3
  padding 0
  background transparent
  font-size font-size-downdefault
  color ui-cl-color-full-black
  display flex
  align-items center
  white-space nowrap
  overflow hidden
  position relative
  border-radius border-small
.vs__selected
  margin 0
  padding 0
  color ui-cl-color-full-black
.vs__selected-options
  padding 11px 20px
.vs__search,
.vs__search:focus
  margin 0
  padding 0
.vs__actions
  padding-right 10px
  margin 0
.vs__clear
  vertical-align unset
  svg
    height 10px
    display block
@media (min-width: 320px) and (max-width: 768px)
  .settings
    .aside-filter
      background-color transparent !important
      &__title
        display block
    .aside-filter
      border-radius 0
      background none
      margin-bottom 20px
      &__list
        flex-direction column
      &__item
        padding 10px 0 10px 0
        font-size font-size-small
    &-main
      width 100%
      padding 15px
    &-push
      width 100% !important
      padding 15px !important
      &__name
        font-size font-size-super-upsmall !important
      &__item
        padding 10px 0 !important
      &__icon
        width 14px !important
    .user-info-form__input_stylus
      padding 10px
      font-size font-size-small
    .vs__selected-options
      padding 10px
      font-size font-size-small
    .user-info-form__select
      padding 10px
      font-size font-size-small
    .settings-push__check-label:after
      width 30px
    .settings-push__check-label:before
      width 17px
      height 17px
      top -8px
    .settings-push__check-input:checked + .settings-push__check-label:before
      left 32%
    .settings-security__block
      width 100%
      padding 15px
    .settings-delete__title
      font-size font-size-small-medium
      line-height 130%
      max-width unset
    .settings-delete__confirm-label
      font-size font-size-super-upsmall
    .settings-delete__confirm-label:before
      width 18px
      height 15px
      margin-right 15px
    .settings-delete__confirm-label:after
      width 0
      height 8px
      top 9px
      left 6px
    .settings-delete__confirm
      margin-bottom 15px
    .settings-delete__actions
      .btn.btn--disable
        margin 0
      .btn.btn--warning
        margin 0
    .settings-delete__actions-link
      font-size font-size-super-medium-small
    .settings-security__title
      font-size 20px
</style>
