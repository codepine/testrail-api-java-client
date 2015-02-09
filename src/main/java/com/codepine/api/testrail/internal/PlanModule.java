/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Kunal Shah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.codepine.api.testrail.internal;

import com.codepine.api.testrail.model.Plan;
import com.codepine.api.testrail.model.Run;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

/**
 * Jackson module for {@link com.codepine.api.testrail.model.Plan} to set some additional properties on {@link com.codepine.api.testrail.model.Plan.Entry.Run}.
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
