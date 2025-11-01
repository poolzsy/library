import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getUserInfo } from '@/api/user' 

export const useUserStore = defineStore('user', () => {
  const userInfo = ref(null)

  // 获取并设置用户信息
  async function fetchUserInfo() {
    try {
      // 尝试调用一个需要认证的接口，比如获取用户信息
      const response = await getUserInfo() 
      userInfo.value = response.data 
      return true
    } catch (error) {
      // 如果调用失败，说明 token 无效
      console.error('获取用户信息失败, token 可能已过期', error)
      resetUserInfo()
      return false
    }
  }

  // 重置用户信息和本地存储
  function resetUserInfo() {
    userInfo.value = null
    localStorage.removeItem('token')
  }

  // 判断用户是否已登录
  const isLoggedIn = () => {
    return !!userInfo.value
  }

  return { userInfo, fetchUserInfo, resetUserInfo, isLoggedIn }
})
