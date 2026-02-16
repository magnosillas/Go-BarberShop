# ==========================================
# 🐳 GoBarber - Multi-stage Dockerfile
# ==========================================

# ==========================================
# Stage 1: Build
# ==========================================
FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copiar apenas arquivos de dependências primeiro (cache de layers)
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Baixar dependências (será cacheado se pom.xml não mudar)
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Build da aplicação (sem testes para acelerar)
RUN mvn clean package -DskipTests -B

# ==========================================
# Stage 2: Runtime
# ==========================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Criar usuário não-root para segurança
RUN addgroup -g 1001 -S gobarber && \
    adduser -u 1001 -S gobarber -G gobarber

# Copiar JAR do stage de build
COPY --from=build /app/target/*.jar app.jar

# Alterar proprietário do arquivo
RUN chown gobarber:gobarber app.jar

# Usar usuário não-root
USER gobarber

# Expor porta da aplicação
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Variáveis de ambiente padrão
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Entrypoint com suporte a JAVA_OPTS
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
