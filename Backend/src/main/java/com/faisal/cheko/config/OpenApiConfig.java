package com.faisal.cheko.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cheko Restaurant API")
                        .description("REST API for Cheko Restaurant Management System")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Faisal")
                                .email("contact@example.com")
                                .url("https://example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }

    /**
     * Customizer that dynamically adds the current request's server URL.
     * So if you're accessing from localhost, staging, or production — it auto updates.
     */
    @Bean
    public OpenApiCustomizer dynamicServerCustomizer() {
        return openApi -> openApi.setServers(null); // Removes static servers — the UI will infer dynamically
    }
    
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/**")
                .build();
    }
}
