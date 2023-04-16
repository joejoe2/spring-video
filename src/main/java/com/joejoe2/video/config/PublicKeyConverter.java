package com.joejoe2.video.config;

import java.io.ByteArrayInputStream;
import java.security.PublicKey;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class PublicKeyConverter implements Converter<String, PublicKey> {
  @SneakyThrows
  @Override
  public PublicKey convert(String from) {
    return RsaKeyConverters.x509().convert(new ByteArrayInputStream(from.getBytes()));
  }
}
