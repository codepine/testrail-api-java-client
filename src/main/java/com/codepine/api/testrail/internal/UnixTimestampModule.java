package com.cymbocha.apis.testrail.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.Date;

/**
 * Jackson module to register serializers and deserializers for unix time stamp in seconds.
 * <p/>
 * INTERNAL ONLY
 */
public class UnixTimestampModule extends SimpleModule {

    public UnixTimestampModule() {
        addSerializer(Date.class, new UnixTimestampSerializer());
        addDeserializer(Date.class, new UnixTimestampDeserializer());
    }

    /**
     * Serializer to convert {@code java.util.Date} to unit timestamps in seconds.
     */
    private static class UnixTimestampSerializer extends JsonSerializer<Date> {

        @Override
        public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeNumber(value.getTime() / 1000);
        }
    }

    /**
     * Deserializer to convert unit timestamps in seconds to {@code java.util.Date}.
     */
    private static class UnixTimestampDeserializer extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String timestamp = jp.getText().trim();
            return new Date(Long.valueOf(timestamp + "000"));
        }
    }
}
