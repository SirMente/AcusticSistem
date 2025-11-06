package Controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // **OPCIONAL PERO RECOMENDADO:** Verificar si el usuario está logueado
        if (request.getSession().getAttribute("usuarioLogueado") == null) {
            // Si no hay sesión, redirige de vuelta al login
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Carga y muestra la vista del Dashboard (usa forward)
        request.getRequestDispatcher("WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}