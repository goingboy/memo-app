<template>
  <div class="login-page">
    <div class="login-wrapper">
      <!-- 左侧吉祥物区域 -->
      <div class="login-hero">
        <div class="hero-content">
          <img src="/mascot.jpg" alt="Memo Mascot" class="mascot-image" />
          <h2 class="hero-title">记录每一个灵感瞬间</h2>
          <p class="hero-desc">简洁、优雅、高效的备忘录应用</p>
        </div>
      </div>
      
      <!-- 右侧登录表单 -->
      <div class="login-container">
        <div class="login-header">
          <div class="logo">
            <el-icon :size="32" color="#10B981"><Document /></el-icon>
            <span>备忘录</span>
          </div>
          <h1 class="login-title">欢迎回来</h1>
          <p class="login-subtitle">使用账号密码登录</p>
        </div>
        
        <el-form ref="formRef" :model="form" :rules="rules" class="login-form" @submit.prevent="handleLogin">
          <el-form-item prop="username">
            <el-input 
              v-model="form.username" 
              placeholder="请输入账号" 
              size="large"
              :prefix-icon="User"
            />
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input 
              v-model="form.password" 
              type="password" 
              placeholder="请输入密码" 
              size="large"
              :prefix-icon="Lock"
              show-password
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          
          <el-button 
            type="primary" 
            size="large" 
            :loading="loading" 
            class="login-btn"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form>
        
        <div class="login-footer">
          <span>还没有账号？</span>
          <router-link to="/register" class="register-link">立即注册</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Document } from '@element-plus/icons-vue'
import { login } from '@/api/auth'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度3-20位', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  
  loading.value = true
  try {
    const res = await login(form)
    userStore.setToken(res.data)
    await userStore.fetchUserInfo()
    ElMessage.success('登录成功')
    router.push('/')
  } catch (error) {
    // 错误已在拦截器处理
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.login-page {
  width: 100%;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0F172A 0%, #1E293B 50%, #0F172A 100%);
  position: relative;
  overflow: hidden;
}

.login-wrapper {
  display: flex;
  width: 900px;
  max-width: 95vw;
  min-height: 560px;
  background: #1E293B;
  border: 1px solid #334155;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 25px 80px rgba(0, 0, 0, 0.5);
}

.login-hero {
  flex: 1;
  background: linear-gradient(135deg, rgba(16, 185, 129, 0.08) 0%, rgba(59, 130, 246, 0.08) 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 48px;
  
  .hero-content {
    text-align: center;
  }
  
  .mascot-image {
    width: 260px;
    height: auto;
    border-radius: 16px;
    margin-bottom: 28px;
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  }
  
  .hero-title {
    font-size: 22px;
    font-weight: 700;
    color: #F1F5F9;
    margin-bottom: 10px;
  }
  
  .hero-desc {
    font-size: 14px;
    color: #94A3B8;
  }
}

.login-container {
  width: 400px;
  padding: 48px 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
  
  .logo {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    margin-bottom: 24px;
    
    span {
      font-size: 24px;
      font-weight: 700;
      color: #F1F5F9;
    }
  }
  
  .login-title {
    font-size: 28px;
    font-weight: 700;
    color: #F1F5F9;
    margin-bottom: 8px;
  }
  
  .login-subtitle {
    color: #94A3B8;
    font-size: 14px;
  }
}

.login-form {
  :deep(.el-form-item) {
    margin-bottom: 20px;
  }
  
  :deep(.el-input__wrapper) {
    background: #0F172A;
    box-shadow: 0 0 0 1px #334155 inset;
    border-radius: 10px;
    height: 48px;
    
    &:hover {
      box-shadow: 0 0 0 1px #475569 inset;
    }
    
    &.is-focus {
      box-shadow: 0 0 0 1px #10B981 inset;
    }
  }
  
  :deep(.el-input__inner) {
    color: #F1F5F9;
    
    &::placeholder {
      color: #64748B;
    }
  }
}

.login-btn {
  width: 100%;
  height: 48px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  margin-top: 8px;
  background: #10B981;
  border-color: #10B981;
  
  &:hover {
    background: #059669;
    border-color: #059669;
  }
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  color: #94A3B8;
  font-size: 14px;
  
  .register-link {
    color: #10B981;
    text-decoration: none;
    margin-left: 4px;
    font-weight: 500;
    
    &:hover {
      text-decoration: underline;
    }
  }
}

@media (max-width: 768px) {
  .login-wrapper {
    flex-direction: column;
    width: 100%;
    height: 100vh;
    border-radius: 0;
  }
  
  .login-hero {
    display: none;
  }
  
  .login-container {
    width: 100%;
    padding: 32px 24px;
  }
}
</style>
