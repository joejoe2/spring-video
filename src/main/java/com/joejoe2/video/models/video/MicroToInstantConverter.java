package com.joejoe2.video.models.video;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class MicroToInstantConverter extends StdConverter<Long, Instant> {
  public Instant convert(final Long value) {
    return Instant.ofEpochSecond(
        TimeUnit.MICROSECONDS.toSeconds(value),
        TimeUnit.MICROSECONDS.toNanos(Math.floorMod(value, TimeUnit.SECONDS.toMicros(1))));
  }
}
