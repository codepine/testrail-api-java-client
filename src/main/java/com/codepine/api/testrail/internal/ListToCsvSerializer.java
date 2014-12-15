package com.cymbocha.apis.testrail.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.base.Joiner;

import java.io.IOException;
import java.util.List;

/**
 * Serializer to convert {@code List<String>} to csv string.
 */
public class ListToCsvSerializer extends JsonSerializer<List<String>> {

    @Override
    public void serialize(List<String> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeString(Joiner.on(',').join(value));
    }
}
