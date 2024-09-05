<script setup lang="ts">
import { usePostStore } from '@/stores/Post'
import { useCategoryStore } from '@/stores/Category'
import { computed, nextTick, onMounted, reactive, ref } from 'vue'
import Quill from '@/components/post/Quill.vue'
import { QuillEditor } from '@vueup/vue-quill'

const postStore = usePostStore()
const categoryStore = useCategoryStore()
const quillEditor = ref<typeof QuillEditor | null>(null)

const state = reactive({
  editorOption: {
    placeholder: '내용을 입력해주세요.',
    modules: {
      toolbar: [
        ['bold', 'italic', 'underline', 'strike'],
        ['blockquote', 'code-block'],
        [{ header: 1 }, { header: 2 }],
        [{ list: 'ordered' }, { list: 'bullet' }],
        [{ color: [] }, { background: [] }],
        [{ font: [] }],
        [{ align: [] }],
        ['clean'],
        ['link', 'image']
      ]
    }
    // handlers: {
    //   image: imageHandler
    // }
  }
})

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

  const editorInstance = quillEditor.value?.getQuill()
  if (editorInstance) {
    editorInstance.getModule('toolbar').addHandler('image', imageHandler)
  }
})

async function imageHandler() {
  console.log('[imageHandler]::: S')
  const editor = quillEditor.value?.getQuill()

  const input = document.createElement('input')
  input.setAttribute('type', 'file')
  input.setAttribute('accept', 'image/*')
  input.multiple = true

  input.click()

  input.onchange = async () => {
    const files = input.files
    if (!files) return

    const uploadPromises = Array.from(files).map(async (file) => {
      const formData = new FormData()
      formData.append('image', file)

      try {
        // 서버에 이미지 업로드 요청
        const response = await fetch('/api/upload', {
          method: 'POST',
          body: formData
        })

        if (!response.ok) {
          throw new Error('Image upload failed')
        }

        const data = await response.json()
        return data.url // 서버에서 반환된 이미지 URL
      } catch (error) {
        console.error('Image upload failed:', error)
        return ''
      }
    })

    const imageUrls = await Promise.all(uploadPromises)

    // Quill 에디터에 이미지 삽입
    imageUrls.forEach((url) => {
      const range = editor.getSelection()
      editor.insertEmbed(range.index, 'image', url)
    })
  }
}

// 글 작성 submit
function submitPost() {
  if (!quillEditor.value) {
    console.error('QuillEditor error')
    return false
  }

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
          :options="state.editorOption"
        />
      </div>
      <!--      <el-input v-model="postStore.state.postWrite.content" type="textarea" rows="15" alt="내용" />-->
    </el-form-item>

    <el-form-item>
      <el-button type="primary" style="width: 100%" @click="postStore.write()">등록완료</el-button>
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
