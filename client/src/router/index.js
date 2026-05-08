import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { public: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { public: true }
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/memo/new',
    name: 'MemoCreate',
    component: () => import('@/views/MemoEdit.vue')
  },
  {
    path: '/memo/:id',
    name: 'MemoDetail',
    component: () => import('@/views/MemoDetail.vue')
  },
  {
    path: '/memo/:id/edit',
    name: 'MemoEdit',
    component: () => import('@/views/MemoEdit.vue')
  },
  {
    path: '/trash',
    name: 'Trash',
    component: () => import('@/views/Trash.vue')
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/Profile.vue')
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('@/views/Admin.vue'),
    meta: { requiresAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  
  // 需要登录的页面
  if (!to.meta.public && !token) {
    next('/login')
    return
  }
  
  // 已登录用户访问登录/注册页
  if (to.meta.public && token) {
    next('/')
    return
  }
  
  // 需要管理员权限
  if (to.meta.requiresAdmin && userInfo.role !== 'admin') {
    next('/')
    return
  }
  
  next()
})

export default router
