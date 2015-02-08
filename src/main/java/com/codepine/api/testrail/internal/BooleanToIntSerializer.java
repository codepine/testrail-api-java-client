package com.codepine.api.testrail.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Serializer to convert (boolean) false/true to (int) 0/1.
 */
public class BooleanToIntSerializer extends JsonSerializer<Boolean> {


    @Override
    public void serialize(final Boolean value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeNumber(value == null ? 0 : value ? 1 : 0);
    }
}
