package com.joejoe2.worker.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.joejoe2.worker.model.VideoEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonDeserialize(using = EventDtoDeserializer.class)
public class EventDto {
  VideoEvent event;
}
