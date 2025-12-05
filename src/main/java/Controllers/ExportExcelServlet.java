package controllers;

import models.DAO.FinanzaDAO;
import models.Finanza;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

@WebServlet("/ExportExcelServlet")
public class ExportExcelServlet extends HttpServlet {

    private final FinanzaDAO dao = new FinanzaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Finanza> lista = dao.listarFinanzas();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Finanzas");

            // Crear cabecera
            Row header = sheet.createRow(0);
            String[] columnas = {"Fecha", "Categoría", "Tipo", "Descripción", "Monto"};
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columnas[i]);
            }

            // Llenar filas
            int rowNum = 1;
            for (Finanza f : lista) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(f.getFecha().toString());
                row.createCell(1).setCellValue(f.getCategoria());
                row.createCell(2).setCellValue(f.getTipo());
                row.createCell(3).setCellValue(f.getDescripcion());
                row.createCell(4).setCellValue(f.getMonto());
            }

            // Ajustar ancho de columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Configurar la respuesta
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=finanzas.xlsx");
            workbook.write(response.getOutputStream());
        }
    }
}
