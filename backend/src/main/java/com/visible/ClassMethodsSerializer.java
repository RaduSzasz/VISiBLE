package com.visible;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.visible.ClassMethods;

import java.io.IOException;

class ClassMethodsSerializer extends JsonSerializer<ClassMethods> {

    @Override
    public void serialize(ClassMethods value, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        for (String className : value.getClasses().keySet()) {
            gen.writeFieldName("class");
            gen.writeString(className);
            gen.writeFieldName("methods");
            gen.writeObject(value.getClasses().get(className));
        }
        gen.writeEndObject();
    }
}
