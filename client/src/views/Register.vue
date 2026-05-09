<template>
  <div class="register-page">
    <div class="register-wrapper">
      <!-- 左侧吉祥物区域 -->
      <div class="register-hero">
        <div class="hero-content">
          <img src="/mascot.jpg" alt="Memo Mascot" class="mascot-image" />
          <h2 class="hero-title">开始你的记录之旅</h2>
          <p class="hero-desc">免费注册，立即开始使用</p>
        </div>
      </div>
      
      <!-- 右侧注册表单 -->
      <div class="register-container">
        <div class="register-header">
          <div class="logo">
            <el-icon :size="32" color="#10B981"><Document /></el-icon>
            <span>备忘录</span>
          </div>
          <h1 class="register-title">创建账号</h1>
          <p class="register-subtitle">填写以下信息完成注册</p>
        </div>
        
        <el-form ref="formRef" :model="form" :rules="rules" class="register-form" @submit.prevent="handleRegister">
          <el-form-item prop="username">
            <el-input 
              v-model="form.username" 
              placeholder="设置账号名（3-20位）" 
              size="large"
              :prefix-icon="User"
            />
          </el-form-item>
          
          <el-form-item prop="email">
            <el-input 
              v-model="form.email" 
              placeholder="绑定邮箱（用于找回密码）" 
              size="large"
              :prefix-icon="Message"
            />
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input 
              v-model="form.password" 
              type="password" 
              placeholder="设置密码（至少6位）" 
              size="large"
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>
          
          <el-form-item prop="confirmPassword">
            <el-input 
              v-model="form.confirmPassword" 
              type="password" 
              placeholder="确认密码" 
              size="large"
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>
          
          <el-button 
            type="primary" 
            size="large" 
            :loading="loading" 
            class="register-btn"
            @click="handleRegister"
          >
            注册
          </el-button>
        </el-form>
        
        <div class="register-footer">
          <span>已有账号？</span>
          <router-link to="/login" class="login-link">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Message, Lock, Document } from '@element-plus/icons-vue'
import { register } from '@/api/auth'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error('两次密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入账号名', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度3-20位', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  
  loading.value = true
  try {
    await register({
      username: form.username,
      email: form.email,
      password: form.password
    })
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (error) {
    // 错误已在拦截器处理
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.register-page {
  width: 100%;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0F172A 0%, #1E293B 50%, #0F172A 100%);
  position: relative;
  overflow: hidden;
}

.register-wrapper {
  display: flex;
  width: 900px;
  max-width: 95vw;
  min-height: 600px;
  background: #1E293B;
  border: 1px solid #334155;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 25px 80px rgba(0, 0, 0, 0.5);
}

.register-hero {
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

.register-container {
  width: 420px;
  padding: 48px 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.register-header {
  text-align: center;
  margin-bottom: 28px;
  
  .logo {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    margin-bottom: 20px;
    
    span {
      font-size: 24px;
      font-weight: 700;
      color: #F1F5F9;
    }
  }
  
  .register-title {
    font-size: 28px;
    font-weight: 700;
    color: #F1F5F9;
    margin-bottom: 8px;
  }
  
  .register-subtitle {
    color: #94A3B8;
    font-size: 14px;
  }
}

.register-form {
  :deep(.el-form-item) {
    margin-bottom: 16px;
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

.register-btn {
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

.register-footer {
  text-align: center;
  margin-top: 24px;
  color: #94A3B8;
  font-size: 14px;
  
  .login-link {
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
  .register-wrapper {
    flex-direction: column;
    width: 100%;
    height: 100vh;
    border-radius: 0;
  }
  
  .register-hero {
    display: none;
  }
  
  .register-container {
    width: 100%;
    padding: 32px 24px;
  }
}
</style>
