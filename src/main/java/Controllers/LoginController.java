package Controllers;

import models.Usuario;
import models.DAO.UsuarioDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    // Instancia de tu capa de acceso a datos para validar credenciales
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Maneja las solicitudes GET (Mostrar el formulario de Login).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Muestra la vista de login, ubicada dentro de WEB-INF (protegida)
        request.getRequestDispatcher("WEB-INF/views/login.jsp").forward(request, response);
    }

    /**
     * Maneja las solicitudes POST (Procesar el intento de Login).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Obtener parámetros del formulario (deben coincidir con el 'name' en el JSP)
        String usuario = request.getParameter("usuario"); 
        String password = request.getParameter("password");

        // 2. Validar credenciales
        Usuario u = usuarioDAO.validar(usuario, password);

        if (u != null) {
            // LOGIN EXITOSO
            
            // 3. Crear o recuperar la sesión
            HttpSession sesion = request.getSession();
            
            // 4. Guardar datos del usuario en la sesión para uso posterior (ej. en el Dashboard)
            sesion.setAttribute("usuarioLogueado", u.getUsuario()); 
            
            // 5. Redireccionar al controlador del Dashboard
            // Usamos response.sendRedirect() para evitar el doble envío de formulario (POST-redirect-GET pattern).
            // Usamos request.getContextPath() para asegurar una URL absoluta, independientemente del contexto de despliegue.
            response.sendRedirect(request.getContextPath() + "/dashboard");
            
        } else {
            // LOGIN FALLIDO
            
            // 6. Agregar un mensaje de error al objeto request
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            
            // 7. Volver a mostrar la vista de login
            // Usamos forward para mantener el mensaje de error en el objeto request.
            request.getRequestDispatcher("WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}