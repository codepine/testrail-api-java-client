package com.codepine.api.testrail.internal;

import com.codepine.api.testrail.model.Field;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

/**
 * Jackson module for {@link com.codepine.api.testrail.model.Field}.
 * <p/>
 * INTERNAL ONLY
 */
public class FieldModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext setupContext) {
        setupContext.addBeanDeserializerModifier(new FieldDeserializerModifier());
        super.setupModule(setupContext);
    }

    private static class FieldDeserializer extends StdDeserializer<Field> implements ResolvableDeserializer {
        private final JsonDeserializer<?> defaultDeserializer;

        FieldDeserializer(JsonDeserializer<?> defaultDeserializer) {
            super(Field.class);
            this.defaultDeserializer = defaultDeserializer;
        }

        @Override
        public Field deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            Field field = (Field) defaultDeserializer.deserialize(jsonParser, deserializationContext);
            ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
            // set type
            field.setType(Field.Type.getType(field.getTypeId()));
            for (Field.Config config : field.getConfigs()) {
                // update options to correct type implementations
                config.setOptions(mapper.convertValue(config.getOptions(), field.getType().getOptionsClass()));
            }
            return field;
        }

        @Override
        public void resolve(DeserializationContext deserializationContext) throws JsonMappingException {
            ((ResolvableDeserializer) defaultDeserializer).resolve(deserializationContext);
        }
    }

    private static class FieldDeserializerModifier extends BeanDeserializerModifier {

        @Override
        public JsonDeserializer<?> modifyDeserializer(DeserializationConfig deserializationConfig, BeanDescription beanDescription, JsonDeserializer<?> jsonDeserializer) {
            if (Field.class.isAssignableFrom(beanDescription.getBeanClass())) {
                return new FieldDeserializer(jsonDeserializer);
            }
            return jsonDeserializer;
        }

    }
}
