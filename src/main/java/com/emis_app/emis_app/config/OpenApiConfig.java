package com.emis_app.emis_app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Server configuration
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Development server");

        // Contact information
        Contact contact = new Contact();
        contact.setEmail("admin@emis.co.ug");
        contact.setName("EMIS Development Team");
        contact.setUrl("https://emis.co.ug");

        // API Information
        Info info = new Info()
                .title("Education Management Information System API")
                .version("1.0.0")
                .contact(contact)
                .description("""
                    Comprehensive API for managing schools and learners in an education system.
                    
                    Features:
                    - Complete CRUD operations for Schools and Learners
                    - Advanced search capabilities with filtering and pagination
                    - Proper relationship management between entities
                    - Validation and error handling
                    - RESTful design following Spring Boot 3.5.x standards
                    """)
                .termsOfService("https://emis.co.ug/terms");

        // Security scheme (optional - for future use)
        Components components = new Components()
                .addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"));

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer))
                .components(components);
    }
}