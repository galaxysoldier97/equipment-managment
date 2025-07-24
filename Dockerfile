# Etapa 1: Compilación con Maven
FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /app

ENV MAVEN_REPO_LOCAL=/root/.m2/repository



# Copiar config de Maven y dependencias necesarias
COPY .m2/settings.xml /root/.m2/settings.xml
COPY pom.xml .
COPY .m2/repository $MAVEN_REPO_LOCAL
COPY libs/parent-5.0.10.pom $MAVEN_REPO_LOCAL/mc/monacotelecom/buildfwk/parent/5.0.10/parent-5.0.10.pom
 

COPY . .

 

# Descargar dependencias (modo offline) y compilar
RUN mvn -T 2C dependency:go-offline -Dmaven.repo.local=$MAVEN_REPO_LOCAL
RUN mvn -T 2C clean install \
    -DskipTests \
    -Dgit-commit-id.skip=true \
    -fn \
    -Dmaven.repo.local=$MAVEN_REPO_LOCAL

# Subir a Nexus si aplica (opcional para builds locales)
RUN mvn deploy -DskipTests --settings /root/.m2/settings.xml

# Etapa 2: Imagen final liviana solo con el .jar
FROM eclipse-temurin:11-jre AS runtime

WORKDIR /app

# Copiar solo el jar final desde el builder
COPY --from=builder /app/*.jar app.jar

# Exponer el puerto si aplica
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
