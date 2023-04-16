package com.joejoe2.video.data.storage;

import com.joejoe2.video.validation.VideoFileName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadRequest {
  @VideoFileName String fileName;
}
