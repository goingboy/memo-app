import { defineStore } from 'pinia'
import { getMemoList, getMemoDetail, createMemo, updateMemo, deleteMemo, getTrashList, restoreMemo, forceDeleteMemo } from '@/api/memo'

export const useMemoStore = defineStore('memo', {
  state: () => ({
    list: [],
    total: 0,
    currentMemo: null,
    trashList: [],
    trashTotal: 0,
    loading: false,
    currentGroupId: null
  }),
  
  actions: {
    async fetchList(params = {}) {
      this.loading = true
      try {
        const res = await getMemoList({
          page: params.page || 1,
          pageSize: params.pageSize || 12,
          groupId: params.groupId || undefined
        })
        this.list = res.data.records || res.data.list || []
        this.total = res.data.total || 0
        this.currentGroupId = params.groupId || null
      } finally {
        this.loading = false
      }
    },
    
    async fetchDetail(id) {
      this.loading = true
      try {
        const res = await getMemoDetail(id)
        this.currentMemo = res.data
        return res.data
      } finally {
        this.loading = false
      }
    },
    
    async create(data) {
      const res = await createMemo(data)
      return res.data
    },
    
    async update(id, data) {
      const res = await updateMemo(id, data)
      return res.data
    },
    
    async remove(id) {
      await deleteMemo(id)
    },
    
    async fetchTrash(params = {}) {
      this.loading = true
      try {
        const res = await getTrashList({
          page: params.page || 1,
          pageSize: params.pageSize || 12
        })
        this.trashList = res.data.records || res.data.list || []
        this.trashTotal = res.data.total || 0
      } finally {
        this.loading = false
      }
    },
    
    async restore(id) {
      await restoreMemo(id)
    },
    
    async forceDelete(id) {
      await forceDeleteMemo(id)
    }
  }
})
