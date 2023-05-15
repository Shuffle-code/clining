package ru.shuffle.clining.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
    @Bean
    OpenAPI customOpenApi() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(info());
        return openAPI;
    }
    @Bean
    Info info() {
        return new Info().title("Smart janitor")
                .description("Автоматизация поиска работников для нужд ЖКХ")
                .version("1.0")
                .license(licence())
                .contact(contact());
    }
    @Bean
    License licence() {
        return new License()
                .name("Unlicense")
                .url("https://unlicense.org");
    }
    @Bean
    Contact contact() {
        return new Contact().email("shuffle2149@gmail.com").name("Евгений Малюгин");
    }
}