# ─── Stage 1: Build ───────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /workspace

# Copy pom trước để tận dụng Docker layer cache
COPY pom.xml .

# Download dependencies (cache layer riêng — chỉ re-download khi pom thay đổi)
RUN mvn dependency:go-offline -q

# Copy source rồi build, bỏ qua test (test chạy trong CI, không trong image)
COPY src src
RUN mvn clean package -DskipTests -q

# Unpack layered JAR để tối ưu Docker layer cache cho lần build sau
RUN java -Djarmode=layertools -jar target/sba-call-api-gemini-demo-0.0.1-SNAPSHOT.jar extract --destination target/extracted

# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine AS runtime

# Tạo user non-root để không chạy app bằng root (security best practice)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

# Copy các layer theo thứ tự ít thay đổi → hay thay đổi
# (dependencies thay đổi ít hơn application code)
COPY --from=builder /workspace/target/extracted/dependencies/ ./
COPY --from=builder /workspace/target/extracted/spring-boot-loader/ ./
COPY --from=builder /workspace/target/extracted/snapshot-dependencies/ ./
COPY --from=builder /workspace/target/extracted/application/ ./

EXPOSE 8080

# Tuỳ chỉnh JVM: giới hạn heap phù hợp container, bật GC logging nếu cần
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]
