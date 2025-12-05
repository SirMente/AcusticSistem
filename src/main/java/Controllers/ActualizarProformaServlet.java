package controllers;

import models.DAO.ProformaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/ActualizarProformaServlet")
public class ActualizarProformaServlet extends HttpServlet {

    private final ProformaDAO dao = new ProformaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idProforma = request.getParameter("id_proforma");
        String nuevoEstado = request.getParameter("estado");

        try {
            boolean actualizado = dao.actualizarEstadoYRegistrarIngreso(idProforma, nuevoEstado);

            if (!actualizado) {
                // Si no se actualizó, enviar mensaje de error a la JSP
                request.setAttribute("error", "No se pudo actualizar el estado de la proforma.");
                request.getRequestDispatcher("Proformas.jsp").forward(request, response);
                return;
            }

        } catch (SQLException e) {
            // Captura cualquier error en la base de datos
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al actualizar la proforma: " + e.getMessage());
            request.getRequestDispatcher("Proformas.jsp").forward(request, response);
            return;
        }

        // Redirige a la lista de proformas si todo salió bien
        response.sendRedirect("Proformas.jsp");
    }
}
