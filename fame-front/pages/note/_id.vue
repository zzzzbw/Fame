<template>
  <div id="note">
    <div v-highlight class="markdown-body" v-html="note.content"></div>

    <comment v-if="note.allowComment" :article-id="note.id"></comment>
    <big-img :visible.sync="showDialog" :img="img"></big-img>
  </div>
</template>

<script type="text/ecmascript-6">
import Comment from '~/components/Comment.vue'
import BigImg from '~/components/BigImg.vue'


export default {
  head () {
    return { title: `${this.note.title}` }
  },
  components: {
    Comment,
    BigImg
  },
  data () {
    return {
      showDialog: false,
      img: ''
    }
  },
  computed: {
    note () {
      return this.$store.state.note.detail
    }
  },
  fetch ({ store, params }) {
    return store.dispatch('getNote', params.id)
  },
  mounted () {
    this.mountedEvent()
  },
  methods: {
    mountedEvent () {
      const markdown = document.getElementById('note').getElementsByClassName('markdown-body')[0]
      const imgs = markdown.getElementsByTagName('img')
      const _this = this
      for (let i = 0; i < imgs.length; i++) {
        imgs[i].addEventListener('click', (e) => {
          e.stopPropagation()
          _this.showDialog = true
          _this.img = imgs[i].getAttribute('src')
        })
      }
    }
  }
}
</script>
