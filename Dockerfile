FROM maven:3.9-eclipse-temurin:21

WORKDIR /app

COPY server/ .

RUN mvn clean package -DskipTests -B

RUN mkdir -p /app/data /app/uploads

EXPOSE 7860

ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=7860

CMD ["sh", "-c", "java -Dserver.port=${SERVER_PORT:-7860} -jar target/*.jar"]
