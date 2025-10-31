<<<<<<< HEAD
package Controllers;

import models.Usuario;
import models.DAO.UsuarioDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

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
=======
package controllers;

import jakarta.servlet.http.HttpSession;
import models.Usuario;
import models.DAO.UsuarioDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @GetMapping("/")
    public String redirigirAlLogin(HttpSession session) {
        if (session.getAttribute("usuario") != null) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("usuario") String usuario,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        Usuario u = usuarioDAO.validar(usuario, password);
        if (u != null) {
            session.setAttribute("usuario", u.getUsuario());
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
>>>>>>> d98825eb0d5aa8b5aa06635a1770d7dbaa48e993
