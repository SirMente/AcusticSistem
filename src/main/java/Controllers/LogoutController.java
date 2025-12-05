package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener sesión actual sin crear una nueva
        HttpSession sesion = request.getSession(false);

        if (sesion != null) {
            sesion.invalidate(); // 🔥 Cierra la sesión
        }

        // Redirigir al login
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
