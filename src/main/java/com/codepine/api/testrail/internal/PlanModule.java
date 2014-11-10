package com.cymbocha.apis.testrail.internal;

import com.cymbocha.apis.testrail.model.Plan;
import com.cymbocha.apis.testrail.model.Run;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

/**
 * Jackson module for {@link com.cymbocha.apis.testrail.model.Plan} to set some additional properties on {@link com.cymbocha.apis.testrail.model.Plan.Entry.Run}.
 * <p/>
 * INTERNAL ONLY
 */
public class PlanModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext setupContext) {
        setupContext.addBeanDeserializerModifier(new PlanDeserializerModifier());
        super.setupModule(setupContext);
    }

    private static class PlanDeserializer extends StdDeserializer<Plan> implements ResolvableDeserializer {

        private final JsonDeserializer<?> defaultDeserializer;

        PlanDeserializer(JsonDeserializer<?> defaultDeserializer) {
            super(Plan.class);
            this.defaultDeserializer = defaultDeserializer;
        }

        @Override
        public Plan deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            Plan plan = (Plan) defaultDeserializer.deserialize(jsonParser, deserializationContext);
            ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();

            if (plan.getEntries() != null) {
                for (Plan.Entry entry : plan.getEntries()) {
                    if (entry.getRuns() != null) {
                        for (Run run : entry.getRuns()) {
                            run.setCreatedOn(plan.getCreatedOn());
                            run.setCreatedBy(plan.getCreatedBy());
                        }
                    }
                }
            }

            return plan;
        }

        @Override
        public void resolve(DeserializationContext deserializationContext) throws JsonMappingException {
            ((ResolvableDeserializer) defaultDeserializer).resolve(deserializationContext);
        }
    }

    private static class PlanDeserializerModifier extends BeanDeserializerModifier {

        @Override
        public JsonDeserializer<?> modifyDeserializer(DeserializationConfig deserializationConfig, BeanDescription beanDescription, JsonDeserializer<?> jsonDeserializer) {
            if (Plan.class.isAssignableFrom(beanDescription.getBeanClass())) {
                return new PlanDeserializer(jsonDeserializer);
            }
            return jsonDeserializer;
        }

    }
}
