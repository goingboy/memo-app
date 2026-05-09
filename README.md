---
title: Memo App Server
emoji: 📝
colorFrom: blue
colorTo: green
sdk: docker
app_port: 7860
pinned: false
---

# 📝 Memo App - 全栈便签应用

[![Vue 3](https://img.shields.io/badge/Vue-3.4.21-4FC08D?logo=vue.js)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-6DB33F?logo=spring)](https://spring.io/projects/spring-boot)
[![Element Plus](https://img.shields.io/badge/Element%20Plus-2.7.0-409EFF)](https://element-plus.org/)
[![Sa-Token](https://img.shields.io/badge/Sa--Token-1.37.0-FF6B6B)](https://sa-token.cc/)

一个功能完整的便签应用，采用 Zeekr 科技风格设计，支持 Markdown 编辑、分组管理、回收站等功能。

![Zeekr Theme](https://img.shields.io/badge/Theme-Zeekr%20Dark-00D4AA)

## ✨ 功能特性

### 👤 用户系统
- ✅ 用户注册/登录（邮箱+密码）
- ✅ 个人资料管理（昵称、头像）
- ✅ Sa-Token 权限认证，安全可靠

### 📝 便签管理
- ✅ 创建/编辑/删除便签
- ✅ Markdown 编辑器 + 实时预览
- ✅ 便签分组管理（支持默认分组）
- ✅ 回收站（软删除 + 恢复 + 永久删除）

### 👑 管理员功能
- ✅ 用户管理（查看用户列表、禁用/启用账号）
- ✅ 默认管理员邮箱: `13167000126@163.com`

### 🎨 UI 设计
- ✅ Zeekr 科技风格深色主题（#0A0A0A 背景 + #00D4AA 主色）
- ✅ 响应式设计（完美适配移动端/平板/桌面）
- ✅ 卡片式布局，流畅动画效果
- ✅ Element Plus 组件库深度定制

## 🛠️ 技术栈

### 后端
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.5 | 主框架 |
| Sa-Token | 1.37.0 | 权限认证 |
| MyBatis Plus | 3.5.5 | ORM框架 |
| H2 Database | 2.2.224 | 嵌入式数据库 |
| Hutool | 5.8.25 | 工具库 |
| SpringDoc | 2.3.0 | API文档 |

### 前端
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.4.21 | 前端框架 |
| Vite | 5.2.0 | 构建工具 |
| Element Plus | 2.7.0 | UI组件库 |
| Pinia | 2.1.7 | 状态管理 |
| Vue Router | 4.3.0 | 路由管理 |
| Vditor | 3.10.8 | Markdown编辑器 |
| Axios | 1.6.8 | HTTP客户端 |

## 📁 项目结构

```
memo-app/
├── client/                    # 前端 Vue3 项目
│   ├── src/
│   │   ├── api/              # API 接口封装
│   │   │   ├── auth.js       # 认证相关
│   │   │   ├── memo.js       # 便签CRUD
│   │   │   ├── group.js      # 分组管理
│   │   │   └── admin.js      # 管理员接口
│   │   ├── assets/
│   │   │   └── styles/
│   │   │       └── main.scss # Zeekr主题样式
│   │   ├── router/
│   │   │   └── index.js      # 路由配置
│   │   ├── stores/           # Pinia状态管理
│   │   │   ├── user.js       # 用户状态
│   │   │   ├── memo.js       # 便签状态
│   │   │   └── group.js      # 分组状态
│   │   ├── utils/
│   │   │   └── request.js    # Axios封装
│   │   ├── views/            # 页面组件
│   │   │   ├── Login.vue     # 登录页
│   │   │   ├── Register.vue  # 注册页
│   │   │   ├── Home.vue      # 首页（便签列表）
│   │   │   ├── MemoDetail.vue# 便签详情
│   │   │   ├── MemoEdit.vue  # 编辑/新建便签
│   │   │   ├── Trash.vue     # 回收站
│   │   │   ├── Profile.vue   # 个人资料
│   │   │   └── Admin.vue     # 管理后台
│   │   ├── App.vue
│   │   └── main.js
│   ├── package.json
│   ├── vite.config.js
│   └── vercel.json           # Vercel部署配置
│
├── server/                    # 后端 Spring Boot 项目
│   ├── src/main/java/com/memo/
│   │   ├── config/           # 配置类
│   │   │   ├── CorsConfig.java
│   │   │   ├── SaTokenConfig.java
│   │   │   ├── WebConfig.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── controller/       # 控制器层
│   │   │   ├── AuthController.java
│   │   │   ├── MemoController.java
│   │   │   ├── GroupController.java
│   │   │   ├── AdminController.java
│   │   │   ├── FileController.java
│   │   │   └── HealthController.java
│   │   ├── dto/              # 数据传输对象
│   │   ├── entity/           # 实体类
│   │   ├── mapper/           # MyBatis Mapper
│   │   ├── service/          # 业务逻辑层
│   │   └── MemoApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml       # 开发配置
│   │   ├── application-prod.yml  # 生产配置
│   │   └── schema.sql            # 数据库脚本
│   ├── Dockerfile            # Docker构建
│   ├── railway.json          # Railway部署配置
│   ├── render.yaml           # Render部署配置
│   └── pom.xml
│
├── docs/                     # 项目文档
│   ├── PRD.md               # 产品需求文档
│   ├── Technical_Solution.md # 技术方案
│   ├── Database_Design.md   # 数据库设计
│   └── UI_Design.md         # UI设计规范
│
├── DEPLOY.md                # 部署指南
└── README.md                # 项目说明
```

## 🚀 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- Maven 3.8+

### 本地开发

**1. 克隆项目**
```bash
git clone https://github.com/yourusername/memo-app.git
cd memo-app
```

**2. 启动后端**
```bash
cd server
mvn spring-boot:run
```
后端运行在 http://localhost:8080

**3. 启动前端**
```bash
cd client
npm install
npm run dev
```
前端运行在 http://localhost:3000

### 生产部署

查看 [DEPLOY.md](./DEPLOY.md) 获取详细的部署指南。

**推荐部署架构:**
- 🎨 **前端**: Vercel (免费，自动部署，全球CDN)
- ⚙️ **后端**: Railway ($5/月免费额度，不休眠)
- 💾 **数据库**: H2嵌入式 (无需额外配置)

## 📸 功能预览

| 功能 | 描述 |
|------|------|
| 🔐 登录/注册 | 深色主题表单，支持头像上传 |
| 🏠 首页 | 卡片式便签列表，支持分组筛选 |
| 📝 编辑器 | Vditor Markdown编辑器，实时预览 |
| 📄 详情页 | Markdown渲染，代码高亮 |
| 🗑️ 回收站 | 软删除管理，支持恢复和永久删除 |
| 👤 个人资料 | 修改昵称、头像、密码 |
| ⚙️ 管理后台 | 用户管理（仅管理员可见）|

## 🔌 API 文档

启动后端后访问: http://localhost:8080/swagger-ui.html

### 主要接口

#### 认证接口
```
POST   /api/auth/register       # 注册
POST   /api/auth/login          # 登录
GET    /api/auth/profile        # 获取用户信息
PUT    /api/auth/profile        # 更新用户信息
POST   /api/auth/avatar         # 上传头像
```

#### 便签接口
```
GET    /api/memos               # 获取便签列表
GET    /api/memos/{id}          # 获取便签详情
POST   /api/memos               # 创建便签
PUT    /api/memos/{id}          # 更新便签
DELETE /api/memos/{id}          # 删除便签（软删除）
```

#### 回收站接口
```
GET    /api/memos/trash         # 获取回收站列表
POST   /api/memos/{id}/restore  # 恢复便签
DELETE /api/memos/{id}/force    # 永久删除
```

#### 分组接口
```
GET    /api/groups              # 获取分组列表
POST   /api/groups              # 创建分组
PUT    /api/groups/{id}         # 更新分组
DELETE /api/groups/{id}         # 删除分组
```

## 🎨 主题定制

### 颜色变量
```scss
// Zeekr 科技风格
$primary-color: #00D4AA;           // 主色调
$bg-dark: #0A0A0A;                 // 背景色
$bg-card: #1A1A1A;                 // 卡片背景
$text-primary: #FFFFFF;            // 主文字
$text-secondary: rgba(255,255,255,0.7);  // 次文字
$border-color: rgba(255,255,255,0.1);    // 边框色
```

### 修改主题
编辑 `client/src/assets/styles/main.scss` 中的变量即可自定义主题。

## ⚠️ 注意事项

1. **管理员账号**: 使用邮箱 `13167000126@163.com` 注册自动获得管理员权限
2. **数据持久化**: 免费部署平台的文件系统会在重新部署后重置，建议定期备份
3. **文件上传**: 上传的文件存储在服务器本地，生产环境建议接入云存储（如AWS S3、阿里云OSS）
4. **CORS配置**: 生产环境建议修改 `CorsConfig.java` 为特定域名，而非 `*`

## 📝 更新日志

### v1.0.0 (2024-01-XX)
- ✨ 初始版本发布
- ✅ 完整的用户系统（注册/登录/资料管理）
- ✅ 便签CRUD操作
- ✅ Markdown编辑器支持
- ✅ 分组管理
- ✅ 回收站功能
- ✅ 管理员后台
- ✅ Zeekr科技风格UI

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 开源协议

本项目基于 [MIT](LICENSE) 协议开源。

## 🙏 致谢

- [Vue.js](https://vuejs.org/) - 渐进式JavaScript框架
- [Spring Boot](https://spring.io/projects/spring-boot) - Java应用框架
- [Element Plus](https://element-plus.org/) - Vue 3组件库
- [Sa-Token](https://sa-token.cc/) - Java权限认证框架
- [Vditor](https://github.com/Vanessa219/vditor) - Markdown编辑器

## 📞 联系我们

如有问题或建议，欢迎提交 [Issue](https://github.com/yourusername/memo-app/issues)。

---

<p align="center">
  Made with ❤️ by AI Assistant
</p>
