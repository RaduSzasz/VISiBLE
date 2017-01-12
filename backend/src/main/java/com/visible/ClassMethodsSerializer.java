package com.visible;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

final class ClassMethodsSerializer extends JsonSerializer<ClassMethods> {

    @Override
    public void serialize(ClassMethods value, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {

        gen.writeStartObject();

        gen.writeFieldName("jar");
        gen.writeString(value.getJarName());

        gen.writeFieldName("data");
        gen.writeStartArray();
        for (String className : value.getClasses().keySet()) {
            gen.writeStartObject();
            gen.writeFieldName("class");
            gen.writeString(className);
            gen.writeFieldName("methods");
            gen.writeObject(value.getClasses().get(className));
            gen.writeEndObject();
        }
        gen.writeEndArray();

        if (value.getErrorMsg() != null) {
            gen.writeFieldName("error");
            gen.writeString(value.getErrorMsg());
        }

        gen.writeEndObject();
    }
}
