package com.ndvr.challenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class OpenApiConfig {

    @Value("${spring.application.version:unknown}")
    String version;

    @Bean
    public OpenAPI ndvrChallengeOpenAPI() {
        return new OpenAPI().info(new Info().title("Challenge Lite")
                        .description("Have fun coding!")
                        .version(version)
        );
    }

}
