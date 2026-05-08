<template>
  <div class="memo-detail-page">
    <header class="detail-header">
      <el-button :icon="ArrowLeft" text @click="$router.back()">返回</el-button>
      <div class="header-actions">
        <el-button type="primary" @click="handleEdit">编辑</el-button>
        <el-button type="danger" :icon="Delete" @click="handleDelete">删除</el-button>
      </div>
    </header>
    
    <div v-if="loading" class="loading">
      <el-icon class="is-loading" :size="40"><Loading /></el-icon>
    </div>
    
    <div v-else-if="memo" class="detail-content">
      <div class="detail-meta">
        <span class="memo-group">{{ groupName }}</span>
        <span class="memo-time">更新于 {{ formatTime(memo.updatedAt) }}</span>
      </div>
      <h1 class="detail-title">{{ memo.title || '无标题' }}</h1>
      <div class="detail-body" v-html="renderedContent"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft, Delete, Loading } from '@element-plus/icons-vue'
import { marked } from 'marked'
import dayjs from 'dayjs'
import { useMemoStore } from '@/stores/memo'
import { useGroupStore } from '@/stores/group'

const route = useRoute()
const router = useRouter()
const memoStore = useMemoStore()
const groupStore = useGroupStore()

const loading = ref(true)
const memo = ref(null)

const renderedContent = computed(() => {
  if (!memo.value?.content) return '<p class=\'empty-content\'>暂无内容</p>'
  return marked.parse(memo.value.content)
})

const groupName = computed(() => {
  if (!memo.value?.groupId) return ''
  const group = groupStore.list.find(g => g.id === memo.value.groupId)
  return group?.name || ''
})

const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

onMounted(async () => {
  try {
    memo.value = await memoStore.fetchDetail(route.params.id)
    if (groupStore.list.length === 0) {
      await groupStore.fetchList()
    }
  } catch (error) {
    router.push('/')
  } finally {
    loading.value = false
  }
})

const handleEdit = () => {
  router.push(`/memo/edit/${route.params.id}`)
}

const handleDelete = async () => {
  await ElMessageBox.confirm('确定要删除这篇备忘录吗？', '提示', { type: 'warning' })
  await memoStore.remove(route.params.id)
  ElMessage.success('已移入垃圾站')
  router.push('/')
}
</script>

<style lang="scss" scoped>
.memo-detail-page {
  min-height: 100vh;
  background: #0A0A0A;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: #1A1A1A;
  border-bottom: 1px solid #333;
  position: sticky;
  top: 0;
  z-index: 10;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
  color: #666;
}

.detail-content {
  max-width: 900px;
  margin: 0 auto;
  padding: 40px 24px;
}

.detail-meta {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  font-size: 13px;
  
  .memo-group {
    color: #00D4AA;
    background: rgba(0, 212, 170, 0.1);
    padding: 2px 10px;
    border-radius: 4px;
  }
  
  .memo-time {
    color: #666;
    line-height: 28px;
  }
}

.detail-title {
  font-size: 32px;
  font-weight: 700;
  color: #fff;
  margin-bottom: 32px;
  line-height: 1.3;
}

.detail-body {
  color: #ccc;
  font-size: 16px;
  line-height: 1.8;
  
  :deep(h1, h2, h3) {
    color: #fff;
    margin: 24px 0 16px;
    font-weight: 600;
  }
  
  :deep(h1) { font-size: 28px; }
  :deep(h2) { font-size: 22px; }
  :deep(h3) { font-size: 18px; }
  
  :deep(p) {
    margin-bottom: 16px;
  }
  
  :deep(code) {
    background: #1A1A1A;
    padding: 2px 6px;
    border-radius: 4px;
    font-family: 'Fira Code', monospace;
    font-size: 14px;
    color: #00D4AA;
  }
  
  :deep(pre) {
    background: #1A1A1A;
    padding: 16px;
    border-radius: 8px;
    overflow-x: auto;
    margin: 16px 0;
    
    code {
      background: none;
      padding: 0;
    }
  }
  
  :deep(blockquote) {
    border-left: 3px solid #00D4AA;
    padding-left: 16px;
    color: #888;
    margin: 16px 0;
  }
  
  :deep(ul), :deep(ol) {
    padding-left: 24px;
    margin-bottom: 16px;
  }
  
  :deep(li) {
    margin-bottom: 8px;
  }
  
  :deep(img) {
    max-width: 100%;
    border-radius: 8px;
    margin: 16px 0;
  }
  
  :deep(a) {
    color: #00D4AA;
    text-decoration: none;
    
    &:hover {
      text-decoration: underline;
    }
  }
  
  :deep(table) {
    width: 100%;
    border-collapse: collapse;
    margin: 16px 0;
    
    th, td {
      border: 1px solid #333;
      padding: 8px 12px;
      text-align: left;
    }
    
    th {
      background: #1A1A1A;
      color: #fff;
    }
  }
}
</style>
