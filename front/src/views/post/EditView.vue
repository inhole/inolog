<script setup lang="ts">
import { defineProps, nextTick, onMounted, reactive, ref } from 'vue'
import { usePostStore } from '@/stores/Post'
import { QuillEditor } from '@vueup/vue-quill'
import { useFileStore } from '@/stores/File'

const props = defineProps<{
  postId: number | any
}>()

const postStore = usePostStore()
const fileStore = useFileStore()

const quillEditor = ref<typeof QuillEditor | null>(null)

function editPost() {
  const editor = quillEditor.value?.getQuill()
  const html = editor.root.innerHTML

  postStore.state.post.content = html
  postStore.editPost(props.postId)
}

onMounted(async () => {
  postStore.getPost(props.postId)
  await nextTick()

  fileStore.state.editor = quillEditor.value?.getQuill()
  if (fileStore.state.editor) {
    const editor = quillEditor.value?.getQuill()
    editor.root.innerHTML = postStore.state.post.content
    fileStore.state.editor.getModule('toolbar').addHandler('image', fileStore.imageHandler)
  }
})
</script>

<template>
  <el-form label-position="top">
    <el-form-item label="제목">
      <el-input
        v-model="postStore.state.post.title"
        size="large"
        type="text"
        placeholder="제목을 입력해주세요"
      />
    </el-form-item>

    <el-form-item label="내용">
      <div class="quill-editor-wrapper">
        <QuillEditor
          ref="quillEditor"
          theme="snow"
          v-model:content="postStore.state.postWrite.content"
          content-type="html"
          :options="fileStore.editorOption"
        />
      </div>
    </el-form-item>

    <el-form-item>
      <el-button type="warning" style="width: 100%" @click="editPost()">수정완료</el-button>
    </el-form-item>
  </el-form>
</template>

<style lang="scss" scoped>
.quill-editor-wrapper {
  height: 30rem; /* Adjust height as needed */
  width: 100%;
  //border: 1px solid #ddd; /* Optional border */
  padding-bottom: 60px; /* Optional padding */
}
</style>
