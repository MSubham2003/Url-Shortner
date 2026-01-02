# ---------- Build stage (Java 21) ----------
FROM jelastic/maven:3.9.4-openjdk-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

# ---------- Runtime stage (Java 21) ----------
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Run as non-root (mandatory for prod)
#RUN useradd -r -u 1001 appuser
#USER appuser

# Copy the fat jar
COPY --from=builder /app/target/*.jar /app.jar

# Container-aware JVM tuning for prod
ENV JAVA_OPTS="\
  -XX:+UseG1GC \
  -XX:+UseContainerSupport \
  -XX:MaxRAMPercentage=70 \
  -XX:MaxGCPauseMillis=200 \
  -XX:InitiatingHeapOccupancyPercent=45 \
  -XX:+UseStringDeduplication \
  -XX:+TieredCompilation \
  -XX:TieredStopAtLevel=1 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/tmp/heapdump.hprof \
#  -Xlog:gc*,gc+heap=info \
  -Djava.security.egd=file:/dev/urandom \
  -Dfile.encoding=UTF-8 \
  -Djava.awt.headless=true \
  -XshowSettings:vm"

EXPOSE 8080

# Start the application with the configured JVM options # JAVA_OPTS is explicitly passed to ensure GC and memory settings are applied
CMD ["sh", "-c", "exec java $JAVA_OPTS -jar /app.jar"]
