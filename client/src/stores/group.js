import { defineStore } from 'pinia'
import { getGroupList, createGroup, updateGroup, deleteGroup } from '@/api/group'

export const useGroupStore = defineStore('group', {
  state: () => ({
    list: [],
    selectedId: null
  }),
  
  getters: {
    defaultGroup: state => state.list.find(g => g.isDefault === 1),
    selectedGroup: state => state.selectedId ? state.list.find(g => g.id === state.selectedId) : null
  },
  
  actions: {
    async fetchList() {
      const res = await getGroupList()
      this.list = res.data || []
      // 默认选中默认分组
      const defaultGroup = this.list.find(g => g.isDefault === 1)
      if (defaultGroup && !this.selectedId) {
        this.selectedId = defaultGroup.id
      }
    },
    
    async create(data) {
      const res = await createGroup(data)
      await this.fetchList()
      return res.data
    },
    
    async update(id, data) {
      await updateGroup(id, data)
      await this.fetchList()
    },
    
    async remove(id) {
      await deleteGroup(id)
      if (this.selectedId === id) {
        this.selectedId = null
      }
      await this.fetchList()
    },
    
    selectGroup(id) {
      this.selectedId = id
    }
  }
})
