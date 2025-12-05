package controllers;

import models.DAO.FinanzaDAO;
import models.Finanza;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/Finanzas")
public class FinanzaServlet extends HttpServlet {

    private final FinanzaDAO dao = new FinanzaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String tipo = request.getParameter("tipo");
            String categoria = request.getParameter("categoria");
            String descripcion = request.getParameter("descripcion");
            double monto = Double.parseDouble(request.getParameter("monto"));
            Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("fecha"));

            Finanza f = new Finanza();
            f.setTipo(tipo);
            f.setCategoria(categoria);
            f.setDescripcion(descripcion);
            f.setMonto(monto);
            f.setFecha(fecha);

            dao.agregarFinanza(f);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Redirigir al GET para recargar la lista
        response.sendRedirect(request.getContextPath() + "/Finanzas");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Finanza> lista = dao.listarFinanzas();
        double ingresos = dao.calcularTotalIngresos();
        double gastos = dao.calcularTotalGastos();
        double balance = ingresos - gastos;

        request.setAttribute("listaFinanzas", lista);
        request.setAttribute("totalBalance", balance);
        request.setAttribute("variacion", "↑10%"); // Puedes calcular dinámicamente
        request.setAttribute("margenGanancia", ingresos != 0 ? Math.round((balance / ingresos) * 100) + "%" : "0%");

        request.getRequestDispatcher("views/gestionFinanzas.jsp").forward(request, response);
    }
}
