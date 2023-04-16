package com.joejoe2.video.validation;

import com.joejoe2.video.exception.ValidationError;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VideoFileNameValidator
    implements Validator<String, String>, ConstraintValidator<VideoFileName, String> {
  public static final Pattern pathPattern = Pattern.compile("[^\\\\\\\\/:*?\\\"<>| \t\n]+.[mp4]$");

  @Override
  public String validate(String data) throws ValidationError {
    checkPath(data);
    return data;
  }

  private void checkPath(String path) throws ValidationError {
    if (path == null || path.isEmpty() || !pathPattern.matcher(path).matches()) {
      throw new ValidationError("filename of the video %s is not valid !".formatted(path));
    }
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    try {
      checkPath(value);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private static final VideoFileNameValidator instance = new VideoFileNameValidator();

  public static VideoFileNameValidator getInstance() {
    return instance;
  }

  private VideoFileNameValidator() {}
}
