// Archivo: LoginController.java
package controllers; // Usa tu paquete de controladores

import models.Usuario;
import models.DAO.UsuarioDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession; // O javax.servlet.http si usas Spring Boot 2

@Controller // Indica que esta clase es un controlador de Spring MVC
public class LoginController {

    private UsuarioDAO usuarioDAO = new UsuarioDAO(); 

    // Mapea la solicitud GET a /login (para mostrar el formulario)
    @GetMapping("/login")
    public String mostrarLogin() {
        // Retorna el nombre de la vista (Spring buscará /WEB-INF/views/login.jsp)
        return "login"; 
    }

    // Mapea la solicitud POST a /login (para procesar el formulario)
    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("usuario") String usuario, // Captura el parámetro 'usuario' del formulario
            @RequestParam("password") String password, // Captura el parámetro 'password'
            HttpSession session,
            Model model) { // Objeto para pasar datos a la vista

        Usuario u = usuarioDAO.validar(usuario, password);

        if (u != null) {
            session.setAttribute("usuario", u.getUsuario());
            // Redirección a la URL del Dashboard Controller
            return "redirect:/dashboard"; 
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            // Vuelve a la vista de login para mostrar el error
            return "login"; 
        }
    }
}