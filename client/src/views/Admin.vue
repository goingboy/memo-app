<template>
  <div class="admin-page">
    <header class="admin-header">
      <el-button :icon="ArrowLeft" text @click="$router.push('/')">返回</el-button>
      <h1 class="page-title">用户管理</h1>
    </header>
    
    <div class="admin-content">
      <div class="search-bar">
        <el-input
          v-model="searchQuery"
          placeholder="搜索用户邮箱或昵称"
          :prefix-icon="Search"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">搜索</el-button>
          </template>
        </el-input>
      </div>
      
      <el-table :data="userList" v-loading="loading" class="user-table">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="email" label="邮箱" min-width="200" />
        <el-table-column prop="nickname" label="昵称" min-width="150">
          <template #default="{ row }">
            {{ row.nickname || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="role" label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="row.role === 'admin' ? 'danger' : ''">
              {{ row.role === 'admin' ? '管理员' : '用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="(val) => handleStatusChange(row.id, val)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="注册时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createdAt) }}
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Search } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import { getUserList, updateUserStatus } from '@/api/admin'

const userList = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)
const searchQuery = ref('')

const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

const fetchUsers = async () => {
  loading.value = true
  try {
    const res = await getUserList({
      page: currentPage.value,
      pageSize: pageSize.value,
      keyword: searchQuery.value
    })
    userList.value = res.list
    total.value = res.total
  } catch (error) {
    // 错误已处理
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchUsers()
}

const handlePageChange = (page) => {
  currentPage.value = page
  fetchUsers()
}

const handleStatusChange = async (userId, status) => {
  try {
    await updateUserStatus(userId, status)
    ElMessage.success('状态已更新')
  } catch (error) {
    // 错误已处理
  }
}

onMounted(() => {
  fetchUsers()
})
</script>

<style lang="scss" scoped>
.admin-page {
  min-height: 100vh;
  background: #0A0A0A;
}

.admin-header {
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
}

.admin-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.search-bar {
  margin-bottom: 24px;
  max-width: 400px;
}

.user-table {
  background: transparent;
  
  :deep(.el-table__header-wrapper) {
    th {
      background: #1A1A1A;
      color: #888;
      font-weight: 600;
    }
  }
  
  :deep(.el-table__row) {
    background: #1A1A1A;
    
    &:hover > td {
      background: #252525;
    }
  }
  
  :deep(td) {
    color: #ccc;
    border-bottom: 1px solid #333;
  }
}

.pagination {
  display: flex;
  justify-content: center;
  padding: 24px 0;
}
</style>
