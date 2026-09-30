package ru.arman.taskflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskflowOpenApi(){
        return  new OpenAPI()
                .info(new Info()
                        .title("TaskFlow API")
                        .description("REST API для управления задачами и пользователями")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Arman")
                                .email("arman@example.com")));
    }
}
