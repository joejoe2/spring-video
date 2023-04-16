package com.joejoe2.video.validation;

import static org.junit.jupiter.api.Assertions.*;

import com.joejoe2.video.exception.ValidationError;
import org.junit.jupiter.api.Test;

class VideoFileNameValidatorTest {
  VideoFileNameValidator videoFileNameValidator = VideoFileNameValidator.getInstance();

  @Test
  void testPath() {
    assertEquals("f1.mp4", videoFileNameValidator.validate("f1.mp4"));
    assertEquals("-f_1.mp4", videoFileNameValidator.validate("-f_1.mp4"));
  }

  @Test
  void testInvalidPath() {
    assertThrows(ValidationError.class, () -> videoFileNameValidator.validate(null));
    assertThrows(ValidationError.class, () -> videoFileNameValidator.validate(""));
    assertThrows(ValidationError.class, () -> videoFileNameValidator.validate(" "));
    assertThrows(ValidationError.class, () -> videoFileNameValidator.validate("\n"));
    assertThrows(ValidationError.class, () -> videoFileNameValidator.validate("\t"));
    assertThrows(ValidationError.class, () -> videoFileNameValidator.validate("mp4"));
    assertThrows(ValidationError.class, () -> videoFileNameValidator.validate(".mp4"));
    assertThrows(ValidationError.class, () -> videoFileNameValidator.validate("f 1.mp4"));
    assertThrows(ValidationError.class, () -> videoFileNameValidator.validate("f\n1.mp4"));
    assertThrows(ValidationError.class, () -> videoFileNameValidator.validate("f\t1.mp4"));
    assertThrows(ValidationError.class, () -> videoFileNameValidator.validate("/f1.mp4"));
    assertThrows(ValidationError.class, () -> videoFileNameValidator.validate("//"));
    assertThrows(ValidationError.class, () -> videoFileNameValidator.validate("f1/f2.mp4"));
    assertThrows(ValidationError.class, () -> videoFileNameValidator.validate("../f1.mp4"));
    assertThrows(ValidationError.class, () -> videoFileNameValidator.validate("./."));
    assertThrows(ValidationError.class, () -> videoFileNameValidator.validate("./f1.mp4"));
  }
}
