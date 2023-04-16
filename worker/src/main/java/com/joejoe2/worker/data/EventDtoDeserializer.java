package com.joejoe2.worker.data;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.joejoe2.worker.model.VideoEvent;
import java.io.IOException;

public class EventDtoDeserializer extends StdDeserializer {
  private static final ObjectMapper mapper = new ObjectMapper();

  public EventDtoDeserializer() {
    super(EventDtoDeserializer.class);
  }

  protected EventDtoDeserializer(Class vc) {
    super(vc);
  }

  @Override
  public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException, JacksonException {
    EventDto eventDto = new EventDto();
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    eventDto.setEvent(mapper.treeToValue(node.get("after"), VideoEvent.class));
    return eventDto;
  }
}
