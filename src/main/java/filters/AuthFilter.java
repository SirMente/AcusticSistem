package filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter({"/dashboard", "/Proveedores", "/GestionClientes", "/ServicioController",
    "/GestionPersonal", "/InventarioController", "/Finanzas",
    "/GestionProformas", "/Proyectos"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession sesion = req.getSession(false);
        boolean logueado = (sesion != null && sesion.getAttribute("usuarioLogueado") != null);

        if (!logueado) {
            res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            res.setHeader("Pragma", "no-cache");
            res.setDateHeader("Expires", 0);
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Evitar cache de páginas privadas
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        chain.doFilter(request, response);
    }

}
