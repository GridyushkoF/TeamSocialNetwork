import storage from '@/requests/storage';

export default {
  namespaced: true,
  state: {
    fileName: null,
  },
  getters: {
    getStoragePostPhoto: (s) => s.fileName,
  },
  mutations: {
    setStoragePostPhoto: (s, value) => {
      s.fileName = value;
    },
  },
  actions: {
    async apiStoragePostPhoto({ commit }, file) {
      const data = new FormData();
      data.append('file', file);
      const response = await storage.api(data);
      commit('setStoragePostPhoto', response.data.fileName);
    },
  },
};
