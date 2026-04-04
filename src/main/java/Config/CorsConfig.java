package Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de CORS (Cross-Origin Resource Sharing)
 * Permite que el frontend React se comunique con el backend Spring Boot
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Permitir origen del frontend React (desarrollo)
                .allowedOrigins(
                    "http://localhost:3000",      // React dev server
                    "http://127.0.0.1:3000"       // Alternativa localhost
                    // Agrega aquí la URL de producción cuando despliegues
                    // "https://tu-dominio.com"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // Cache de preflight request por 1 hora
    }
}
