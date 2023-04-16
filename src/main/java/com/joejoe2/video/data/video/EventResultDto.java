package com.joejoe2.video.data.video;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.joejoe2.video.models.video.EventResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonDeserialize(using = EventResultDtoDeserializer.class)
public class EventResultDto {
  EventResult result;
}
