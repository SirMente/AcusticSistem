package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import models.DAO.ClienteDAO;
import models.DAO.ProyectoDAO;
import models.DAO.FinanzaDAO;
import models.DAO.ProductoDAO;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProyectoDAO proyectoDAO = new ProyectoDAO();
    private final FinanzaDAO finanzaDAO = new FinanzaDAO();
    private final ProductoDAO inventarioDAO = new ProductoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validar sesión
        if (request.getSession().getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // =======================
        // 1. KPI DEL DASHBOARD
        // =======================

        int totalClientes = clienteDAO.contarClientes();
        int totalProyectos = proyectoDAO.contarProyectos();
        int productosConBajoStock = inventarioDAO.contarStockBajo();

        double ingresos = finanzaDAO.calcularTotalIngresos();
        double gastos   = finanzaDAO.calcularTotalGastos();
        double margenGanancia = ingresos != 0 ? ( (ingresos - gastos) / ingresos * 100 ) : 0;

        request.setAttribute("clientes", totalClientes);
        request.setAttribute("proyectos", totalProyectos);
        request.setAttribute("stock_bajo", productosConBajoStock);
        request.setAttribute("margen", Math.round(margenGanancia * 10.0) / 10.0);

        // =======================
        // 2. DATOS PARA GRÁFICOS
        // =======================

        // Gráfico Finanzas
        Map<String, Double> ingresosPorMes = finanzaDAO.obtenerIngresosMensuales();
        Map<String, Double> gastosPorMes   = finanzaDAO.obtenerGastosMensuales();

        request.setAttribute("ingresosPorMes", ingresosPorMes);
        request.setAttribute("gastosPorMes", gastosPorMes);

        // Gráfico Proyectos: AGRUPAR ESTADOS
        Map<String, Integer> estadosBD = proyectoDAO.contarPorEstado();

        Map<String, Integer> proyectosPorEstado = new LinkedHashMap<>();
        proyectosPorEstado.put("Activos", 0);
        proyectosPorEstado.put("En Pausa", 0);
        proyectosPorEstado.put("Finalizados", 0);

        if (estadosBD != null) {
            for (Map.Entry<String, Integer> entry : estadosBD.entrySet()) {
                String estado = entry.getKey();
                int cantidad = entry.getValue();

                switch (estado) {
                    case "Instalacion":
                    case "Pruebas":
                        proyectosPorEstado.put("Activos", proyectosPorEstado.get("Activos") + cantidad);
                        break;
                    case "Planificacion":
                        proyectosPorEstado.put("En Pausa", proyectosPorEstado.get("En Pausa") + cantidad);
                        break;
                    case "Entrega":
                        proyectosPorEstado.put("Finalizados", proyectosPorEstado.get("Finalizados") + cantidad);
                        break;
                }
            }
        }

        request.setAttribute("proyectosPorEstado", proyectosPorEstado);

        // Enviar a la vista
        request.getRequestDispatcher("views/dashboard.jsp").forward(request, response);
    }
}
