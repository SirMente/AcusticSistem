# Usar una imagen base de Tomcat
FROM tomcat:11.0.21-jdk21-temurin

# Copiar el WAR al directorio de despliegue de Tomcat
COPY target/SistemaAcustic-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Exponer el puerto que Tomcat usa
EXPOSE 8080

# Comando por defecto para ejecutar Tomcat
CMD ["catalina.sh", "run"]
