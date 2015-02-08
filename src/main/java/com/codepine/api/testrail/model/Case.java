package com.cymbocha.apis.testrail.model;

import com.cymbocha.apis.testrail.TestRail;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * TestRail case.
 */
@Data
public class Case {

    private static final String CUSTOM_FIELD_KEY_PREFIX = "custom_";

    private int id;

    @JsonView({TestRail.Cases.Add.class, TestRail.Cases.Update.class})
    private String title;

    private int sectionId;

    @JsonView({TestRail.Cases.Add.class, TestRail.Cases.Update.class})
    private int typeId;

    @JsonView({TestRail.Cases.Add.class, TestRail.Cases.Update.class})
    private int priorityId;

    @JsonView({TestRail.Cases.Add.class, TestRail.Cases.Update.class})
    private Integer milestoneId;

    @JsonView({TestRail.Cases.Add.class, TestRail.Cases.Update.class})
    private String refs;

    private int createdBy;

    private Date createdOn;

    private int updatedBy;

    private Date updatedOn;

    @JsonView({TestRail.Cases.Add.class, TestRail.Cases.Update.class})
    private String estimate;

    private String estimateForecast;

    private int suiteId;

    @JsonView({TestRail.Cases.Add.class, TestRail.Cases.Update.class})
    @Getter(onMethod = @_({@JsonAnyGetter, @JsonSerialize(keyUsing = CustomFieldSerializer.class)}))
    @JsonIgnore
    private Map<String, Object> customFields;

    @JsonAnySetter
    public Case addCustomField(String key, Object value) {
        if (customFields == null) {
            customFields = new HashMap<>();
        }
        customFields.put(key.replaceFirst(CUSTOM_FIELD_KEY_PREFIX, ""), value);
        return this;
    }

    /**
     * Serializer for custom fields.
     */
    private static class CustomFieldSerializer extends StdKeySerializer {

        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            super.serialize(CUSTOM_FIELD_KEY_PREFIX + o, jsonGenerator, serializerProvider);
        }
    }

}
