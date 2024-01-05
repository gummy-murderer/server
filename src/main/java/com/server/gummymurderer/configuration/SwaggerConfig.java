package com.server.gummymurderer.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Gummy Murderer App",
                description = "gummy murderer app api명세",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

}