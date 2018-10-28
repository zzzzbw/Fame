<template>
  <div id="article">
    <h2 class="article-title text-bold">{{article.title}}</h2>
    <div class="article-info">
      <p class="article-category"><span class="icon-folder"></span> {{article.category | formatCategory}}</p>
      <p class="article-date"><span class="icon-calendar"></span> {{article.created | time('yyyy-MM-dd')}}</p>
      <p class="article-date"><span class="icon-eye"></span> {{article.hits}}</p>
    </div>

    <div class="markdown-body" v-html="article.content" v-highlight>
    </div>
    <div class="article-tags">
      <label class="label-tags">Tags:</label>
      <span v-for="tag in $util.stringToTags(article.tags)" :key="tag" class="article-tag">
        #{{tag}}
      </span>
    </div>
    <comment :article-id="article.id"></comment>

    <big-img
      :visible.sync="isBigImg"
      :img="img">
    </big-img>
  </div>
</template>

<script type="text/ecmascript-6">
  import Comment from '~/components/Comment.vue'
  import BigImg from '~/components/BigImg.vue'

  export default {
    head () {
      return { title: `${this.article.title}` }
    },
    fetch ({ store, params }) {
      return store.dispatch('getArticle', params.id)
    },
    data () {
      return {
        isBigImg: false,
        img: ''
      }
    },
    components: {
      Comment,
      BigImg
    },
    computed: {
      article () {
        return this.$store.state.article.detail
      }
    },
    methods: {
      initEvent () {
        const markdown = document.getElementById('article').getElementsByClassName('markdown-body')[0]
        const imgs = markdown.getElementsByTagName('img')
        let _this = this
        for (let i = 0; i < imgs.length; i++) {
          imgs[i].addEventListener('click', (e) => {
            e.stopPropagation()
            _this.isBigImg = true
            _this.img = imgs[i].getAttribute('src')
          })
        }
      }
    },
    mounted () {
      this.initEvent()
    }
  }
</script>

<style>
  #article .markdown-body img {
    max-width: 100%;
    margin: .5rem auto;
    display: block;
    text-align: center;
    border-radius: 4px;
    transition: all .25s;
    opacity: .9;
    cursor: zoom-in;
  }
</style>

<style scoped>
  .article-title {
    color: #34495e;
    margin: 1.2em 0 0;
    font-size: 2.0em;
  }

  .article-info {
  }

  .article-date {
    color: #50596c;
    display: inline-block;
    margin-left: 8px;
  }

  .article-category {
    color: #50596c;
    display: inline-block;
    margin-right: 8px;
  }

  .article-tags {
    margin: 15px 0;
  }

  .article-tags .label-tags {
    margin-right: 6px;
    font-size: 16px;
    font-weight: 600;
    color: #34495e;
  }

  .article-tags .article-tag {
    font-weight: bold;
    color: #5764c6;
    margin: 0 0.2em;
  }

  @media screen and (max-width: 600px) {
    .article-title {
      font-size: 1.8em;
    }
  }

</style>
