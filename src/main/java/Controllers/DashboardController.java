// Archivo: DashboardController.java
package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String mostrarDashboard() {
        // Retorna el nombre de la vista (Spring buscará /WEB-INF/views/dashboard.jsp)
        return "dashboard"; 
    }
}