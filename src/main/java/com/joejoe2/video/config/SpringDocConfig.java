package com.joejoe2.video.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "Spring Video API", version = "v0.0.1"))
@SecurityScheme(
    name = "jwt",
    scheme = "bearer",
    bearerFormat = "jwt",
    type = SecuritySchemeType.HTTP,
    in = SecuritySchemeIn.HEADER)
@Configuration
public class SpringDocConfig {}
