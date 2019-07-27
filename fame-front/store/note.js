export const state = () => ({
  menu: [],
  detail: {}
})

export const mutations = {
  SET_DETAIL(state, data) {
    state.detail = data
  },
  SET_MENU(state, data) {
    state.menu = data
  }
}
