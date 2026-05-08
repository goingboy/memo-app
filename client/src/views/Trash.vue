<template>
  <div class="trash-page">
    <header class="trash-header">
      <el-button :icon="ArrowLeft" text @click="$router.push('/')">返回</el-button>
      <h1 class="page-title">垃圾站</h1>
      <span class="trash-count">{{ memoStore.trashList.length }} 篇已删除</span>
    </header>
    
    <div v-if="memoStore.loading" class="loading">
      <el-icon class="is-loading" :size="40"><Loading /></el-icon>
    </div>
    
    <div v-else-if="memoStore.trashList.length === 0" class="empty-trash">
      <el-icon :size="64" color="#333"><Delete /></el-icon>
      <p>垃圾站是空的</p>
    </div>
    
    <div v-else class="trash-list">
      <div
        v-for="memo in memoStore.trashList"
        :key="memo.id"
        class="trash-item"
      >
        <div class="item-content">
          <h3 class="item-title">{{ memo.title || '无标题' }}</h3>
          <p class="item-preview">{{ memo.content?.substring(0, 100) || '无内容' }}</p>
          <span class="item-time">删除于 {{ formatTime(memo.deletedAt) }}</span>
        </div>
        <div class="item-actions">
          <el-button type="primary" text @click="handleRestore(memo.id)">
            恢复
          </el-button>
          <el-button type="danger" text @click="handlePermanentDelete(memo.id)">
            永久删除
          </el-button>
        </div>
      </div>
      
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="memoStore.trashTotal"
          layout="prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft, Delete, Loading } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import { useMemoStore } from '@/stores/memo'

const memoStore = useMemoStore()
const currentPage = ref(1)
const pageSize = ref(20)

const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

onMounted(() => {
  memoStore.fetchTrashList()
})

const handleRestore = async (id) => {
  try {
    await memoStore.restore(id)
    ElMessage.success('备忘录已恢复')
  } catch (error) {
    // 错误已处理
  }
}

const handlePermanentDelete = async (id) => {
  try {
    await ElMessageBox.confirm(
      '确定要永久删除这篇备忘录吗？此操作不可恢复！',
      '警告',
      { type: 'error', confirmButtonText: '永久删除' }
    )
    await memoStore.permanentDelete(id)
    ElMessage.success('已永久删除')
  } catch (error) {
    if (error !== 'cancel') {
      // 错误已处理
    }
  }
}

const handlePageChange = (page) => {
  currentPage.value = page
  memoStore.fetchTrashList(page, pageSize.value)
}
</script>

<style lang="scss" scoped>
.trash-page {
  min-height: 100vh;
  background: #0A0A0A;
}

.trash-header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 24px;
  background: #1A1A1A;
  border-bottom: 1px solid #333;
  position: sticky;
  top: 0;
  z-index: 10;
  
  .page-title {
    font-size: 20px;
    font-weight: 600;
    color: #fff;
    margin: 0;
  }
  
  .trash-count {
    margin-left: auto;
    color: #666;
    font-size: 14px;
  }
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
  color: #666;
}

.empty-trash {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  color: #666;
  
  p {
    margin-top: 16px;
    font-size: 16px;
  }
}

.trash-list {
  max-width: 900px;
  margin: 0 auto;
  padding: 24px;
}

.trash-item {
  background: #1A1A1A;
  border: 1px solid #333;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 16px;
  transition: all 0.2s;
  
  &:hover {
    border-color: #00D4AA;
  }
  
  .item-content {
    margin-bottom: 16px;
  }
  
  .item-title {
    font-size: 18px;
    font-weight: 600;
    color: #fff;
    margin: 0 0 8px;
  }
  
  .item-preview {
    color: #888;
    font-size: 14px;
    line-height: 1.6;
    margin: 0 0 12px;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
  
  .item-time {
    color: #666;
    font-size: 12px;
  }
  
  .item-actions {
    display: flex;
    gap: 8px;
    padding-top: 16px;
    border-top: 1px solid #333;
  }
}

.pagination {
  display: flex;
  justify-content: center;
  padding: 24px 0;
}
</style>
