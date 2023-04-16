package com.joejoe2.video.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import java.io.IOException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@Configuration
public class StringTrimConfig {

  @ControllerAdvice
  public static class ControllerStringParamTrimConfig {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
      StringTrimmerEditor propertyEditor = new StringTrimmerEditor(false);
      binder.registerCustomEditor(String.class, propertyEditor);
    }
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return new Jackson2ObjectMapperBuilderCustomizer() {
      @Override
      public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        jacksonObjectMapperBuilder.deserializerByType(
            String.class,
            new StdScalarDeserializer<String>(String.class) {
              @Override
              public String deserialize(JsonParser jsonParser, DeserializationContext ctx)
                  throws IOException {
                return StringUtils.trimWhitespace(jsonParser.getValueAsString());
              }
            });
      }
    };
  }
}
