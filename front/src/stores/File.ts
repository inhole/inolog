import { defineStore } from 'pinia'
import { reactive } from 'vue'
import { container } from 'tsyringe'
import { useRouter } from 'vue-router'
import FileRepository from '@/repository/FileRepository'
import { QuillEditor } from '@vueup/vue-quill'

export const useFileStore = defineStore('file', () => {
  const state = reactive({
    editor: QuillEditor
  })
  const router = useRouter()
  const FILE_REPOSITORY = container.resolve(FileRepository)

  const editorOption = {
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
  }

  async function imageHandler() {
    // const editor = quillEditor.value?.getQuill()
    const editor = state.editor

    // input 세팅
    const input = document.createElement('input')
    input.setAttribute('type', 'file')
    input.setAttribute('accept', 'image/*')
    input.multiple = true

    input.click()

    // 이미지 선택후
    input.onchange = async () => {
      const files = input.files
      if (!files) return

      const uploadPromises = Array.from(files).map(async (file) => {
        const formData = new FormData()
        formData.append('image', file)

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
      })

      console.log('uploadPromises :: ' + JSON.stringify(uploadPromises))

      const imageUrls = await Promise.all(uploadPromises)

      // Quill 에디터에 이미지 삽입
      imageUrls.forEach((url) => {
        const range = editor.getSelection()
        editor.insertEmbed(range.index, 'image', url)
      })
    }
  }

  return { state, editorOption, imageHandler }
})
