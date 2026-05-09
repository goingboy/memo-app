# 第一阶段：Maven构建
FROM maven:3.9-eclipse-temurin:21 AS builder

WORKDIR /build

# 复制整个server目录
COPY server/ .

# 构建项目
RUN mvn clean package -DskipTests -B

# 第二阶段：运行
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 从构建阶段复制jar文件
COPY --from=builder /build/target/*.jar app.jar

# 创建必要目录
RUN mkdir -p /app/data /app/uploads

# 暴露端口
EXPOSE 7860

# 设置环境变量
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=7860

# 启动应用
CMD ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${SERVER_PORT:-7860} -jar app.jar"]
