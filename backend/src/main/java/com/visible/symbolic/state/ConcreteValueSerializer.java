package com.visible.symbolic.state;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

public class ConcreteValueSerializer extends JsonSerializer<Map<String, Integer>> {

    @Override
    public void serialize(Map<String, Integer> value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeString("Test");
    }
}
