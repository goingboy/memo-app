<template>
  <div class="home-page">
    <!-- 顶部导航 -->
    <header class="home-header">
      <div class="header-left">
        <h1 class="logo">备忘录</h1>
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
        <el-button type="primary" :icon="Plus" @click="$router.push('/memo/new')">
          新建
        </el-button>
        <el-dropdown @command="handleCommand">
          <div class="user-info">
            <el-avatar :size="36" :src="userStore.avatar">
              {{ userStore.nickname?.charAt(0) }}
            </el-avatar>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon>个人资料
              </el-dropdown-item>
              <el-dropdown-item command="trash">
                <el-icon><Delete /></el-icon>垃圾站
              </el-dropdown-item>
              <el-dropdown-item v-if="userStore.isAdmin" command="admin">
                <el-icon><Setting /></el-icon>管理
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
        <div class="sidebar-header">
          <span>分组</span>
          <el-button text size="small" :icon="Plus" @click="handleCreateGroup">新建</el-button>
        </div>
        <div class="group-list">
          <div 
            class="group-item"
            :class="{ active: !groupStore.selectedId }"
            @click="handleSelectGroup(null)"
          >
            <el-icon><FolderOpened /></el-icon>
            <span>全部备忘录</span>
            <span class="count">{{ memoStore.total }}</span>
          </div>
          <div 
            v-for="group in groupStore.list" 
            :key="group.id"
            class="group-item"
            :class="{ active: groupStore.selectedId === group.id }"
            @click="handleSelectGroup(group.id)"
          >
            <el-icon><Folder /></el-icon>
            <span>{{ group.name }}</span>
            <el-dropdown trigger="click" @command="(cmd) => handleGroupCommand(cmd, group)">
              <el-icon class="group-more"><MoreFilled /></el-icon>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="edit">编辑</el-dropdown-item>
                  <el-dropdown-item v-if="group.isDefault !== 1" command="delete">删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </aside>

      <!-- 右侧内容区 -->
      <main class="main-content">
        <div v-if="loading" class="loading-container">
          <el-icon class="is-loading"><Loading /></el-icon>
        </div>
        
        <div v-else-if="memoStore.list.length === 0" class="empty-container">
          <el-empty description="还没有备忘录" :image-size="120">
            <el-button type="primary" @click="$router.push('/memo/new')">创建第一个备忘录</el-button>
          </el-empty>
        </div>
        
        <div v-else class="memo-grid">
          <div 
            v-for="memo in memoStore.list" 
            :key="memo.id"
            class="memo-card"
            @click="$router.push(`/memo/${memo.id}`)"
          >
            <div class="memo-card-header">
              <h3 class="memo-title">{{ memo.title || '无标题' }}</h3>
              <el-dropdown trigger="click" @command="(cmd) => handleMemoCommand(cmd, memo)">
                <el-icon class="memo-more" @click.stop><MoreFilled /></el-icon>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="edit">编辑</el-dropdown-item>
                    <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
            <div class="memo-content">{{ getPreview(memo.content) }}</div>
            <div class="memo-footer">
              <span class="memo-time">{{ formatTime(memo.updatedAt) }}</span>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div v-if="memoStore.total > pageSize" class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="pageSize"
            :total="memoStore.total"
            layout="prev, pager, next"
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
import { Plus, Search, User, Delete, Setting, SwitchButton, Folder, FolderOpened, MoreFilled, Loading } from '@element-plus/icons-vue'
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
      groupId: groupStore.selectedId
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
  // 简单实现，实际可加防抖
  currentPage.value = 1
  fetchMemos()
}

const handlePageChange = () => {
  fetchMemos()
}

const getPreview = (content) => {
  if (!content) return '暂无内容'
  // 去掉Markdown标记获取纯文本预览
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

const handleMemoCommand = async (cmd, memo) => {
  if (cmd === 'edit') {
    router.push(`/memo/${memo.id}/edit`)
  } else if (cmd === 'delete') {
    await ElMessageBox.confirm('确定要删除这篇备忘录吗？删除后可从垃圾站恢复', '提示', {
      type: 'warning'
    })
    await memoStore.remove(memo.id)
    ElMessage.success('已移入垃圾站')
    fetchMemos()
  }
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
  background-color: #0A0A0A;
  display: flex;
  flex-direction: column;
}

.home-header {
  height: 64px;
  display: flex;
  align-items: center;
  padding: 0 24px;
  background: #1A1A1A;
  border-bottom: 1px solid #333;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left {
  flex: 0 0 auto;
  
  .logo {
    font-size: 20px;
    font-weight: 700;
    color: #00D4AA;
    letter-spacing: 2px;
  }
}

.header-center {
  flex: 1;
  max-width: 400px;
  margin: 0 auto;
  
  .search-input {
    width: 100%;
  }
}

.header-right {
  flex: 0 0 auto;
  display: flex;
  align-items: center;
  gap: 16px;
  
  .user-info {
    cursor: pointer;
  }
}

.home-content {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.sidebar {
  width: 240px;
  background: #1A1A1A;
  border-right: 1px solid #333;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px 8px;
  color: #B0B0B0;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.group-list {
  flex: 1;
  padding: 8px;
}

.group-item {
  display: flex;
  align-items: center;
  padding: 10px 12px;
  border-radius: 8px;
  cursor: pointer;
  color: #B0B0B0;
  font-size: 14px;
  transition: all 0.2s;
  gap: 8px;
  
  &:hover {
    background: rgba(0, 212, 170, 0.1);
    color: #fff;
  }
  
  &.active {
    background: rgba(0, 212, 170, 0.15);
    color: #00D4AA;
  }
  
  .el-icon {
    font-size: 16px;
  }
  
  span {
    flex: 1;
  }
  
  .count {
    flex: 0 0 auto;
    color: #666;
    font-size: 12px;
  }
  
  .group-more {
    opacity: 0;
    font-size: 14px;
    
    &:hover {
      color: #00D4AA;
    }
  }
  
  &:hover .group-more {
    opacity: 1;
  }
}

.main-content {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
}

.loading-container,
.empty-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
  font-size: 48px;
  color: #666;
}

.memo-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.memo-card {
  background: #1A1A1A;
  border: 1px solid #333;
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  flex-direction: column;
  min-height: 160px;
  
  &:hover {
    border-color: #00D4AA;
    box-shadow: 0 4px 20px rgba(0, 212, 170, 0.1);
    transform: translateY(-2px);
  }
}

.memo-card-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 8px;
  
  .memo-title {
    font-size: 16px;
    font-weight: 600;
    color: #fff;
    flex: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  
  .memo-more {
    opacity: 0;
    color: #666;
    font-size: 16px;
    transition: opacity 0.2s;
    
    &:hover {
      color: #00D4AA;
    }
  }
  
  &:hover .memo-more {
    opacity: 1;
  }
}

.memo-content {
  flex: 1;
  font-size: 13px;
  color: #888;
  line-height: 1.6;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.memo-footer {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
  
  .memo-time {
    font-size: 12px;
    color: #666;
  }
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 32px;
}

// 响应式布局
@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    left: -240px;
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
