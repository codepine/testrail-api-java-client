package com.codepine.api.testrail.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Deserializer to convert (int) 0/1 to (boolean) false/true.
 */
public class IntToBooleanDeserializer extends JsonDeserializer<Boolean> {

    @Override
    public Boolean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return jp.getValueAsInt(0) == 0 ? Boolean.FALSE : Boolean.TRUE;
    }
}
