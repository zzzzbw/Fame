export const state = () => ({
  header: [],
  detail: {},
  list: {
    data: [],
    totalPage: 0,
    currentPage: 0,
  },
})

export const mutations = {
  SET_HEADER(state, data) {
    state.header = data
  },
  SET_DETAIL(state, data) {
    state.detail = data
  },
  SET_LIST(state, data) {
    state.list = data
  },
}
