import { defineStore } from 'pinia'
import { userApi } from '../api'

export const useUserStore = defineStore('user', {
  state: () => ({
    info: null,
    role: localStorage.getItem('role') || ''
  }),
  actions: {
    async fetchInfo() {
      try {
        const res = await userApi.getInfo()
        this.info = res.data
        this.role = res.data.role
        localStorage.setItem('role', res.data.role)
      } catch (e) {
        console.error('获取用户信息失败', e)
      }
    },
    logout() {
      localStorage.removeItem('token')
      localStorage.removeItem('role')
      this.info = null
      this.role = ''
    }
  }
})
