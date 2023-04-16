package com.joejoe2.video.data.video;

import com.joejoe2.video.validation.UUID;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoRequest {
  @Parameter(in = ParameterIn.PATH)
  @UUID(message = "invalid video id !")
  String id;
}
