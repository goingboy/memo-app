# 第一阶段：Maven构建
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# 先复制pom.xml，利用Docker缓存依赖
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源代码并构建
COPY src ./src
RUN mvn clean package -DskipTests -B

# 第二阶段：运行
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 从构建阶段复制jar文件
COPY --from=builder /build/target/*.jar app.jar

# 创建上传目录
RUN mkdir -p /app/uploads/avatars /app/uploads/images

# 暴露端口（Render使用PORT环境变量）
EXPOSE 8080

# 设置环境变量
ENV JAVA_OPTS="-Xmx384m -Xms128m"
ENV SPRING_PROFILES_ACTIVE=prod

# 启动应用
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
