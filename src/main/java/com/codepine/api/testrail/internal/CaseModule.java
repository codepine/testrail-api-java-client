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

import com.codepine.api.testrail.model.Case;
import com.codepine.api.testrail.model.CaseField;
import com.codepine.api.testrail.model.Field;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.base.Function;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Jackson module for {@link com.codepine.api.testrail.model.Case}.
 * <p/>
 * INTERNAL ONLY
 */
public class CaseModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext setupContext) {
        setupContext.addBeanDeserializerModifier(new CaseDeserializerModifier());
        super.setupModule(setupContext);
    }

    private static class CaseDeserializer extends StdDeserializer<Case> implements ResolvableDeserializer {
        private final JsonDeserializer<?> defaultDeserializer;

        CaseDeserializer(JsonDeserializer<?> defaultDeserializer) {
            super(Case.class);
            this.defaultDeserializer = defaultDeserializer;
        }

        @Override
        public Case deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            Case testCase = (Case) defaultDeserializer.deserialize(jsonParser, deserializationContext);

            ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
            List<CaseField> caseFieldList = (List<CaseField>) deserializationContext.findInjectableValue(Case.class.toString(), null, null);
            Map<String, CaseField> caseFields = Maps.uniqueIndex(caseFieldList, new Function<CaseField, String>() {
                @Override
                public String apply(final CaseField caseField) {
                    return caseField.getName();
                }
            });
            Map<String, Object> customFields = new HashMap<>(testCase.getCustomFields().size());
            for (Map.Entry<String, Object> customField : testCase.getCustomFields().entrySet()) {
                checkArgument(caseFields.containsKey(customField.getKey()), "Case field list configuration is possibly outdated since it does not contain custom field: " + customField.getKey());
                customFields.put(customField.getKey(), mapper.convertValue(customField.getValue(), Field.Type.getType(caseFields.get(customField.getKey()).getTypeId()).getTypeReference()));
            }
            testCase.setCustomFields(customFields);
            return testCase;
        }

        @Override
        public void resolve(DeserializationContext deserializationContext) throws JsonMappingException {
            ((ResolvableDeserializer) defaultDeserializer).resolve(deserializationContext);
        }
    }

    private static class CaseDeserializerModifier extends BeanDeserializerModifier {

        @Override
        public JsonDeserializer<?> modifyDeserializer(DeserializationConfig deserializationConfig, BeanDescription beanDescription, JsonDeserializer<?> jsonDeserializer) {
            if (Case.class.isAssignableFrom(beanDescription.getBeanClass())) {
                return new CaseDeserializer(jsonDeserializer);
            }
            return jsonDeserializer;
        }

    }
}
