package testeMaven.com.techmuu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


public class TechMuuApplication {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║     🐄 TECHMUU - INICIANDO API 🐄     ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        SpringApplication.run(TechMuuApplication.class, args);
        
        System.out.println("\n✓ API REST iniciada com sucesso!");
        System.out.println("✓ Acesse: http://localhost:8080");
        System.out.println("✓ Documentação: http://localhost:8080/api/docs\n");
    }

    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000", "http://127.0.0.1:5500", "*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
    }
}