<script setup lang="ts">
import { useCommentStore } from '@/stores/comment'

const props = defineProps<{
  postId: number | String
}>()

const postId = Number(props.postId)

const store = useCommentStore()
</script>

<template>
  <el-form label-position="top">
    <el-form-item label="작성자">
      <el-input
        v-model="store.state.commentWrite.author"
        id="author"
        placeholder="1~8글자로 입력해주세요."
        minlength="1"
        maxlength="8"
      ></el-input>
    </el-form-item>

    <el-form-item label="비밀번호">
      <el-input
        v-model="store.state.commentWrite.password"
        type="password"
        id="password"
        placeholder="6~30글자로 입력해주세요."
        minlength="6"
        maxlength="30"
      ></el-input>
    </el-form-item>

    <el-form-item label="내용">
      <el-input
        v-model="store.state.commentWrite.content"
        id="content"
        placeholder="10~1000자까지 입력해주세요."
        type="textarea"
        :rows="15"
        :autosize="{ minRows: 5, maxRows: 4 }"
        minlength="10"
        maxlength="1000"
      ></el-input>
    </el-form-item>

    <el-form-item>
      <el-button
        type="primary"
        style="width: 100%"
        class="button"
        @click="store.writeComment(postId)"
        >등록하기</el-button
      >
    </el-form-item>
  </el-form>
</template>

<style scoped lang="scss"></style>
