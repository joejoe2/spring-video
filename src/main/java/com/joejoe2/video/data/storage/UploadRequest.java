package com.joejoe2.video.data.storage;

import com.joejoe2.video.validation.VideoFileName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadRequest {
  @VideoFileName String fileName;

  MultipartFile file;
}
