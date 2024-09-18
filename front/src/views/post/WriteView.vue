<script setup lang="ts">
import { usePostStore } from '@/stores/Post'
import { useCategoryStore } from '@/stores/Category'
import { computed, nextTick, onMounted, ref } from 'vue'
import { QuillEditor } from '@vueup/vue-quill'
import { useFileStore } from '@/stores/File'

const postStore = usePostStore()
const categoryStore = useCategoryStore()
const fileStore = useFileStore()

const quillEditor = ref<typeof QuillEditor | null>(null)

const categoryId = computed({
  get() {
    return postStore.state.postWrite.category.id === 0 ? '' : postStore.state.postWrite.category.id
  },
  set(value) {
    postStore.state.postWrite.category.id = value === '' ? 0 : value
  }
})

onMounted(async () => {
  categoryStore.getList()
  await nextTick()

  // const editorInstance = quillEditor.value?.getQuill()
  fileStore.state.editor = quillEditor.value?.getQuill()
  if (fileStore.state.editor) {
    fileStore.state.editor.getModule('toolbar').addHandler('image', fileStore.imageHandler)
  }
})

// 글 작성 submit
function submitPost() {
  const editor = quillEditor.value?.getQuill()
  const html = editor.root.innerHTML

  postStore.state.postWrite.content = html
  postStore.write()
}
</script>

<template>
  <el-form label-position="top">
    <el-form-item label="제목">
      <el-input
        v-model="postStore.state.postWrite.title"
        size="large"
        type="text"
        placeholder="제목을 입력해주세요"
      />
    </el-form-item>

    <el-form-item label="카테고리">
      <el-select v-model="categoryId" placeholder="선택" size="large">
        <el-option
          v-for="category in categoryStore.state.categoryList"
          :key="category.id"
          :label="category.name"
          :value="category.id"
        />
      </el-select>
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
      <!--      <el-input v-model="postStore.state.postWrite.content" type="textarea" rows="15" alt="내용" />-->
    </el-form-item>

    <el-form-item>
      <el-button type="primary" style="width: 100%" @click="submitPost()">등록완료</el-button>
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
