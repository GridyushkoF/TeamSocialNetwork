<template>
  <div class="comment-block" :class="{ 'show-comments': isShowSubComments }">
    <template v-if="admin">
      <div class="edit" v-tooltip.bottom="'Разблокировать'" v-if="blocked">
        <simple-svg :filepath="'/static/img/unblocked.svg'" />
      </div>

      <div class="edit" v-tooltip.bottom="'Заблокировать'" v-else>
        <simple-svg :filepath="'/static/img/blocked.svg'" />
      </div>
    </template>

    <comment-main
      :admin="admin"
      :info="info"
      :edit="edit"
      :deleted="deleted"
      @answer-comment="onAnswerMain"
      @edit-comment="onEditMain"
      @delete-comment="onDeleteComment"
      @recover-comment="onRecoverComment"
    />

    <div class="comment-block__reviews" v-if="!info.is_deleted">
      <a
        class="comment-block__reviews-show"
        href="#"
        v-if="!currentSubComents && info.commentsCount"
        @click.prevent="showSubComments"
      >
        {{ translations.commentAnswerShow }} {{ info.commentsCount }} {{ answerText }}
      </a>

      <div class="comment-block__reviews-list" v-show="currentSubComents">
        <transition name="fade">
          <div v-if="currentSubComents" class="subcomments">
            <comment-main
              :isSubcomment="true"
              :admin="admin"
              v-for="i in currentSubComents.value"
              :key="i.id"
              :info="i"
              :edit="getInfo.id === i.author.id"
              :deleted="getInfo.id === i.author.id"
              @answer-comment="onAnswerSub"
              @edit-comment="onEditSub"
              @delete-comment="onDeleteSubComment"
              @recover-comment="onRecoverSubComment"
            />
          </div>
        </transition>

        <comment-add
          :isSubcomment="true"
          v-if="!admin"
          ref="addComment"
          :id="info.postId"
          :parent-id="info.parentId"
          v-model="commentText"
          @submited="onSubmitComment"
        />
      </div>
    </div>
  </div>
</template>

<script>
import CommentMain from '@/components/Comments/Main';
import CommentAdd from '@/components/Comments/Add';
import { mapActions, mapGetters, mapState } from 'vuex';
import translations from '@/utils/lang.js';

export default {
  name: 'CommentBlock',
  components: { CommentMain, CommentAdd },
  props: {
    admin: Boolean,
    blocked: Boolean,
    info: Object,
    edit: Boolean,
    deleted: Boolean,
  },
  data: () => ({
    isShowSubComments: false,
    commentText: '',
    commentEdit: false,
    commentEditId: null,
    commentEditParentId: null,
  }),
  computed: {
    ...mapGetters('profile/info', ['getInfo']),
    ...mapState('profile/comments', ['subComments']),

    translations() {
      const lang = this.$store.state.auth.languages.language.name;
      if (lang === 'Русский') {
        return translations.rus;
      } else {
        return translations.eng;
      }
    },

    answerText() {
      if (!this.info) return this.translations.commentAnswerTextSecond;
      return this.info.commentsCount && this.info.commentsCount > 1 ? this.translations.commentAnswerTextFirst : this.translations.commentAnswerTextSecond;
    },

    currentSubComents() {
      return this.subComments[this.info.id];
    },
  },

  methods: {
    ...mapActions('profile/comments', [
      'commentActions',
      'deleteComment',
      'recoverComment',
      'commentsById',
    ]),

    async showSubComments() {
      if (this.isShowSubComments) return;
      await this.commentsById({ postId: this.info.postId, commentId: this.info.id });
      this.isShowSubComments = true;
    },

    onAnswerSub() {
      this.$refs.addComment.setFocus();
    },

    async onAnswerMain() {
      await this.showSubComments();
      this.onAnswerSub();
    },

    onEditMain({ commentText }) {
      this.$emit('edit-comment', {
        commentInfo: this.info,
        commentText,
      });
    },

    onDeleteComment(id) {
      this.deleteComment({
        id,
        postId: this.info.postId,
      });
    },

    onDeleteSubComment(id, parentId) {
      this.deleteComment({
        id,
        postId: this.info.postId,
        parentId,
      });
    },

    onRecoverComment(id) {
      this.recoverComment({
        id,
        postId: this.info.postId,
      });
    },

    onRecoverSubComment(id) {
      this.recoverComment({
        id,
        postId: this.info.postId,
        isSubComment: true,
      });
    },

    onEditSub({ parentId, id, commentText }) {
      this.commentEdit = true;
      this.commentText = commentText;
      this.commentEditId = id;
      this.commentEditParentId = parentId;
      this.onAnswerSub();
    },

    onSubmitComment() {
      this.commentActions({
        isSubcomment: true,
        edit: this.commentEdit,
        postId: this.info.postId,
        parentId: this.commentEdit ? this.commentEditParentId : this.info.id,
        text: this.commentText,
        id: this.commentEditId,
      }).then(() => {
        this.commentText = '';
        this.commentEdit = false;
        this.commentEditInfo = null;
        this.commentEditParentId = null;
        this.isShowSubComments = true;
      });
    },
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl'

.comment-block
  position relative

    &:after
      display block

  &:not(:last-child)
    margin-bottom 20px

  &.show-comments
    & + .comment-block
      margin-top 0

      &:after
        width 100%

  .comment-add

    &.is-subcomment
      padding 15px 0
      margin-top 0
      border-top 0

  .comment-main
    padding 15px 0


.comment-block__reviews
  max-width calc(100% - 50px)
  margin-left auto

.comment-block__reviews-show
  color ui-cl-color-eucalypt
  font-size font-size-small
  font-weight font-weight-bold
  display inline-flex
  align-items flex-start

  &:before
    content '↳'
    display block
    width 7px
    margin-right 5px
    font-size font-size-small

  .comment-main__pic
    width 30px
    height 30px
.subcomments
  &.fade-enter-active,
  &.fade-leave-active
    transition all .2s ease-in-out
  &.fade-enter,
  &.fade-leave-to
    opacity 0
  .comment-main
    padding-top 15px !important
    margin-bottom 0
  .comment-add__pic
    width 30px
    height 30px
    font-size font-size-super-small
  .comment-main__pic
    margin-right 0
</style>
