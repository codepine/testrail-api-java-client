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
import java.util.HashMap;
import java.util.Map;

/**
 * TestRail test.
 */
@Data
public class Test {

    private static final String CUSTOM_FIELD_KEY_PREFIX = "custom_";

    private int id;

    private int caseId;

    private Integer assignedtoId;

    private String title;

    private int statusId;

    private int typeId;

    private int priorityId;

    private Integer milestoneId;

    private Integer runId;

    private String refs;

    private String estimate;

    private String estimateForecast;

    private Map<String, Object> customFields;

    @JsonAnySetter
    public Test addCustomField(String key, Object value) {
        if (customFields == null) {
            customFields = new HashMap<>();
        }
        customFields.put(key.replaceFirst(CUSTOM_FIELD_KEY_PREFIX, ""), value);
        return this;
    }

}
