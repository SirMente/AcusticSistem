package Controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Proforma;
import models.Cliente;
import models.DAO.ProformaDAO;
import models.DAO.ClienteDAO;

@WebServlet("/GestionProformas")
public class ProformaController extends HttpServlet {

    private ProformaDAO proformaDAO;
    private ClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        // Asegúrate de que tus DAOs estén correctamente inicializados y conectados a la BD
        this.proformaDAO = new ProformaDAO();
        this.clienteDAO = new ClienteDAO();
    }

    // --- MANEJO DE LA VISTA (LISTAR) ---
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // ... (1. OBTENER y 2. ADJUNTAR listaProformas) ...
            List<Proforma> listaProformas = proformaDAO.listarProformas();
            request.setAttribute("listaProformas", listaProformas);

            // 3. Redirigir (forward) a la vista principal
            request.getRequestDispatcher("WEB-INF/views/gestionProformas.jsp").forward(request, response);

        } catch (Exception e) {
            // 🛑 SOLUCIÓN AL ERROR 404: Se cae aquí cuando falla la DB.
            e.printStackTrace();

            // 1. Adjuntar el mensaje de error
            request.setAttribute("error", "Error al cargar las proformas: " + e.getMessage() + ". Revisar conexión DB.");

            // 2. Redirigir a la misma página para mostrar el error en la alerta
            request.getRequestDispatcher("WEB-INF/views/gestionProformas.jsp").forward(request, response);
        }
    }

    // --- MANEJO DE LA INSERCIÓN (AGREGAR) ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            int idCliente = Integer.parseInt(request.getParameter("id_cliente"));
            String descripcionServicio = request.getParameter("descripcion_servicio");

            String presupuestoStr = request.getParameter("presupuesto");
            BigDecimal presupuesto = new BigDecimal(presupuestoStr);

            Date fechaEmision = Date.valueOf(request.getParameter("fecha_emision"));

            // Nota: Aquí falta recoger el campo 'cantidad' si lo incluiste en el modal.
            Proforma nuevaProforma = new Proforma(idCliente, descripcionServicio, presupuesto, fechaEmision);

            boolean exito = proformaDAO.agregarProforma(nuevaProforma);

            if (exito) {
                // Éxito: Redirigir al GET para mostrar la lista actualizada
                response.sendRedirect(request.getContextPath() + "/GestionProformas?mensaje=proforma_agregada");
            } else {
                request.setAttribute("error", "Error de BD al guardar la proforma. Revisa logs.");
                // Si falla el POST, recargamos la página llamando al GET para que se muestren los datos existentes
                doGet(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Error de formato. Asegúrate de que ID y Presupuesto sean números y la fecha sea válida.");
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error interno al procesar la proforma: " + e.getMessage());
            e.printStackTrace();
            doGet(request, response);
        }
    }
}
