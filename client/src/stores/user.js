import { defineStore } from 'pinia'
import { getProfile } from '@/api/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user') || 'null')
  }),
  
  getters: {
    isLoggedIn: state => !!state.token,
    isAdmin: state => state.user?.isAdmin === 1,
    nickname: state => state.user?.nickname || '用户',
    avatar: state => state.user?.avatar || null
  },
  
  actions: {
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
    },
    
    setUser(user) {
      this.user = user
      localStorage.setItem('user', JSON.stringify(user))
    },
    
    async fetchUserInfo() {
      try {
        const res = await getProfile()
        this.setUser(res.data)
        return res.data
      } catch (error) {
        this.logout()
        throw error
      }
    },
    
    logout() {
      this.token = ''
      this.user = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
})
