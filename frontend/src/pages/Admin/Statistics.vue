<!-- eslint-disable vue/html-indent -->
<template>
  <div>
    <ul v-if="statsDataAccount || statsDataPosts || statsDataComment || statsDataLikes" class="admin__pages-list">
      <li class="admin__pages-item">
        <div class="admin__pages-img">
          <img src="../../../public/static/img/admin/admin_1.png" alt="Пользователи">
        </div>
        <div class="admin__pages-info">
          <h4 class="admin__pages-title">Пользователей зарегестрировано</h4>
            <span class="admin__pages-statistic">{{ statsDataAccount?.count }}</span>
        </div>
      </li>
      <li class="admin__pages-item">
        <div class="admin__pages-img">
          <img src="../../../public/static/img/admin/admin_3.png" alt="Публикации">
        </div>
        <div class="admin__pages-info">
          <h4 class="admin__pages-title">Публикаций создано</h4>
          <span class="admin__pages-statistic">{{ statsDataPosts?.count }}</span>
        </div>
      </li>
      <li class="admin__pages-item">
        <div class="admin__pages-img">
          <img src="../../../public/static/img/admin/admin_2.png" alt="Комментарии">
        </div>
        <div class="admin__pages-info">
          <h4 class="admin__pages-title">Комментариев оставлено</h4>
          <span class="admin__pages-statistic">{{ statsDataComment?.count }}</span>
        </div>
      </li>
      <li class="admin__pages-item">
        <div class="admin__pages-img">
          <img src="../../../public/static/img/admin/admin_4.png" alt="Лайки">
        </div>
        <div class="admin__pages-info">
          <h4 class="admin__pages-title">Лайков поставлено</h4>
          <span class="admin__pages-statistic">{{ statsDataLikes?.count }}</span>
        </div>
      </li>
    </ul>
    <div v-else-if="statisticsError" class="statistics-error">
      <h3 class="statistics-error__title">404. Данные не найдены.</h3>
      <p class="statistics-error__status">В данный момент сервер не может получить данные.</p>
      <div class="statistics-error__image"></div>
      <button class="statistics-error__button" @click.prevent="loadData">Попробовать снова</button>
    </div>
    <div v-else-if="!statsDataAccount || !statsDataPosts || !statsDataComment || !statsDataLikes" class="admin__pages-users panel loading__info">
      Загружаем данные статистики...
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import moment from 'moment'

export default {
  name: 'AdminStatistics',
  data() {
    return {
      statsDataAccount: null,
      statsDataPosts: null,
      statsDataComment: null,
      statsDataLikes: null,
      statisticsError: false,
      statisticsLoading: false,
    }
  },
  created() {
    this.loadStatisticsAccount();
    this.loadStatisticsPosts();
    this.loadStatisticsComment();
    this.loadStatisticsLikes();
  },
  methods: {
    loadStatisticsByType (type) {
    const today = moment();
    const formattedToday = today.format('YYYY-MM-DD');
    const lastDayOfYear = today.endOf('year').format('YYYY-MM-DD');
    const firstMonthOfYear = today.startOf('year').format('YYYY-MM-DD');
    const url =
    `/admin-console/statistic/${type}?date=${formattedToday}T00:00:00.735Z&firstMonth=${firstMonthOfYear}T00:00:00.735Z&lastMonth=${lastDayOfYear}T00:00:00.735Z`;

    setTimeout(() => {
      axios.get(url)
      .then(response => {
        if (type === 'account') {
          this.statsDataAccount = response.data;
        } else if (type === 'post') {
          this.statsDataPosts = response.data;
        } else if (type === 'comment') {
          this.statsDataComment = response.data;
        } else if (type === 'like') {
          this.statsDataLikes = response.data;
        }
      })
      .catch(() => {
          this.statisticsError = true;
          this.statisticsLoading = false;
        })
      .then(() => {
          this.statisticsLoading = false;
        });
    }, 1000)

    },
    loadStatisticsAccount() {
      this.loadStatisticsByType('account');
    },
    loadStatisticsPosts() {
      this.loadStatisticsByType('post');
    },
    loadStatisticsComment() {
      this.loadStatisticsByType('comment');
    },
    loadStatisticsLikes() {
      this.loadStatisticsByType('like');
    },
  },
};
</script>
