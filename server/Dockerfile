# 使用官方OpenJDK 17镜像作为基础镜像
FROM eclipse-temurin:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制Maven构建的jar文件
COPY target/memo-app-server-1.0.0.jar app.jar

# 创建上传目录
RUN mkdir -p /app/uploads/avatars /app/uploads/images

# 暴露端口
EXPOSE 8080

# 设置环境变量
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=prod

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
