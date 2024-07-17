<template>
  <div class="admin-panel admin-modal">
    <div class="admin-panel__adds">
      <h2 class="admin__title">Администраторы и модераторы ({{ countAdmins }})</h2>
      <a href="#" class="admin-panel__btn-add" @click.prevent="openModal('add')">
        Добавить модератора/администратора
      </a>
    </div>
    <div class="panel-publications admin-panel__content">
      <div class="admin-panel__left">
        <search-input v-on:input-change="handleInputChange" />
        <div class="admin-panel__result">
          <div class="admin-panel__result-block" v-if="filter === 'all'">
            <friends-block moderator />
            <friends-block moderator :info="info" />
          </div>
          <div class="admin-panel__result-block" v-if="filter === 'modeators'">
            <friends-block moderator />
          </div>
          <div class="admin-panel__result-block" v-if="filter === 'administrators'">
            <friends-block moderator :info="info" />
          </div>
        </div>
      </div>
      <admin-sidebar :list="list" v-model="filter" @change-filter="onChangeFilter"/>
    </div>
    <modal  v-model="modalShow">
      <h3 class="admin-panel__modal-title">Добавить модератора/администратора</h3>
      <form class="admin-panel__form">
        <div class="admin-panel__form-input">
          <span>Имя:</span>
          <input type="text" v-model="formList.name">
        </div>
        <div class="admin-panel__form-input">
          <span>E-Mail:</span>
          <input type="email" v-model="formList.email">
        </div>
        <div class="admin-panel__form-input">
          <span>Пароль:</span>
          <input type="password" v-model="formList.firstPassword">
        </div>
        <div class="admin-panel__form-input">
          <span>Повторите пароль:</span>
          <input type="password" v-model="formList.secondPassword">
        </div>
        <div class="admin-panel__form-input">
          <span>Тип:</span>
          <div class="radio-labels">
            <label class="radio-label">
              <input type="radio" name="user-type" value="admin" v-model="formList.selectedType">
              Администратор
            </label>
            <label class="radio-label">
              <input type="radio" name="user-type" value="moderator" v-model="formList.selectedType">
              Модератор
            </label>
          </div>
        </div>
      </form>
      <template slot="actions">
        <button class="admin-panel__modal-add admin-panel__btn-add">Добавить</button>
        <button class="admin-panel__modal-cancel" @click="closeModal">Отмена</button>
      </template>
    </modal>
  </div>
</template>

<script>
import SearchInput from '../SearchInput.vue';
import AdminSidebar from './Sidebar.vue';
import FriendsBlock from '@/components/Friends/Block';
import Modal from '@/components/Modal';

export default {
  name: 'AdministratorPanel',
  components: { SearchInput, AdminSidebar, FriendsBlock, Modal },
  props: {
    value: String,
    list: {
      type: Array,
      default: () => [
        { text: 'Все', id: 'all' },
        { text: 'Модераторы', id: 'modeators' },
        { text: 'Администраторы', id: 'administrators' },
      ],
    },
    info: {
      type: Object,
      default: () => ({
        firstName: 'Андрей',
        lastName: 'Вострецов',
        birthDate: 1559751301818,
        town_id: 1,
        photo: '/static/img/user/user_2.jpg',
        id: 125,
      }),
    },
  },

  data: () => ({
    filter: 'all',
    inputValue: '',
    isActive: false,
    countAdmins: 0,
    modalShow: false,
    modalType: 'add',
    formList: {
      name: 'Михаил',
      email: 'mlvmisha@gmail.com',
      firstPassword: 'qwerty123',
      secondPassword: 'qwerty123',
      selectedType: 'moderator'

    },
  }),

  watch: {
    selectedType(newVal) {
      this.$emit('update:type', newVal);
    }
  },

  methods: {
    onChangeFilter(id) {
      this.filter = id;
    },

    handleInputChange(value) {
      this.inputValue = value;
    },

    closeModal() {
      this.modalShow = false;
      this.form = {
        name: '',
        email: '',
        firstPassword: null,
        secondPassword: null
      }
    },

    openModal(id) {
      this.modalType = id;
      this.modalShow = true;
    },
  },
}
</script>


<style lang="stylus">
@import '../../../assets/stylus/base/vars.styl'

.radio-label {
  position relative
  display flex
  align-items center
  font-size font-size-small-medium
}

.radio-labels
  display flex
  align-items center
  gap 15px

.radio-label input[type="radio"]
  margin-right 5px
  appearance none
  -webkit-appearance none
  -moz-appearance none
  width 7px
  height 7px
  border-radius border-half
  border 2px solid ui-cl-color-BCBCC7
  outline none
  cursor pointer

.radio-label input[type="radio"]:checked
  background-color ui-cl-color-eucalypt
  border-color ui-cl-color-eucalypt


.admin-modal
  .modal__body
    margin 0
    max-width 100%
    margin-bottom 20px

.admin-panel__result-block
  .friends-block
    margin-bottom 0

.admin-panel__adds
  display flex
  align-items center
  justify-content: space-between

.admin-panel__btn-add
  display flex
  align-items center
  gap 5px
  border-radius 7px
  border 1px solid ui-cl-color-gun-powder
  background-color transparent
  padding 10px 20px
  font-weight font-weight-medium
  color ui-cl-color-gun-powder
  transition all .2s ease-in-out
  @media (any-hover: hover)
    &:hover
      border-color ui-cl-color-gun-powder
      background-color ui-cl-color-gun-powder
      color ui-cl-color-white-theme

.admin-panel__form
  display flex
  flex-direction column
  gap 15px

.admin-panel__modal-title
  font-family 'Exo 2'
  font-size font-size-updefault
  line-height 21px
  font-weight font-weight-medium
  margin-bottom 20px

.admin-panel__form-input
  display grid
  grid-template-columns 100px 1fr
  align-items center
  span
    display inline-block
    text-align left
    font-size font-size-small-medium
    line-height 100%
    margin-right 30px
  input
    border 1px solid #cacaca
    border-radius border-small
    padding 10px
    font-size font-size-small-medium

.admin-panel__modal-add
  margin-right 20px

.admin-panel__modal-cancel
  border 1px solid #d9a7a7
  background-color transparent
  padding 10px 20px
  font-weight font-weight-regular
  color #d9a7a7
  border-radius 7px
  transition all .2s ease-in-out
  @media (any-hover: hover)
    &:hover
      border-color #e52b2b
      background-color #e52b2b
      color ui-cl-color-white-theme
</style>
