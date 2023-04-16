package com.joejoe2.video.data.video;

import com.joejoe2.video.validation.VideoFileName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRequest {
  @VideoFileName String fileName;

  @NotBlank
  @Size(min = 1, max = 128)
  String title;
}
