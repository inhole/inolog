import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/post/HomeView.vue'
import WriteView from '../views/post/WriteView.vue'
import ReadView from '../views/post/ReadView.vue'
import EditView from '../views/post/EditView.vue'
import LoginView from '../views/user/LoginView.vue'
import CommentWrite from '@/views/comment/CommentWriteView.vue'
import CategoryView from '@/views/category/CategoryView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/write',
      name: 'write',
      component: WriteView
    },
    {
      path: '/post/:postId',
      name: 'post',
      component: ReadView,
      props: true
    },
    {
      path: '/edit/:postId',
      name: 'edit',
      component: EditView,
      props: true
    },
    {
      path: '/comment/write/:postId',
      name: 'commentWrite',
      component: CommentWrite,
      props: true
    },
    {
      path: '/category',
      name: 'category',
      component: CategoryView
    }
    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (About.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import('../views/AboutView.vue')
    // }
  ]
})

export default router
