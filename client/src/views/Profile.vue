<template>
  <div class="profile-page">
    <header class="profile-header">
      <el-button :icon="ArrowLeft" text @click="$router.back()">返回</el-button>
      <h1 class="page-title">个人资料</h1>
    </header>
    
    <div class="profile-content">
      <div class="avatar-section">
        <el-avatar :size="100" :src="avatarUrl" />
        <el-upload
          class="avatar-uploader"
          :action="uploadUrl"
          :headers="uploadHeaders"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <el-button type="primary" text>更换头像</el-button>
        </el-upload>
      </div>
      
      <el-form :model="form" label-position="top" class="profile-form">
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" placeholder="设置你的昵称" maxlength="50" />
        </el-form-item>
        
        <el-form-item label="邮箱">
          <el-input v-model="form.email" disabled />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" :loading="saving" @click="handleSave">
            保存修改
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { updateProfile, uploadAvatar } from '@/api/user'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const saving = ref(false)

const form = reactive({
  nickname: '',
  email: ''
})

const avatarUrl = computed(() => {
  return userStore.userInfo?.avatar || ''
})

const uploadUrl = computed(() => {
  return import.meta.env.VITE_API_BASE_URL + '/user/avatar'
})

const uploadHeaders = computed(() => {
  return {
    Authorization: `Bearer ${localStorage.getItem('token')}`
  }
})

onMounted(() => {
  if (userStore.userInfo) {
    form.nickname = userStore.userInfo.nickname || ''
    form.email = userStore.userInfo.email || ''
  }
})

const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg'
  const isPNG = file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG && !isPNG) {
    ElMessage.error('头像只能是 JPG 或 PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像大小不能超过 2MB!')
    return false
  }
  return true
}

const handleAvatarSuccess = (response) => {
  userStore.userInfo.avatar = response.url
  ElMessage.success('头像更新成功')
}

const handleSave = async () => {
  saving.value = true
  try {
    await updateProfile({ nickname: form.nickname })
    userStore.userInfo.nickname = form.nickname
    ElMessage.success('资料已更新')
  } catch (error) {
    // 错误已处理
  } finally {
    saving.value = false
  }
}
</script>

<style lang="scss" scoped>
.profile-page {
  min-height: 100vh;
  background: #0A0A0A;
}

.profile-header {
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

.profile-content {
  max-width: 500px;
  margin: 0 auto;
  padding: 40px 24px;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 32px;
  
  .avatar-uploader {
    margin-top: 16px;
  }
}

.profile-form {
  :deep(.el-form-item__label) {
    color: #888;
  }
  
  :deep(.el-input__wrapper) {
    background: #1A1A1A;
    box-shadow: 0 0 0 1px #333 inset;
  }
  
  :deep(.el-input__inner) {
    color: #fff;
  }
}
</style>
