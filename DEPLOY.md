# Memo App 部署指南

## 📋 项目概述

Memo App 是一个全栈便签应用，采用以下技术栈：
- **前端**: Vue 3 + Vite + Element Plus + Pinia
- **后端**: Spring Boot 3 + Sa-Token + MyBatis Plus + H2 Database

## 🚀 部署架构

```
┌─────────────────┐     ┌─────────────────┐
│   Vercel (前端)  │────▶│ Railway (后端)   │
│   memo-app-ui   │     │ memo-app-server │
└─────────────────┘     └────────┬────────┘
                                 │
                    ┌────────────┴────────────┐
                    │    H2 Database (文件)    │
                    └─────────────────────────┘
```

## 📦 后端部署 (Railway/Render)

### 方式一: Railway (推荐)

1. **注册账号**
   - 访问 https://railway.app
   - 使用 GitHub 账号登录

2. **创建项目**
   ```bash
   # 将代码推送到 GitHub
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin https://github.com/yourusername/memo-app.git
   git push -u origin main
   ```

3. **部署到 Railway**
   - 在 Railway 控制台点击 "New Project"
   - 选择 "Deploy from GitHub repo"
   - 选择你的仓库
   - Railway 会自动识别 `railway.json` 配置

4. **配置环境变量**
   - `JAVA_OPTS`: `-Xmx512m -Xms256m`
   - `SPRING_PROFILES_ACTIVE`: `prod`

5. **获取域名**
   - 部署完成后，Railway 会提供一个域名
   - 例如: `https://memo-app-server.up.railway.app`

### 方式二: Render

1. **注册账号**
   - 访问 https://render.com
   - 使用 GitHub 账号登录

2. **创建 Web Service**
   - 点击 "New +" → "Web Service"
   - 连接 GitHub 仓库
   - Render 会自动识别 `render.yaml`

3. **配置**
   - 选择免费计划 (Free)
   - 点击创建，等待部署完成

## 🎨 前端部署 (Vercel)

1. **注册账号**
   - 访问 https://vercel.com
   - 使用 GitHub 账号登录

2. **导入项目**
   - 点击 "Add New Project"
   - 导入 `memo-app` 仓库
   - 设置根目录为 `client`

3. **配置环境变量**
   - `VITE_API_BASE_URL`: 你的后端地址
   - 例如: `https://memo-app-server.up.railway.app`

4. **部署**
   - 点击 Deploy，等待构建完成
   - Vercel 会提供一个域名，例如: `https://memo-app.vercel.app`

## 🔧 本地开发

### 后端启动

```bash
cd memo-app/server
./mvnw spring-boot:run
```

后端运行在 http://localhost:8080

### 前端启动

```bash
cd memo-app/client
npm install
npm run dev
```

前端运行在 http://localhost:3000

## 📁 项目结构

```
memo-app/
├── client/                    # 前端 Vue3 项目
│   ├── src/
│   │   ├── api/              # API 接口
│   │   ├── assets/           # 静态资源
│   │   ├── components/       # 组件
│   │   ├── router/           # 路由配置
│   │   ├── stores/           # Pinia 状态管理
│   │   ├── utils/            # 工具函数
│   │   ├── views/            # 页面视图
│   │   ├── App.vue           # 根组件
│   │   └── main.js           # 入口文件
│   ├── package.json
│   ├── vite.config.js
│   └── vercel.json           # Vercel 部署配置
│
├── server/                    # 后端 Spring Boot 项目
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/memo/
│   │       │       ├── config/      # 配置类
│   │       │       ├── controller/  # 控制器
│   │       │       ├── dto/         # 数据传输对象
│   │       │       ├── entity/      # 实体类
│   │       │       ├── exception/   # 异常处理
│   │       │       ├── mapper/      # MyBatis Mapper
│   │       │       ├── service/     # 业务逻辑
│   │       │       └── util/        # 工具类
│   │       └── resources/
│   │           ├── application.yml      # 主配置
│   │           ├── application-prod.yml # 生产配置
│   │           └── mapper/              # XML 映射文件
│   ├── Dockerfile            # Docker 构建文件
│   ├── railway.json          # Railway 配置
│   ├── render.yaml           # Render 配置
│   └── pom.xml               # Maven 配置
│
└── DEPLOY.md                 # 本部署文档
```

## 🔐 管理员账号

默认管理员邮箱: `13167000126@163.com`

使用该邮箱注册的用户会自动获得管理员权限。

## 📝 注意事项

1. **免费额度**
   - Railway: 每月 $5 免费额度
   - Render: 免费实例会在 15 分钟无活动后休眠
   - Vercel: 免费版无限静态托管

2. **数据持久化**
   - H2 数据库使用文件存储
   - Railway/Render 的文件系统在重新部署后会重置
   - 如需持久化数据，建议升级到付费计划或使用外部数据库

3. **CORS 配置**
   - 后端已配置允许所有来源 (`*`)
   - 生产环境建议修改为特定域名

4. **文件上传**
   - 上传的文件存储在服务器本地
   - 重新部署后会丢失，建议后续接入云存储

## 🆘 故障排查

### 后端无法启动

```bash
# 检查日志
railway logs

# 本地测试
cd server
./mvnw clean package
java -jar target/memo-app-server-1.0.0.jar
```

### 前端无法连接后端

1. 检查 `VITE_API_BASE_URL` 环境变量
2. 确认后端 CORS 配置
3. 检查浏览器控制台网络请求

### 数据库问题

```bash
# 删除数据库文件重新初始化
rm -rf server/data/
```

## 📞 技术支持

如有问题，请查看：
- Spring Boot 文档: https://spring.io/projects/spring-boot
- Vue 3 文档: https://vuejs.org/
- Element Plus: https://element-plus.org/
- Sa-Token: https://sa-token.cc/

---

**部署完成！🎉**
