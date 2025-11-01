import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../layout/Layout.vue'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Layout',
      component: Layout,
      redirect: '/home',
      children: [
        {
          path: 'home',
          name: 'home',
          component: () => import('../views/Home.vue')
        },
        {
          path: 'user',
          name: 'user',
          component: () => import('../views/user/User.vue')
        },
        {
          path: 'role',
          name: 'role',
          component: () => import('../views/role/Role.vue')
        },
        {
          path: 'bookCategory',
          name: 'bookCategory',
          component: () => import('../views/books/BookCategory.vue')
        }
      ],
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/login/Login.vue'),
    },
  ],
})

// 全局前置守卫
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  const token = localStorage.getItem('token')
  const whiteList = ['/login']
  if (token) {
    // 有 token
    if (userStore.isLoggedIn()) {
      // 如果Pinia中已有用户信息，说明已经验证过，直接放行
      if (to.path === '/login') {
        next({ path: '/' })
      } else {
        next()
      }
    } else {
      // Pinia 中没有用户信息，这是首次加载或刷新页面，需要去后端验证 token
      const isTokenValid = await userStore.fetchUserInfo()
      if (isTokenValid) {
        // token 有效，已获取用户信息
        if (to.path === '/login') {
          next({ path: '/' })
        } else {
          next()
        }
      } else {
        // token 无效
        userStore.resetUserInfo()
        next(`/login?redirect=${to.path}`)
      }
    }
  } else {
    // 没有 token
    if (whiteList.includes(to.path)) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
    }
  }
})

export default router
