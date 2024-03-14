package com.owl.payrit.global.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                    .components(new Components())
                    .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                    .title("PAYRIT API")
                    .description("PAYRIT REST API 문서입니다.")
                    .version("1.0.0");
    }
}
