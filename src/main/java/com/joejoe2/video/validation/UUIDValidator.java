package com.joejoe2.video.validation;

import com.joejoe2.video.exception.ValidationError;
import java.util.UUID;

public class UUIDValidator implements Validator<String, UUID> {
  private static final UUIDValidator instance = new UUIDValidator();

  private UUIDValidator() {}

  public static UUIDValidator getInstance() {
    return instance;
  }

  @Override
  public UUID validate(String data) throws ValidationError {
    if (data == null) throw new ValidationError("uuid can not be null !");

    try {
      return UUID.fromString(data);
    } catch (Exception e) {
      throw new ValidationError(e.getMessage());
    }
  }
}
