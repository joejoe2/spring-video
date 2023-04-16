package com.joejoe2.video.validation;

import com.joejoe2.video.exception.ValidationError;

public interface Validator<I, O> {
  public abstract O validate(I data) throws ValidationError;
}
