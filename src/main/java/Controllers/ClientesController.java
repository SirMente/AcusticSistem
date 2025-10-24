package controllers; // Usa el paquete base de tu aplicación (ej. com.acustica.controllers)

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model; // Opcional, solo si necesitas pasar datos a la vista

// La anotación @Controller marca esta clase como un componente de Spring MVC
@Controller 
public class ClientesController {

    // @GetMapping mapea la solicitud HTTP GET a la URL "/clientes"
    @GetMapping("/clientes") 
    public String listarClientes(Model model) {
        
        // El objeto Model permite pasar datos del Controller a la vista (JSP)
        // 1. (Opcional) Aquí iría la lógica para obtener datos de clientes:
        // List<Cliente> listaClientes = clienteService.obtenerTodos();
        // model.addAttribute("clientes", listaClientes);
        
        // 2. Retornamos el nombre lógico de la vista. 
        // Spring Boot, usando tu configuración en application.properties, 
        // buscará: /WEB-INF/views/gestionclientes.jsp
        return "gestionclientes"; 
    }
    
    /*
    // Si tu formulario en gestionclientes.jsp enviara datos por POST, 
    // lo manejarías con un método @PostMapping:
    
    @PostMapping("/clientes")
    public String guardarCliente(@RequestParam String nombre, ...) {
        // Lógica para guardar el nuevo cliente
        // ...
        return "redirect:/clientes"; // Redirige de vuelta a la lista para evitar el doble envío de formulario
    }
    */
}