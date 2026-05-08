# Memo App - 全栈便签应用

一个功能完整的便签应用，采用 Zeekr 科技风格设计，支持 Markdown 编辑、分组管理、回收站等功能。

## ✨ 功能特性

### 用户系统
- ✅ 用户注册/登录（邮箱+密码）
- ✅ 个人资料管理（昵称、头像）
- ✅ Sa-Token 权限认证

### 便签管理
- ✅ 创建/编辑/删除便签
- ✅ Markdown 编辑器 + 实时预览
- ✅ 便签分组管理
- ✅ 回收站（软删除 + 恢复）

### 管理员功能
- ✅ 用户管理（查看、禁用账号）
- ✅ 默认管理员邮箱: `13167000126@163.com`

### UI 设计
- ✅ Zeekr 科技风格深色主题
- ✅ 响应式设计（移动端/平板/桌面）
- ✅ 卡片式布局
- ✅ 流畅动画效果

## 🛠️ 技术栈

### 后端
- **框架**: Spring Boot 3.2.5
- **安全**: Sa-Token 1.37.0
- **ORM**: MyBatis Plus 3.5.5
- **数据库**: H2 Database（嵌入式）
- **工具库**: Hutool 5.8.25
- **文档**: SpringDoc OpenAPI 2.3.0

### 前端
- **框架**: Vue 3.4.21
- **构建**: Vite 5.2.0
- **UI库**: Element Plus 2.7.0
- **状态管理**: Pinia 2.1.7
- **路由**: Vue Router 4.3.0
- **Markdown**: Vditor 3.10.8 + Marked 12.0.1
- **HTTP**: Axios 1.6.8

## 📁 项目结构

```
memo-app/
├── client/                    # 前端 Vue3 项目
│   ├── src/
│   │   ├── api/              # API 接口 (auth, memo, group, admin)
│   │   ├── assets/           # 静态资源
│   │   │   └── styles/       # SCSS 样式
│   │   ├── router/           # 路由配置
│   │   ├── stores/           # Pinia 状态管理
│   │   ├── utils/            # 工具函数
│   │   ├── views/            # 页面视图
│   │   │   ├── Login.vue
│   │   │   ├── Register.vue
│   │   │   ├── Home.vue
│   │   │   ├── MemoDetail.vue
│   │   │   ├── MemoEdit.vue
│   │   │   ├── Trash.vue
│   │   │   ├── Profile.vue
│   │   │   └── Admin.vue
│   │   ├── App.vue
│   │   └── main.js
│   ├── package.json
│   ├── vite.config.js
│   └── vercel.json
│
├── server/                    # 后端 Spring Boot 项目
│   ├── src/main/java/com/memo/
│   │   ├── config/           # 配置类
│   │   ├── controller/       # 控制器 (8个)
│   │   ├── dto/              # 数据传输对象
│   │   ├── entity/           # 实体类
│   │   ├── mapper/           # MyBatis Mapper
│   │   ├── service/          # 业务逻辑
│   │   └── MemoApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   ├── application-prod.yml
│   │   └── schema.sql
│   ├── Dockerfile
│   ├── railway.json
│   ├── render.yaml
│   └── pom.xml
│
├── DEPLOY.md                 # 部署指南
└── README.md                 # 项目说明
```

## 🚀 快速开始

### 本地开发

**1. 启动后端**
```bash
cd server
./mvnw spring-boot:run
# 或
mvn spring-boot:run
```
后端运行在 http://localhost:8080

**2. 启动前端**
```bash
cd client
npm install
npm run dev
```
前端运行在 http://localhost:3000

### 生产部署

查看 [DEPLOY.md](./DEPLOY.md) 获取详细的部署指南。

**部署架构:**
- 前端: Vercel (免费)
- 后端: Railway 或 Render (免费)
- 数据库: H2 嵌入式 (文件存储)

## 📸 页面预览

| 页面 | 描述 |
|------|------|
| 登录页 | 深色主题登录表单 |
| 注册页 | 用户注册，支持头像上传 |
| 首页 | 卡片式便签列表，分组筛选 |
| 详情页 | Markdown 渲染 + 分享功能 |
| 编辑页 | Vditor Markdown 编辑器 |
| 回收站 | 已删除便签管理 |
| 个人资料 | 用户信息修改 |
| 管理后台 | 用户管理（仅管理员）|

## 🔌 API 接口

### 认证接口
- `POST /api/auth/register` - 注册
- `POST /api/auth/login` - 登录
- `GET /api/auth/profile` - 获取用户信息
- `PUT /api/auth/profile` - 更新用户信息
- `POST /api/auth/avatar` - 上传头像

### 便签接口
- `GET /api/memos` - 获取便签列表
- `GET /api/memos/{id}` - 获取便签详情
- `POST /api/memos` - 创建便签
- `PUT /api/memos/{id}` - 更新便签
- `DELETE /api/memos/{id}` - 删除便签（软删除）

### 回收站接口
- `GET /api/memos/trash` - 获取回收站列表
- `POST /api/memos/{id}/restore` - 恢复便签
- `DELETE /api/memos/{id}/force` - 永久删除

### 分组接口
- `GET /api/groups` - 获取分组列表
- `POST /api/groups` - 创建分组
- `PUT /api/groups/{id}` - 更新分组
- `DELETE /api/groups/{id}` - 删除分组

### 管理员接口
- `GET /api/admin/users` - 获取用户列表
- `PUT /api/admin/users/{id}/status` - 更新用户状态

### 健康检查
- `GET /api/health` - 服务健康状态

## 🎨 UI 设计规范

### 颜色方案
- 背景色: `#0A0A0A`
- 卡片背景: `#1A1A1A`
- 主色调: `#00D4AA` (Zeekr绿)
- 文字主色: `#FFFFFF`
- 文字次色: `rgba(255,255,255,0.7)`
- 边框色: `rgba(255,255,255,0.1)`

### 字体
- 主字体: `Inter, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif`

### 圆角
- 卡片: 16px
- 按钮: 8px
- 输入框: 8px

## 📝 开发文档

- [产品需求文档 (PRD)](./docs/PRD.md)
- [技术方案文档](./docs/Technical_Solution.md)
- [数据库设计文档](./docs/Database_Design.md)
- [UI设计规范](./docs/UI_Design.md)

## ⚠️ 注意事项

1. **管理员账号**: 使用邮箱 `13167000126@163.com` 注册自动获得管理员权限
2. **数据持久化**: 免费部署平台的文件系统会在重新部署后重置
3. **文件上传**: 上传的文件存储在服务器本地，建议后续接入云存储
4. **CORS**: 生产环境建议修改 CORS 配置为特定域名

## 📄 开源协议

MIT License

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

---

**Made with ❤️ by AI Assistant**
