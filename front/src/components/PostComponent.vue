<script setup lang="ts">
import type Post from '@/entity/post/Post'

const props = defineProps<{
  post: Post
}>()
</script>

<template>
  <div class="title">
    <router-link :to="{ name: 'post', params: { postId: props.post.id } }">{{
      props.post.title
    }}</router-link>
  </div>

  <div class="regDate">{{ props.post.getDisplaySimpleRegDate() }}</div>

  <div class="content">
    {{ props.post.getDisplayContent() }}
  </div>

  <!--  <div class="readMore">-->
  <!--    <router-link :to="{ name: 'post', params: { postId: props.post.id } }">Read more </router-link>-->
  <!--  </div>-->
</template>

<style scoped lang="scss">
.title {
  a {
    color: inherit;
    text-decoration: none;
    font-size: 1.4rem;
    font-weight: 400;

    &:hover {
      text-decoration: underline;
    }
  }
}

.regDate {
  margin-top: 0.4rem;
  font-size: 0.75rem;
  color: #6b6b6b;
}

.content {
  margin-top: 0.4rem;
  font-size: 0.9rem;
}
</style>
