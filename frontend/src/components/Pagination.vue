<template>
  <ul class="catalog__pagination pagination">
    <li class="pagination__item">
      <button
        class="pagination__link pagination__link--arrow"
        :class="{'pagination__link--disabled': page === prevPage}"
        @click.prevent="paginate(prevPage)"
        aria-label="Предыдущая страница"
      >
        <svg width="8" height="14" fill="black">
          <use xlink:href="#icon-arrow-left" />
        </svg>
      </button>
    </li>

    <li class="pagination__item" v-for="pageNumber in pages" :key="pageNumber">
      <a
        href="#"
        class="pagination__link"
        :class="{
          'pagination__link--current': pageNumber === page,
          'pagination__link--disabled': pageNumber === page
        }"
        @click.prevent="paginate(pageNumber)"
      >
        {{ pageNumber }}
      </a>
    </li>

    <li class="pagination__item">
      <button
        class="pagination__link pagination__link--arrow"
        :class="{ 'pagination__link--disabled': page === nextPage }"
        @click.prevent="paginate(nextPage)"
        aria-label="Следующая страница"
      >
        <svg width="8" height="14" fill="currentColor">
          <use xlink:href="#icon-arrow-right" />
        </svg>
      </button>
    </li>
  </ul>
</template>

<script>
export default {
  model: {
    prop: 'page',
    event: 'paginate'
  },
  props: ['page', 'count', 'perPage'],

  computed: {
    pages () {
      return Math.ceil(this.count / this.perPage)
    },
    prevPage () {
      if (this.page === 1) {
        return this.page
      }
      return this.page - 1
    },
    nextPage () {
      if (this.page >= this.pages) {
        return this.page
      }
      return this.page + 1
    }
  },
  methods: {
    paginate (page) {
      this.$emit('paginate', page)
    }
  }
};
</script>


<style lang="stylus">
@import '../assets/stylus/base/vars.styl'

.catalog__pagination
  margin-top 30px

.pagination__link--arrow
  border-radius border-small

.pagination__link
  padding: 10px 3px
</style>
