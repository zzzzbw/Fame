<template>
  <div>
    <div class="tag-list">
      <a v-for="tag in tags" :key="tag.id" class="tag-link" @click="changeArticles(tag.name)">
        <span class="tag chip" v-bind:class="{'tag-active':tag.name===activeTag}">
            {{tag.name}}
        </span>
      </a>
    </div>
    <transition name="flow" mode="out-in">
      <div class="tag-content" v-if="show">
        <div class="divider"></div>
        <div class="tag-title">{{tagTitle}}</div>
        <ul class="tag-ul">
          <li v-for="article in articles" :key="article.id" class="article-title">
            <nuxt-link :to="{ path: '/article/'+article.id }" class="text-primary">{{article.title}}</nuxt-link>
          </li>
        </ul>
      </div>
    </transition>
  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    head () {
      return { title: `标签` }
    },
    fetch ({ store }) {
      return store.dispatch('getTags')
    },
    computed: {
      tags () {
        return this.$store.state.tag.data
      }
    },
    data: function () {
      return {
        articles: [],
        tagTitle: '',
        activeTag: '',
        show: false
      }
    },
    methods: {
      changeArticles (name) {
        let tag = this.tags.find(tag => Object.is(tag.name, name))
        if (tag !== null) {
          this.activeTag = name
          this.show = false
          this.tagTitle = tag.name
          this.articles = tag.articles
          this.show = true
        }
      }
    }
  }
</script>

<style scoped>
  .tag-list {
    margin-top: 40px;
    margin-bottom: 30px;
    text-align: center;
  }

  .tag-link {
    text-decoration: none;
    text-underline: none;
  }

  .tag {
    background: #f0f1f4;
    border-radius: 5rem;
    color: #727e96;
    margin: .3rem .1rem;
    padding: .1rem .5rem;
  }

  .tag-link .tag:hover {
    background: #acb3c2;
    color: #f0f1f4;
    box-shadow: 0 0 3px rgba(14, 14, 14, 0.3);
    margin-top: -5px;
    transition: all 0.2s;
  }

  .tag-active {
    background: #acb3c2;
    color: #f0f1f4;
    box-shadow: 0 0 3px rgba(14, 14, 14, 0.3);
    margin-top: -5px;
    transition: all 0.2s;
  }

  .tag-content {
    margin: 20px auto;
    width: 75%;
  }

  .tag-title {
    margin-right: 6px;
    font-size: 24px;
    font-weight: 600;
    color: #34495e;
  }

  .tag-title:before {
    content: "#";
    margin-right: 5px;
    color: #5764c6;
    font-size: 1.2em;
    font-weight: 700;
  }

  .article-title a {
    text-decoration: none;
    font-size: 1.1em;
  }

</style>
