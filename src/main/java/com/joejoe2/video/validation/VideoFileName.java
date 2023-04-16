package com.joejoe2.video.validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Target(ElementType.FIELD)
@Constraint(validatedBy = {VideoFileNameValidator.class})
@NotBlank(message = "filename of the video cannot be empty !")
@Size(min = 1, max = 512)
@Retention(RUNTIME)
public @interface VideoFileName {
  String message() default "invalid filename of the video !";
}
