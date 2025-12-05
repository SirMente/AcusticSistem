# ===============================================
# ETAPA 1: BUILDER (Construye el Proyecto Maven)
# ===============================================
# Usamos una imagen que ya tiene Maven y Java 21 (Temurin)
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el pom.xml para descargar dependencias primero (cache)
COPY pom.xml .

# Descarga las dependencias
RUN mvn dependency:go-offline

# Copia todo el código fuente
COPY . .

# Ejecuta la construcción de Maven (genera el WAR en target/)
RUN mvn clean install -DskipTests

# ===============================================
# ETAPA 2: RUNTIME (Ejecuta la Aplicación en Tomcat 11)
# ===============================================
# Usamos la imagen base ligera de Tomcat
FROM tomcat:11.0.21-jdk21-temurin

# 1. Copia el archivo WAR DE LA ETAPA ANTERIOR a la carpeta webapps
# El nombre del WAR es el artefacto de tu pom.xml
COPY --from=builder /app/target/SistemaAcustic-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# 2. Exponer el puerto
EXPOSE 8080

# 3. Comando de inicio de Tomcat (Ya definido en la imagen base, pero explícito es mejor)
CMD ["catalina.sh", "run"]