export const state = () => ({
  header: [],
})

export const mutations = {
  SET_HEADER(state, data) {
    state.header = data
  },
}
