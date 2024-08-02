<script setup lang="ts">
import { defineProps, onMounted } from 'vue'
import Comments from '@/components/comment/Comments.vue'
import { usePostStore } from '@/stores/Post'

// router 설정 > index.ts 에서 지정한 postId 가져오기
const props = defineProps<{
  postId: number | string
}>()
const postId = Number(props.postId)

const store = usePostStore()

onMounted(() => {
  store.getPost(postId)
})
</script>

<template>
  <el-row>
    <el-col :spen="22" :offset="1">
      <div class="title">{{ store.state.post.title }}</div>
    </el-col>
  </el-row>

  <el-row>
    <el-col :span="10" :offset="8">
      <div class="title">
        <div class="regDate">Posted in {{ store.state.post.getDisplayRegDate() }}</div>
      </div>
    </el-col>
  </el-row>

  <el-row>
    <el-col>
      <div class="content">
        {{ store.state.post.content }}
      </div>

      <div class="footer">
        <el-button type="primary" size="small" plain @click="store.edit(postId)">수정</el-button>
        <el-button type="danger" size="small" plain @click="store.remove(postId)">삭제</el-button>
      </div>
    </el-col>
  </el-row>

  <el-row class="comments">
    <el-col>
      <Comments :postId="postId" />
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
  margin-bottom: 1rem;
  font-size: 0.73rem;
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
