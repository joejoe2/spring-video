package com.joejoe2.video.config;

import java.security.interfaces.RSAPublicKey;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class JwtConfig {
  @Value("${jwt.secret.publicKey}")
  private RSAPublicKey publicKey;
}
