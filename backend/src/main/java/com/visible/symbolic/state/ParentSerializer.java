package com.visible.symbolic.state;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ParentSerializer extends JsonSerializer<State> {
    @Override
    public void serialize(State value,
                          JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.getId());
    }
}