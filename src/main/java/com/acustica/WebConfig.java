package com.acustica; 

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 1. Configura la ubicación de recursos estáticos
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapea la carpeta '/assets/' a la carpeta física 'Web Pages/assets/'
        // También mapea la carpeta '/Imagenes/' a 'Web Pages/Imagenes/'
        registry.addResourceHandler("/assets/**", "/Imagenes/**")
                .addResourceLocations("/Web Pages/assets/", "/Web Pages/Imagenes/");
    }

    // 2. Opcional: Configura la resolución de vistas (aunque ya lo tienes en properties)
    // Es mejor dejarlo en application.properties para este caso.
}