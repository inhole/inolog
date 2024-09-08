<script setup lang="ts">
import { defineProps, onBeforeMount, onMounted } from 'vue'
import Comments from '@/components/comment/Comments.vue'
import { usePostStore } from '@/stores/Post'
import { useUserStore } from '@/stores/User'
import { useLikesStore } from '@/stores/Likes'

// router 설정 > index.ts 에서 지정한 postId 가져오기
const props = defineProps<{
  postId: number | string
}>()
const postId = Number(props.postId)

const postStore = usePostStore()
const userStore = useUserStore()
const likesStore = useLikesStore()

onBeforeMount(() => {
  likesStore.getLikes(postId)
  postStore.upHits(postId)
})

onMounted(() => {
  postStore.getPost(postId)
})
</script>

<template>
  <el-row>
    <el-col :spen="22" :offset="1">
      <div class="title">
        {{ postStore.state.post.title }}
        <span class="hits-likes">
          <span class="hits"
            ><el-icon :size="15"><View /></el-icon> {{ postStore.state.post.hits }}</span
          >
          <template v-if="userStore.state.profile != null">
            <template v-if="likesStore.state.likes.likesYn == false">
              <span class="likes" @click="likesStore.insertLikes(postId)">
                <el-icon :size="15"><Pear /></el-icon>
                {{ likesStore.state.likes.likesCount }}</span
              >
            </template>
            <template v-else>
              <span class="likes" style="color: red" @click="likesStore.deleteLikes(postId)">
                <el-icon :size="15"><Pear /></el-icon>
                {{ likesStore.state.likes.likesCount }}</span
              >
            </template>
          </template>
          <template v-else>
            <span class="likes">
              <el-icon :size="15"><Pear /></el-icon>
              {{ likesStore.state.likes.likesCount }}</span
            >
          </template>
        </span>
      </div>
    </el-col>
  </el-row>

  <el-row>
    <el-col :span="10" :offset="8">
      <div class="title">
        <div class="sub">
          <span class="category">{{ postStore.state.post.categoryName }}</span>
          <span class="regDate">Posted in {{ postStore.state.post.getDisplayRegDate() }}</span>
        </div>
      </div>
    </el-col>
  </el-row>

  <el-row>
    <el-col>
      <!--      <div class="content">-->
      <!--        {{ postStore.state.post.content }}-->
      <!--      </div>-->
      <div class="content" v-html="postStore.state.post.content" />

      <div class="footer" v-if="userStore.state.profile != null">
        <el-button type="primary" size="small" plain @click="postStore.edit(postId)"
          >수정
        </el-button>
        <el-button type="danger" size="small" plain @click="postStore.remove(postId)"
          >삭제
        </el-button>
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
  .hits-likes {
    margin-right: 1rem;
    font-size: 0.8rem;
    display: block;
    text-align: right;
    .hits {
      margin-right: 0.3rem;
    }
  }
}

.sub {
  margin-top: 0.5rem;
  margin-bottom: 1rem;
  font-weight: 300;

  .category {
    margin-right: 0.8rem;
    font-size: 0.8rem;
    font-family: Georgia, serif;
  }

  .regDate {
    font-size: 0.73rem;
  }
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

<style>
img {
  width: 100%;
}
</style>
