<template>
  <markdown-editor v-model="content" :configs="configs" :highlight="true" ref="markdownEditor"
                   preview-class="markdown-body">
  </markdown-editor>
</template>

<script type="text/ecmascript-6">
  import markdownEditor from 'vue-simplemde/src/markdown-editor'
  import hljs from 'highlight.js';

  window.hljs = hljs
  export default {
    name: 'md-editor',
    model: {
      prop: 'value',
      event: 'change'
    },
    props: {
      value: String
    },
    components: {
      markdownEditor
    },
    data: function () {
      return {
        content: '',
        configs: {
          autoDownloadFontAwesome: false, // 禁止下载fontAwesome
          status: false, // 禁用底部状态栏
          spellChecker: false, // 禁用拼写检查
          toolbar: [{
            name: 'bold',
            action: function toggleBold (editor) {
              editor.toggleBold()
            },
            className: 'icon-bold',
            title: '加粗'
          }, {
            name: 'italic',
            action: function toggleItalic (editor) {
              editor.toggleItalic()
            },
            className: 'icon-italic',
            title: 'icon-italic'
          }, {
            name: 'strikethrough',
            action: function toggleStrikethrough (editor) {
              editor.toggleStrikethrough()
            },
            className: 'icon-strikethrough',
            title: '删除线'
          }, {
            name: 'heading',
            action: function toggleHeadingSmaller (editor) {
              editor.toggleHeadingSmaller()
            },
            className: 'icon-header',
            title: '标题'
          }, {
            name: 'code',
            action: function toggleCodeBlock (editor) {
              editor.toggleCodeBlock()
            },
            className: 'icon-code',
            title: '代码块'
          }, {
            name: 'quote',
            action: function toggleBlockquote (editor) {
              editor.toggleBlockquote()
            },
            className: 'icon-quote-left',
            title: '引用'
          }, {
            name: 'unordered-list',
            action: function toggleUnorderedList (editor) {
              editor.toggleUnorderedList()
            },
            className: 'icon-list-ul',
            title: '无序列表'
          }, {
            name: 'ordered-list',
            action: function toggleOrderedList (editor) {
              editor.toggleOrderedList()
            },
            className: 'icon-list-ol',
            title: '有序列表'
          }, {
            name: 'link',
            action: function drawLink (editor) {
              editor.drawLink()
            },
            className: 'icon-link',
            title: '插入链接'
          }, {
            name: 'image',
            action: function drawImage (editor) {
              editor.drawImage()
            },
            className: 'icon-image',
            title: '插入图片'
          }, '|', {
            name: 'preview',
            action: function togglePreview (editor) {
              editor.togglePreview()
            },
            className: 'icon-eye',
            title: '预览'
          }, {
            name: 'fullscreen',
            action: this.toggleFullScreen,
            className: 'icon-arrows-alt',
            title: '全屏'
          }, {
            name: 'side-by-side',
            action: this.toggleSideBySide,
            className: 'icon-columns',
            title: '分屏'
          }]
        }
      }
    },
    methods: {
      toggleFullScreen (editor) {
        if (this.isFullScreen) {
          this.isFullScreen = false
          this.$root.$emit('indexDown')
        } else {
          this.isFullScreen = true
          this.$root.$emit('indexUp')
        }
        editor.toggleFullScreen()
      },
      toggleSideBySide (editor) {
        if (!editor.isFullscreenActive()) {
          this.isFullScreen = true
          this.$root.$emit('indexUp')
        }
        editor.toggleSideBySide()
      },
      escDown () {
        let _this = this
        document.onkeydown = function (event) {
          if (event.code === 'Escape') {
            if (_this.isFullScreen) {
              _this.$root.$emit('indexDown')
              _this.isFullScreen = false
            }
          }
        }
      }
    },
    mounted () {
      this.escDown()
    },
    watch: {
      value (val) {
        this.value = val
        this.content = this.value
      },
      content () {
        this.$emit('change', this.content)
      }
    }
  }
</script>
<style>

</style>

<style scoped>
  .markdown-editor {
    line-height: 1.15;
  }
</style>
