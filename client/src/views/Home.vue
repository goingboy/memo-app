<template>
  <div class="home-page">
    <!-- 顶部导航 -->
    <header class="home-header">
      <div class="header-left">
        <div class="logo">
          <el-icon :size="28" color="#10B981"><Document /></el-icon>
          <span>备忘录</span>
        </div>
      </div>
      <div class="header-center">
        <el-input 
          v-model="searchKeyword"
          placeholder="搜索备忘录..."
          :prefix-icon="Search"
          clearable
          class="search-input"
          @input="handleSearch"
        />
      </div>
      <div class="header-right">
        <el-button type="primary" size="large" :icon="Plus" @click="$router.push('/memo/new')">
          新建备忘录
        </el-button>
        <el-dropdown @command="handleCommand" trigger="click">
          <div class="user-info">
            <el-avatar :size="40" :src="userStore.avatar" class="user-avatar">
              {{ userStore.nickname?.charAt(0) || userStore.username?.charAt(0) || '?' }}
            </el-avatar>
            <div class="user-meta">
              <span class="user-name">{{ userStore.nickname || userStore.username || '用户' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon>个人资料
              </el-dropdown-item>
              <el-dropdown-item command="trash">
                <el-icon><Delete /></el-icon>回收站
              </el-dropdown-item>
              <el-dropdown-item v-if="userStore.isAdmin" command="admin">
                <el-icon><Setting /></el-icon>系统管理
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <el-icon><SwitchButton /></el-icon>退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </header>

    <div class="home-content">
      <!-- 左侧边栏 -->
      <aside class="sidebar">
        <div class="sidebar-section">
          <div class="sidebar-header">
            <span class="section-title">分组</span>
            <el-button text type="primary" size="small" :icon="Plus" @click="handleCreateGroup">
              新建
            </el-button>
          </div>
          <div class="group-list">
            <div 
              class="group-item"
              :class="{ active: !groupStore.selectedId }"
              @click="handleSelectGroup(null)"
            >
              <div class="group-info">
                <el-icon :size="18"><FolderOpened /></el-icon>
                <span>全部备忘录</span>
              </div>
              <span class="count">{{ memoStore.total || memoStore.list.length }}</span>
            </div>
            <div 
              v-for="group in groupStore.list" 
              :key="group.id"
              class="group-item"
              :class="{ active: groupStore.selectedId === group.id }"
              @click="handleSelectGroup(group.id)"
            >
              <div class="group-info">
                <el-icon :size="18"><Folder /></el-icon>
                <span>{{ group.name }}</span>
              </div>
              <el-dropdown trigger="click" @command="(cmd) => handleGroupCommand(cmd, group)">
                <el-icon class="group-more" @click.stop><MoreFilled /></el-icon>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="edit">
                      <el-icon><Edit /></el-icon>编辑
                    </el-dropdown-item>
                    <el-dropdown-item v-if="group.isDefault !== 1" command="delete">
                      <el-icon><Delete /></el-icon>删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </div>
      </aside>

      <!-- 右侧内容区 -->
      <main class="main-content">
        <div v-if="loading" class="loading-container">
          <el-skeleton :rows="3" animated />
        </div>
        
        <div v-else-if="memoStore.list.length === 0" class="empty-container">
          <el-empty description="还没有备忘录" :image-size="160">
            <template #image>
              <div class="empty-illustration">
                <el-icon :size="80" color="#475569"><Document /></el-icon>
              </div>
            </template>
            <el-button type="primary" size="large" :icon="Plus" @click="$router.push('/memo/new')">
              创建第一个备忘录
            </el-button>
          </el-empty>
        </div>
        
        <div v-else class="memo-grid">
          <div 
            v-for="memo in memoStore.list" 
            :key="memo.id"
            class="memo-card"
          >
            <div class="memo-card-header">
              <h3 class="memo-title" @click="$router.push(`/memo/${memo.id}`)">{{ memo.title || '无标题' }}</h3>
              <div class="memo-actions">
                <el-button 
                  text 
                  circle 
                  size="small"
                  @click.stop="$router.push(`/memo/${memo.id}/edit`)"
                >
                  <el-icon><Edit /></el-icon>
                </el-button>
                <el-button 
                  text 
                  circle 
                  size="small"
                  type="danger"
                  @click.stop="handleDeleteMemo(memo)"
                >
                  <el-icon><Delete /></el-icon>
                </el-button>
              </div>
            </div>
            <div class="memo-content" @click="$router.push(`/memo/${memo.id}`)">
              {{ getPreview(memo.content) }}
            </div>
            <div class="memo-footer" @click="$router.push(`/memo/${memo.id}`)">
              <div class="memo-meta">
                <el-tag v-if="memo.groupName" size="small" effect="plain">{{ memo.groupName }}</el-tag>
                <span class="memo-time">{{ formatTime(memo.updatedAt) }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div v-if="memoStore.total > pageSize" class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="pageSize"
            :total="memoStore.total"
            layout="prev, pager, next, jumper"
            @current-change="handlePageChange"
          />
        </div>
      </main>
    </div>

    <!-- 新建/编辑分组对话框 -->
    <el-dialog v-model="groupDialogVisible" :title="editingGroup ? '编辑分组' : '新建分组'" width="400px">
      <el-form ref="groupFormRef" :model="groupForm" :rules="groupRules">
        <el-form-item label="分组名称" prop="name">
          <el-input v-model="groupForm.name" placeholder="请输入分组名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="groupDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="groupLoading" @click="handleSaveGroup">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, User, Delete, Setting, SwitchButton, Folder, FolderOpened, MoreFilled, Edit, Document, ArrowDown } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { useMemoStore } from '@/stores/memo'
import { useGroupStore } from '@/stores/group'
import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'

dayjs.locale('zh-cn')

const router = useRouter()
const userStore = useUserStore()
const memoStore = useMemoStore()
const groupStore = useGroupStore()

const loading = ref(false)
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = 12

// 分组相关
const groupDialogVisible = ref(false)
const groupFormRef = ref()
const editingGroup = ref(null)
const groupLoading = ref(false)
const groupForm = reactive({ name: '' })
const groupRules = {
  name: [{ required: true, message: '请输入分组名称', trigger: 'blur' }]
}

onMounted(async () => {
  if (!userStore.isLoggedIn) {
    await userStore.fetchUserInfo().catch(() => {
      router.push('/login')
    })
  }
  await Promise.all([
    groupStore.fetchList(),
    fetchMemos()
  ])
})

const fetchMemos = async () => {
  loading.value = true
  try {
    await memoStore.fetchList({
      page: currentPage.value,
      pageSize,
      groupId: groupStore.selectedId,
      keyword: searchKeyword.value
    })
  } finally {
    loading.value = false
  }
}

const handleSelectGroup = (id) => {
  groupStore.selectGroup(id)
  currentPage.value = 1
  fetchMemos()
}

const handleSearch = () => {
  currentPage.value = 1
  fetchMemos()
}

const handlePageChange = () => {
  fetchMemos()
}

const getPreview = (content) => {
  if (!content) return '暂无内容'
  return content.replace(/[#*`\[\]]/g, '').substring(0, 100) + (content.length > 100 ? '...' : '')
}

const formatTime = (time) => {
  return dayjs(time).format('MM-DD HH:mm')
}

const handleCommand = (cmd) => {
  switch (cmd) {
    case 'profile':
      router.push('/profile')
      break
    case 'trash':
      router.push('/trash')
      break
    case 'admin':
      router.push('/admin')
      break
    case 'logout':
      userStore.logout()
      router.push('/login')
      break
  }
}

const handleDeleteMemo = async (memo) => {
  await ElMessageBox.confirm('确定要删除这篇备忘录吗？删除后可从回收站恢复', '提示', {
    type: 'warning'
  })
  await memoStore.remove(memo.id)
  ElMessage.success('已移入回收站')
  fetchMemos()
}

const handleCreateGroup = () => {
  editingGroup.value = null
  groupForm.name = ''
  groupDialogVisible.value = true
}

const handleGroupCommand = (cmd, group) => {
  if (cmd === 'edit') {
    editingGroup.value = group
    groupForm.name = group.name
    groupDialogVisible.value = true
  } else if (cmd === 'delete') {
    ElMessageBox.confirm('确定要删除该分组吗？分组内的备忘录将移至默认分组', '提示', {
      type: 'warning'
    }).then(async () => {
      await groupStore.remove(group.id)
      ElMessage.success('分组已删除')
    }).catch(() => {})
  }
}

const handleSaveGroup = async () => {
  const valid = await groupFormRef.value.validate().catch(() => false)
  if (!valid) return
  
  groupLoading.value = true
  try {
    if (editingGroup.value) {
      await groupStore.update(editingGroup.value.id, groupForm)
      ElMessage.success('分组已更新')
    } else {
      await groupStore.create(groupForm)
      ElMessage.success('分组已创建')
    }
    groupDialogVisible.value = false
  } catch (error) {
    // 错误已处理
  } finally {
    groupLoading.value = false
  }
}
</script>

<style lang="scss" scoped>
.home-page {
  width: 100%;
  min-height: 100vh;
  background-color: #0F172A;
  display: flex;
  flex-direction: column;
}

.home-header {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: #1E293B;
  border-bottom: 1px solid #334155;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left {
  .logo {
    display: flex;
    align-items: center;
    gap: 10px;
    
    span {
      font-size: 20px;
      font-weight: 700;
      color: #F1F5F9;
      letter-spacing: 1px;
    }
  }
}

.header-center {
  flex: 1;
  max-width: 480px;
  margin: 0 24px;
  
  .search-input {
    width: 100%;
  }
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
  
  .user-info {
    display: flex;
    align-items: center;
    gap: 10px;
    cursor: pointer;
    padding: 4px 12px 4px 4px;
    border-radius: 24px;
    transition: background 0.2s;
    
    &:hover {
      background: rgba(255, 255, 255, 0.05);
    }
    
    .user-avatar {
      border: 2px solid #10B981;
    }
    
    .user-meta {
      display: flex;
      align-items: center;
      gap: 4px;
      
      .user-name {
        font-size: 14px;
        color: #F1F5F9;
        max-width: 100px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      
      .el-icon {
        color: #64748B;
        font-size: 12px;
      }
    }
  }
}

.home-content {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.sidebar {
  width: 260px;
  background: #1E293B;
  border-right: 1px solid #334155;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.sidebar-section {
  padding: 16px;
}

.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  margin-bottom: 8px;
  
  .section-title {
    font-size: 12px;
    font-weight: 600;
    color: #64748B;
    text-transform: uppercase;
    letter-spacing: 1px;
  }
}

.group-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.group-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 12px;
  border-radius: 8px;
  cursor: pointer;
  color: #94A3B8;
  font-size: 14px;
  transition: all 0.2s;
  
  &:hover {
    background: rgba(16, 185, 129, 0.08);
    color: #F1F5F9;
  }
  
  &.active {
    background: rgba(16, 185, 129, 0.15);
    color: #10B981;
    
    .count {
      color: #10B981;
    }
  }
  
  .group-info {
    display: flex;
    align-items: center;
    gap: 10px;
  }
  
  .count {
    font-size: 12px;
    color: #64748B;
    background: rgba(255, 255, 255, 0.05);
    padding: 2px 8px;
    border-radius: 10px;
  }
  
  .group-more {
    opacity: 0;
    font-size: 14px;
    padding: 4px;
    border-radius: 4px;
    
    &:hover {
      color: #10B981;
      background: rgba(16, 185, 129, 0.1);
    }
  }
  
  &:hover .group-more {
    opacity: 1;
  }
}

.main-content {
  flex: 1;
  padding: 24px 32px;
  overflow-y: auto;
  background: #0F172A;
}

.loading-container {
  padding: 24px;
}

.empty-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
  
  .empty-illustration {
    opacity: 0.5;
  }
}

.memo-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.memo-card {
  background: #1E293B;
  border: 1px solid #334155;
  border-radius: 12px;
  padding: 16px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  flex-direction: column;
  min-height: 160px;
  
  &:hover {
    border-color: #10B981;
    box-shadow: 0 0 0 1px #10B981, 0 10px 40px rgba(16, 185, 129, 0.1);
    transform: translateY(-2px);
  }
}

.memo-card-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 12px;
  gap: 8px;
  
  .memo-title {
    font-size: 16px;
    font-weight: 600;
    color: #F1F5F9;
    flex: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    cursor: pointer;
    
    &:hover {
      color: #10B981;
    }
  }
  
  .memo-actions {
    display: flex;
    gap: 4px;
    opacity: 0;
    transition: opacity 0.2s;
    
    .el-button {
      color: #64748B;
      
      &:hover {
        color: #10B981;
      }
      
      &.is-danger:hover {
        color: #EF4444;
      }
    }
  }
  
  &:hover .memo-actions {
    opacity: 1;
  }
}

.memo-content {
  flex: 1;
  font-size: 13px;
  color: #94A3B8;
  line-height: 1.6;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  cursor: pointer;
  
  &:hover {
    color: #CBD5E1;
  }
}

.memo-footer {
  margin-top: 12px;
  cursor: pointer;
  
  .memo-meta {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 8px;
    
    .el-tag {
      background: rgba(16, 185, 129, 0.1);
      border: none;
      color: #10B981;
    }
    
    .memo-time {
      font-size: 12px;
      color: #64748B;
    }
  }
  
  &:hover .memo-time {
    color: #94A3B8;
  }
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 32px;
  padding: 16px;
}

// 响应式布局
@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    left: -260px;
    top: 64px;
    bottom: 0;
    z-index: 99;
    transition: left 0.3s;
    
    &.open {
      left: 0;
    }
  }
  
  .header-center {
    display: none;
  }
  
  .memo-grid {
    grid-template-columns: 1fr;
  }
}
</style>
