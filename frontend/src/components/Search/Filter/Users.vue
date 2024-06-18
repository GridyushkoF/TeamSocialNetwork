<template>
  <div>
    <button
      class="search-showmore-filter"
      @click.prevent="showBlock = !showBlock"
      :class="{ 'open-filter': showBlock }"
    >
      <span v-if="!showBlock">{{ translations.searchNewsShowMoreButton }}</span>
      <span v-else>{{ translations.searchNewsCloseMoreButton }}</span>
      <arrow-bottom />
    </button>
    <transition name="fade">
      <div class="search-filter--users" v-show="showBlock">
        <div class="search-filter">
          <div class="search-filter__block name">
            <label class="search__label" for="search-people-name">{{ translations.createAccNameField1 }}:</label>
            <input
              class="search__input"
              :placeholder="translations.searchUserNameInput"
              type="text"
              id="search-people-name"
              v-model="firstName"
            />
          </div>
          <div class="search-filter__block lastname">
            <label class="search__label" for="search-people-lastname">{{ translations.createAccNameField2 }}</label>
            <input
              class="search__input"
              :placeholder="translations.searchUserSurnameInput"
              type="text"
              id="search-people-lastname"
              v-model="lastName"
            />
          </div>
          <div class="search-filter__block age">
            <label class="search__label">{{ translations.friendsFindYearTitle }}</label>
            <div class="search__row">
              <select class="select search-filter__select" v-model.number="ageFrom">
                <option :value="null" :disabled="disabled">{{ translations.friendsFindFrom }}</option>
                <option v-for="age in ageFromArray" :value="age" :key="age">{{ translations.friendsFindFrom }} {{ age }}</option>
              </select>
              <span class="search__age-defis">—</span>
              <select class="select search-filter__select" v-model.number="ageTo">
                <option :value="null" :disabled="disabled">{{ translations.friendsFindTo }}</option>
                <option v-for="age in ageToArray" :value="age" :key="age">{{ translations.friendsFindTo }} {{ age }}</option>
              </select>
            </div>
          </div>
          <div class="search-filter__block region">
            <label class="search__label">{{ translations.friendsFindRegion }}</label>
            <div class="search__row">
              <v-select
                :placeholder="translations.settingMainPlaceholderCountry"
                class="country"
                v-model="country"
                :options="countryNames"
                :key="countries.id"
              />
              <v-select
                :placeholder="translations.settingMainPlaceholderCity"
                class="country"
                v-model="city"
                :options="cityNames"
                :key="cities.id"
              />
            </div>
          </div>
        </div>
        <div class="search-filter__block btn-news">
          <button class="search-filter__btn" @click.prevent="onSearchUsers">
            {{ translations.searchNewsBtnComplited }}
          </button>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import Vue from 'vue';
import { mapActions, mapGetters } from 'vuex';
import VSelect from 'vue-select'
import axios from 'axios';
import translations from '@/utils/lang.js';
import 'vue-select/dist/vue-select.css'
import ArrowBottom from '../../../Icons/ArrowBottom.vue';
Vue.component('VSelect', VSelect)

export default {
  name: 'SearchFilterUsers',

  components: {
    ArrowBottom
  },

  data: () => ({
    firstName: null,
    lastName: null,
    ageFrom: null,
    ageTo: null,

    country: '',
    city: '',
    countries: [],
    cities: [],
    currentCountry: {},

    disabled: false,
    ageFromArray: [...Array(100).keys()].slice(5),
    ageToArray: [...Array(121).keys()].slice(5),

    showBlock: false,
  }),

  computed: {
    ...mapGetters('global/search', ['getUsersQueryParams', 'searchText']),

    translations() {
      const lang = this.$store.state.auth.languages.language.name;
      if (lang === 'Русский') {
        return translations.rus;
      } else {
        return translations.eng;
      }
    },

    countryNames () {
      return this.countries.map(country => country.title)
    },

    cityNames () {
      return this.cities?.map(city => city.title)
    },
  },

  watch: {
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
  },

  methods: {
    ...mapActions('global/search', ['searchUsers']),

    onSearchUsers() {
      const { firstName, lastName, ageFrom, ageTo } = this;
      if (ageFrom !== null && ageTo !== null && ageFrom >= ageTo) {
        this.$store.dispatch('global/alert/setAlert', {
          status: 'action',
          text: 'Неправильный выбор возрастного интервала!',
        });
        return;
      }
      const countryName = this.country ? this.country : null;
      const cityName = this.city ? this.city : null;
      const searchQuery = Object.assign({}, this.getUsersQueryParams, {
        firstName,
        lastName,
        ageFrom,
        ageTo,
        country: countryName,
        city: cityName,
        author: this.searchText,
      });
      this.searchUsers({ payload: searchQuery });
    },

    loadCountries() {
      axios
        .get('/geo/country')
        .then((response) => {
          this.countries = response.data;
        })
        .catch(() => {});
      return;
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
  },
};
</script>

<style lang="stylus">

.search-filter--users
  &.fade-enter-active,
  &.fade-leave-active
    transition all .2s ease-in-out
  &.fade-enter,
  &.fade-leave-to
    opacity 0

.search-showmore-filter
  background-color transparent
  transition all .2s ease-in-out
  svg path
    stroke #000
  &:hover
    opacity 0.5
  &.open-filter
    margin-bottom 15px
    transition all .2s ease-in-out
    svg
      transform rotate(180deg)
      transition all .2s ease-in-out


.search-filter__btn
  display inline-block
  text-align center
  color #fff
  padding 8px
  border 2px solid #21a45d
  transition all 0.2s ease-in-out
  background #21a45d
  &:hover
    background #333
    border-color #333
    color #fff

.v-select
  width 100%
  height 41px
.vs__search
  font-size 14px
  margin-top 0
.vs--searchable .vs__dropdown-toggle
  padding 5px
  border 1px solid #e3e3e3
  height 100%
.vs__selected-options
  padding 0
.vs__search, .vs__search:focus
  font-size 14px
  margin-top 0
.vs__selected
  font-size 14px
  margin-top 0
</style>
