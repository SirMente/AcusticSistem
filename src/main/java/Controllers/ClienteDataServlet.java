// ClienteDataServlet.java
package controllers;

import com.google.gson.Gson; // Necesitarás la librería Gson para convertir Java a JSON
import models.DAO.ClienteDAO;
import models.Cliente;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ClienteData")
public class ClienteDataServlet extends HttpServlet {

    private ClienteDAO clienteDAO = new ClienteDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Obtener el parámetro 'docu' (DNI/RUC)
        String docuStr = request.getParameter("docu");

        response.setContentType("application/json"); // Indicamos que la respuesta es JSON
        response.setCharacterEncoding("UTF-8");

        if (docuStr == null || docuStr.trim().isEmpty()) {
            // Si el DNI/RUC es nulo o vacío, devolvemos un objeto vacío o nulo
            response.getWriter().write("{}");
            return;
        }

        Cliente cliente = null;
        try {
            // 2. Buscar el cliente en la base de datos
            // Usamos el método existente 'buscarClientePorDni' del DAO
            cliente = clienteDAO.buscarClientePorDni(docuStr);

            // 3. Devolver los datos del cliente como JSON
            if (cliente != null) {
                // Convertir el objeto Cliente a JSON
                String clienteJson = gson.toJson(cliente);
                response.getWriter().write(clienteJson);
            } else {
                // Si no se encuentra, devolver JSON vacío o un error específico si lo deseas
                response.getWriter().write("{\"error\": \"Cliente no encontrado\"}");
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al buscar cliente por DNI/RUC: " + e.getMessage());
            // En caso de error de BD, devolvemos un JSON de error
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Código 500
            response.getWriter().write("{\"error\": \"Error interno del servidor al buscar el cliente.\"}");
        }
    }
}
