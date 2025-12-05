package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import models.Proforma;
import models.DAO.ProformaDAO; 
import models.DAO.ProductoDAO; 
import models.DAO.ServicioDAO; 
import models.DetalleProforma;
import models.Producto; 
import models.Servicio; 

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;
import java.net.URL; // Aunque ya no la usemos para Image.getInstance, la dejaremos

@jakarta.servlet.annotation.WebServlet(name = "GenerarPDF", urlPatterns = {"/GenerarPDF"})
public class GenerarPDF extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(GenerarPDF.class.getName());

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ServicioDAO servicioDAO = new ServicioDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idProformaStr = request.getParameter("id");
        String estado = request.getParameter("estado"); 

        if (idProformaStr == null || idProformaStr.trim().isEmpty() || estado == null || estado.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetros de Proforma inválidos o faltantes.");
            return;
        }

        ProformaDAO proformaDAO = new ProformaDAO();
        Proforma proforma;

        try {
            proforma = proformaDAO.obtenerProformaConDetalles(idProformaStr);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de BD al obtener Proforma: " + idProformaStr, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error de base de datos al buscar proforma.");
            return;
        }

        if (proforma == null || proforma.getDetalles() == null || proforma.getDetalles().isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Proforma no encontrada o sin items.");
            return;
        }

        BigDecimal totalProforma = proforma.getTotal();
        BigDecimal montoAPagar;
        String tituloDocumento;

        if ("PAGADA_PARCIAL".equals(estado)) {
            montoAPagar = totalProforma.divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
            tituloDocumento = "COMPROBANTE DE PAGO - PRIMER ABONO (50%)";
        } else if ("PAGADA_TOTAL".equals(estado)) {
            montoAPagar = totalProforma;
            tituloDocumento = "COMPROBANTE DE PAGO FINAL (100% TOTAL)";
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Estado no permitido para generación de PDF de pago.");
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"Comprobante_Proforma_" + idProformaStr + "_" + estado + ".pdf\"");

        try (OutputStream os = response.getOutputStream()) {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, os);
            document.open();
            
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.RED);
            Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fontDetalle = new Font(Font.FontFamily.HELVETICA, 10);
            Font fontLink = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.BLUE);

            Paragraph titulo = new Paragraph(tituloDocumento, fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(Chunk.NEWLINE);

            // --- CABECERA DE LA PROFORMA (Se mantiene) ---
            Paragraph pHeader = new Paragraph();
            pHeader.add(new Chunk("Proforma Nro: ", fontSubtitulo));
            pHeader.add(new Chunk(proforma.getIdProforma() + "\n", fontDetalle));
            pHeader.add(new Chunk("Fecha: ", fontSubtitulo));
            pHeader.add(new Chunk(proforma.getFechaProforma().toString() + "\n", fontDetalle));
            pHeader.add(new Chunk("Cliente DNI/RUC: ", fontSubtitulo));
            pHeader.add(new Chunk(proforma.getDniCliente() + "\n", fontDetalle));
            pHeader.add(new Chunk("Estado Actual de la Proforma: ", fontSubtitulo));
            pHeader.add(new Chunk(proforma.getEstado() + "\n", fontDetalle));
            document.add(pHeader);
            document.add(Chunk.NEWLINE);

            // --- TABLA DE ITEMS ---
            document.add(new Paragraph("Detalle de Items (Base de Cálculo):", fontSubtitulo));
            
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            float[] columnWidths = {0.8f, 3.5f, 0.8f, 1.4f, 1.5f};
            table.setWidths(columnWidths);

            table.addCell(getHeaderCell("Tipo", fontSubtitulo));
            table.addCell(getHeaderCell("Descripción / Imagen", fontSubtitulo));
            table.addCell(getHeaderCell("Cant.", fontSubtitulo));
            table.addCell(getHeaderCell("P. Unit. (S/)", fontSubtitulo));
            table.addCell(getHeaderCell("Subtotal Base (S/)", fontSubtitulo));

            // Obtener la ruta base del servlet fuera del bucle para eficiencia
            String rutaBaseAplicacion = getServletContext().getRealPath("");

            // Filas de Items
            for (DetalleProforma detalle : proforma.getDetalles()) {
                
                String tipoStr;
                String descripcionItem; 
                String imagenUrl = null; 

                try {
                    if (detalle.getIdProducto() != null && detalle.getIdProducto() != 0) {
                        tipoStr = "P";
                        Producto producto = productoDAO.obtenerProductoPorId(detalle.getIdProducto());
                        if (producto != null) {
                            descripcionItem = producto.getNombre();
                            imagenUrl = producto.getImagen_url();
                        } else {
                            descripcionItem = "Producto no encontrado (ID: " + detalle.getIdProducto() + ")";
                        }
                    } else if (detalle.getIdServicio() != null && detalle.getIdServicio() != 0) {
                        tipoStr = "S";
                        Servicio servicio = servicioDAO.obtenerServicioPorId(detalle.getIdServicio());
                        if (servicio != null) {
                            descripcionItem = servicio.getNombre();
                        } else {
                            descripcionItem = "Servicio no encontrado (ID: " + detalle.getIdServicio() + ")";
                        }
                    } else {
                        tipoStr = "ERR";
                        descripcionItem = "Ítem inválido (sin ID)";
                    }
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error de BD al obtener nombre/imagen: " + e.getMessage());
                    tipoStr = "ERR";
                    descripcionItem = "Error de BD";
                }
                
                BigDecimal precioUnitario = detalle.getPrecioVenta();
                BigDecimal subtotalBase = precioUnitario.multiply(new BigDecimal(detalle.getCantidad())).setScale(2, RoundingMode.HALF_UP);

                // --- Creación de la Celda de Descripción (¡CORREGIDO PARA RUTA LOCAL!) ---
                PdfPCell cellDescripcion = new PdfPCell();
                cellDescripcion.setPadding(5);
                
                cellDescripcion.addElement(new Paragraph(descripcionItem, fontDetalle));
                
                if (imagenUrl != null && !imagenUrl.trim().isEmpty() && tipoStr.equals("P")) {
                    
                    // Asegurar que la ruta comience sin un '/' si el URL ya lo tiene (ej: "/uploads/img.jpg")
                    // y que use File.separator (aunque iText suele manejar el '/' en Windows/Linux)
                    String pathRelativo = imagenUrl.startsWith("/") ? imagenUrl.substring(1) : imagenUrl;
                    String rutaCompletaArchivo = rutaBaseAplicacion + "/" + pathRelativo;
                    
                    try {
                        // ** CORRECCIÓN: Usar la ruta de archivo local directamente **
                        Image img = Image.getInstance(rutaCompletaArchivo);
                        
                        img.scaleAbsolute(50f, 50f); 
                        img.setAlignment(Element.ALIGN_CENTER);
                        cellDescripcion.addElement(img);
                        cellDescripcion.addElement(new Paragraph("Imagen de Producto adjunta", fontLink));
                    } catch (BadElementException | IOException e) {
                        LOGGER.log(Level.WARNING, "Error al cargar la imagen local desde: " + rutaCompletaArchivo, e);
                        cellDescripcion.addElement(new Paragraph("❌ Imagen no disponible/inválida. Ruta: " + pathRelativo, fontLink));
                    }
                }
                // -----------------------------------------------------------------

                table.addCell(getDataCell(tipoStr, fontDetalle, Element.ALIGN_CENTER));
                table.addCell(cellDescripcion);
                table.addCell(getDataCell(String.valueOf(detalle.getCantidad()), fontDetalle, Element.ALIGN_CENTER));
                table.addCell(getDataCell(String.format("%,.2f", precioUnitario), fontDetalle, Element.ALIGN_RIGHT));
                table.addCell(getDataCell(String.format("%,.2f", subtotalBase), fontDetalle, Element.ALIGN_RIGHT));
            }
            document.add(table);
            document.add(Chunk.NEWLINE);
            
            // --- RESUMEN DE PAGOS Y MONTO ACTUAL (Se mantiene) ---
            document.add(new LineSeparator());
            document.add(Chunk.NEWLINE);
            
            Paragraph totalGeneral = new Paragraph("MONTO TOTAL DE LA PROFORMA (CON IGV 18%): S/ " + String.format("%,.2f", totalProforma), fontSubtitulo);
            totalGeneral.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalGeneral);
            
            Paragraph totalPago = new Paragraph(
                    "MONTO DE ESTE COMPROBANTE:", 
                    new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK)
            );
            totalPago.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalPago);

            Paragraph monto = new Paragraph("S/ " + String.format("%,.2f", montoAPagar), fontTitulo);
            monto.setAlignment(Element.ALIGN_RIGHT);
            document.add(monto);
            
            if ("PAGADA_PARCIAL".equals(estado)) {
                 Paragraph advertencia = new Paragraph("Este monto representa el 50% del total. El 50% restante deberá ser pagado para completar el proyecto.", fontDetalle);
                 advertencia.setAlignment(Element.ALIGN_CENTER);
                 document.add(Chunk.NEWLINE);
                 document.add(advertencia);
            }

            document.close();
        } catch (DocumentException e) {
            LOGGER.log(Level.SEVERE, "Error al generar el PDF iText", e);
            throw new ServletException("Error al generar el PDF", e);
        }
    }
    
    // Métodos auxiliares se mantienen...
    private PdfPCell getHeaderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        return cell;
    }
    
    private PdfPCell getDataCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5);
        return cell;
    }
}