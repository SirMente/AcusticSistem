package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // 1. DEFINICIÓN DE VARIABLES DE ENTORNO (Render)
    // Estas variables leerán los valores que configuraste en el dashboard de Render.
    private static final String DB_HOST = System.getenv("DB_HOST");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    private static final String DB_NAME = System.getenv("DB_NAME");
    private static final String DB_PORT = System.getenv("DB_PORT"); // Puerto estándar de Postgres: 5432

    // 2. CONSTRUCCIÓN DINÁMICA DE LA URL (PostgreSQL)
    // Usamos el driver y el formato de URL de PostgreSQL.
    // La URL será: jdbc:postgresql://[HOST_INTERNO]:5432/[DB_NAME]
    private static final String JDBC_URL = 
        "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;

    // 3. DRIVER DE POSTGRESQL
    private static final String JDBC_DRIVER = "org.postgresql.Driver"; 

    public static Connection getConnection() {
        Connection con = null;
        
        // Verificación básica de que las variables de entorno están cargadas
        if (DB_HOST == null || DB_HOST.isEmpty()) {
            System.err.println("❌ Error: DB_HOST no está definido. Asegúrate de configurar las Variables de Entorno en Render.");
            return null;
        }

        try {
            // Cargar el driver de PostgreSQL
            Class.forName(JDBC_DRIVER);
            
            // Establecer la conexión usando la URL dinámica y las credenciales de Render
            con = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
            System.out.println("✅ Conexión exitosa a la BD PostgreSQL en Render.");
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: No se pudo cargar el driver JDBC: " + JDBC_DRIVER + ". ¿Agregaste la dependencia de PostgreSQL al pom.xml?");
        } catch (SQLException e) {
             System.err.println("❌ Error de conexión a la BD: " + e.getMessage());
             System.err.println("URL de conexión usada: " + JDBC_URL);
        }
        return con;
    }
}