package Controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import models.Cliente;
import models.DAO.ClienteDAO;

@WebServlet("/ClienteData")
public class ClienteDataServlet extends HttpServlet {

    private ClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        this.clienteDAO = new ClienteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            out.print("{\"error\": \"ID no proporcionado.\"}");
            return;
        }
        
        try {
            int idCliente = Integer.parseInt(idStr);
            Cliente cliente = clienteDAO.obtenerClientePorId(idCliente); // Llama al DAO

            if (cliente != null) {
                // 1. Cliente encontrado: Devolvemos JSON 200 OK
                Gson gson = new Gson();
                String jsonOutput = gson.toJson(cliente);
                out.print(jsonOutput);
            } else {
                // 2. Cliente NO encontrado: Devolvemos 404
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); 
                out.print("{\"error\": \"Cliente con ID " + idCliente + " no existe.\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            out.print("{\"error\": \"El ID debe ser un número entero.\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
            System.err.println("Error de BD en ClienteDataServlet: " + e.getMessage());
            out.print("{\"error\": \"Error en la base de datos.\"}");
        }
    }
}