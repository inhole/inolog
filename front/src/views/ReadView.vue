<script setup lang="ts">
import { defineProps, onMounted, reactive } from 'vue'
import { container } from 'tsyringe'
import PostRepository from '@/repository/PostRepository'
import Post from '@/entity/post/Post'

// router 설정 > index.ts 에서 지정한 postId 가져오기
const props = defineProps<{
  postId: number
}>()

const POST_REPOSITORY = container.resolve(PostRepository)

// type StateType = {
//   post: Post | null
// }
// const state = reactive<StateType>({
const state = reactive({
  post: new Post()
})

function getPost() {
  POST_REPOSITORY.get(props.postId)
    .then((post: Post) => {
      state.post = post
    })
    .catch((e) => {
      console.log(e)
    })
}

onMounted(() => {
  getPost()
})
</script>

<template>
  <el-row>
    <el-col :spen="22" :offset="1">
      <div class="title">{{ state.post.title }}</div>
    </el-col>
  </el-row>

  <el-row>
    <el-col :span="10" :offset="8">
      <div class="title">
        <div class="regDate">Posted in {{ state.post.getDisplayRegDate() }}</div>
      </div>
    </el-col>
  </el-row>

  <el-row>
    <el-col>
      <div class="content">
        {{ state.post.content }}
      </div>

      <div class="footer">
        <el-button type="primary" size="small" plain>수정</el-button>
        <el-button type="danger" size="small" plain>삭제</el-button>
      </div>
    </el-col>
  </el-row>

  <el-row class="comments">
    <el-col>
      <Comments />
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">
.title {
  font-size: 1.8rem;
  font-weight: 400;
  text-align: center;
}

.regDate {
  margin-top: 0.5rem;
  font-size: 0.78rem;
  font-weight: 300;
}

.content {
  margin-top: 0.8rem;
  font-weight: 300;

  word-break: break-all;
  white-space: break-spaces;
  line-height: 1.4;
  min-height: 5rem;
}

.footer {
  text-align: end;
  //display: flex;
}
</style>
