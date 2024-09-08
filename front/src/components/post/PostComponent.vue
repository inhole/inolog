<script setup lang="ts">
import Post from '@/entity/post/Post'

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

  <div class="sub-title">
    <span class="category-name">{{ props.post.categoryName }}</span>
    <span class="reg-date">{{ props.post.getDisplaySimpleRegDate() }}</span>
  </div>

  <!--  <div class="content">-->
  <!--    {{ props.post.getDisplayContent() }}-->
  <!--  </div>-->

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

.sub-title {
  margin-top: 0.4rem;
  .category-name {
    font-size: 0.85rem;
    color: #beb78c;
    margin-right: 0.3rem;
  }
  .reg-date {
    font-size: 0.75rem;
    color: #6b6b6b;
  }
}

.content {
  margin-top: 0.4rem;
  font-size: 0.9rem;
}
</style>
