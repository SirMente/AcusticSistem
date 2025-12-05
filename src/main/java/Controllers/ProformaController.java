package controllers;

import models.DAO.ServicioDAO;
import models.DAO.ProformaDAO;
import models.DAO.ClienteDAO;
import models.DAO.ProductoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Proforma;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Cliente;
import models.Producto;
import models.Servicio;
import models.DetalleItem;

@WebServlet("/GestionProformas")
public class ProformaController extends HttpServlet {

    private ProformaDAO proformaDAO;
    private ClienteDAO clienteDAO;
    private ServicioDAO servicioDAO;
    private ProductoDAO productoDAO;

    @Override
    public void init() throws ServletException {
        this.proformaDAO = new ProformaDAO();
        this.clienteDAO = new ClienteDAO();
        this.servicioDAO = new ServicioDAO();
        this.productoDAO = new ProductoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {

            if ("ver".equals(accion)) {
                // Obtener ID
                String idProforma = request.getParameter("id");

                Proforma pf = proformaDAO.obtenerProformaConDetalles(idProforma);

                if (pf == null) {
                    request.setAttribute("error", "La proforma no existe.");
                    doGet(request, response);
                    return;
                }

                request.setAttribute("proforma", pf);
                request.getRequestDispatcher("views/verProforma.jsp").forward(request, response);
                return;
            }

            // ACCIÓN POR DEFECTO → LISTADO
            List<Proforma> listaProformas = proformaDAO.obtenerTodasLasProformas();
            request.setAttribute("listaProformas", listaProformas);

            List<Cliente> listaClientes = clienteDAO.obtenerClientesParaSelect();
            request.setAttribute("listaClientes", listaClientes);

            List<Servicio> listaServicios = servicioDAO.listarServicios();
            request.setAttribute("listaServicios", listaServicios);

            List<Producto> listaProductos = productoDAO.listarProductos();
            request.setAttribute("listaProductos", listaProductos);

            request.getRequestDispatcher("views/gestionProformas.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error de base de datos: " + e.getMessage());
            request.getRequestDispatcher("views/gestionProformas.jsp").forward(request, response);
        }
    }
    // -------------------------------------------------------------------------

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Determinar qué acción se está realizando (nueva proforma o cambio de estado)
        String accion = request.getParameter("accion");

        if ("cambiarEstado".equals(accion)) {
            handleCambiarEstado(request, response);
        } else {
            // Asumimos que cualquier otro POST sin 'accion' o con 'agregar' es para crear una proforma.
            handleAgregarProforma(request, response);
        }
    }

// Método para cambiar el estado de una proforma
private void handleCambiarEstado(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String idProformaStr = request.getParameter("idProforma");
    String nuevoEstado = request.getParameter("nuevoEstado");

    // Validación básica
    if (idProformaStr == null || idProformaStr.trim().isEmpty() ||
        nuevoEstado == null || nuevoEstado.trim().isEmpty()) {

        request.setAttribute("error", "Faltan datos para cambiar el estado de la proforma.");
        doGet(request, response);
        return;
    }

    try {
        boolean exito = proformaDAO.actualizarEstadoProforma(idProformaStr, nuevoEstado);

        if (exito) {
            String urlRedireccion = request.getContextPath() + "/GestionProformas?mensaje=estado_actualizado";

            // Si el estado requiere generar un PDF, AÑADIMOS los parámetros a la URL
            if ("PAGADA_PARCIAL".equals(nuevoEstado) || "PAGADA_TOTAL".equals(nuevoEstado)) {
                // AGREGAMOS parámetros para que la JSP sepa qué PDF descargar al cargar
                urlRedireccion += "&descargarPDF=true";
                urlRedireccion += "&pdfId=" + idProformaStr;
                urlRedireccion += "&pdfEstado=" + nuevoEstado;
            }

            // ⚠️ La redirección es ahora SIEMPRE a la página principal
            response.sendRedirect(urlRedireccion);
            return;

        } else {
            request.setAttribute("error",
                    "No se pudo actualizar el estado de la proforma " + idProformaStr
                    + ". La proforma podría no existir.");
            doGet(request, response);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        request.setAttribute("error",
                "Error de BD al actualizar estado: " + e.getMessage());
        doGet(request, response);
    }
}


    // ⭐ MÉTODO EXISTENTE PARA AGREGAR PROFORMA (MOVIDO) ⭐
    private void handleAgregarProforma(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // El documento del cliente seleccionado
        String docuCliente = request.getParameter("id_cliente");
        String presupuestoStr = request.getParameter("presupuesto");
        String fechaEmisionStr = request.getParameter("fecha_emision");

        if (docuCliente == null || presupuestoStr == null || fechaEmisionStr == null || docuCliente.trim().isEmpty()) {
            request.setAttribute("error", "Faltan datos obligatorios (Documento, Presupuesto, Fecha).");
            doGet(request, response);
            return;
        }

        try {
            BigDecimal total = new BigDecimal(presupuestoStr);
            Date fechaEmision = Date.valueOf(fechaEmisionStr);

            Proforma nuevaProforma = new Proforma();
            nuevaProforma.setDniCliente(docuCliente);
            nuevaProforma.setTotal(total);
            nuevaProforma.setFechaProforma(fechaEmision);

            // ⭐ RECUPERAR LOS ÍTEMS DEL DETALLE ⭐
            List<DetalleItem> detalles = new ArrayList<>();
            int i = 0;
            // Iteramos hasta que el parámetro oculto 'detalle_items[i].id' ya no exista
            while (request.getParameter("detalle_items[" + i + "].id") != null) {

                DetalleItem item = new DetalleItem();

                // Mapeo de los campos ocultos del JSP a la clase DetalleItem
                item.setTipo(Integer.parseInt(request.getParameter("detalle_items[" + i + "].tipo")));
                item.setId(Integer.parseInt(request.getParameter("detalle_items[" + i + "].id")));
                item.setCantidad(Integer.parseInt(request.getParameter("detalle_items[" + i + "].cantidad")));
                // Usamos Double.parseDouble para el precio (si el input oculto está bien formateado)
                item.setPrecioUnitario(Double.parseDouble(request.getParameter("detalle_items[" + i + "].precioUnitario")));

                detalles.add(item);
                i++;
            }

            // VALIDACIÓN CRÍTICA: Se debe asegurar que haya al menos un ítem
            if (detalles.isEmpty()) {
                request.setAttribute("error", "La proforma debe contener al menos un ítem (producto o servicio).");
                doGet(request, response);
                return;
            }

            // ⭐ LLAMADA A UN NUEVO MÉTODO DAO PARA GUARDAR TODO ⭐
            boolean exito = proformaDAO.agregarProformaConDetalles(nuevaProforma, detalles);

            if (exito) {
                response.sendRedirect(request.getContextPath() + "/GestionProformas?mensaje=proforma_agregada");
            } else {
                request.setAttribute("error", "No se pudo agregar la proforma o sus detalles por una razón desconocida.");
                doGet(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Formato numérico o de fecha inválido: " + e.getMessage());
            doGet(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al crear Proforma y su detalle: " + e.getMessage());
            doGet(request, response);
        }
    }
}
