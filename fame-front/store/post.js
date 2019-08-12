export const state = () => ({
  list: {
    data: [],
    totalPage: 0,
    currentPage: 0
  },
  detail: {}
})

export const mutations = {
  SET_DETAIL(state, data) {
    state.detail = data
  },
  SET_LIST(state, data) {
    state.list = data
  }
}
