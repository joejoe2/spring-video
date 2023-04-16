package com.joejoe2.video.service.video;

import com.joejoe2.video.data.video.VideoProfile;
import com.joejoe2.video.exception.DoesNotExist;
import com.joejoe2.video.models.video.EventResult;
import java.util.List;
import java.util.UUID;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public interface VideoService {
  public VideoProfile createFromObjectStorage(UUID userId, String title, String objectName);

  StreamingResponseBody m3u8Index(UUID videoId) throws DoesNotExist;

  StreamingResponseBody ts(UUID videoId, String ts) throws DoesNotExist;

  List<VideoProfile> list();

  VideoProfile profile(UUID videoId) throws DoesNotExist;

  void onVideoEventComplete(EventResult eventResult) throws Exception;
}
