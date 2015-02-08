package com.cymbocha.apis.testrail.model;

import com.cymbocha.apis.testrail.TestRail;
import com.cymbocha.apis.testrail.internal.CsvToListDeserializer;
import com.cymbocha.apis.testrail.internal.ListToCsvSerializer;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import com.google.common.base.Preconditions;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.util.*;

/**
 * TestRail result.
 */
@Data
@ToString(exclude = "caseId")
public class Result {

    private static final String CUSTOM_FIELD_KEY_PREFIX = "custom_";

    private int id;

    private int testId;

    @JsonView({TestRail.Results.AddListForCases.class})
    private Integer caseId;

    @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
    private int statusId;

    private Date createdOn;

    private int createdBy;

    @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
    private Integer assignedtoId;

    @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
    private String comment;

    @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
    private String version;

    @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
    private String elapsed;

    @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
    @JsonSerialize(using = ListToCsvSerializer.class)
    @JsonDeserialize(using = CsvToListDeserializer.class)
    private java.util.List<String> defects;

    /**
     * Add a defect.
     *
     * @param defect defect to be added
     * @return this instance for chaining
     */
    public Result addDefect(@NonNull String defect) {
        Preconditions.checkArgument(!defect.isEmpty(), "defect cannot be empty");
        java.util.List<String> defects = getDefects();
        if(defects == null) {
            defects = new ArrayList<>();
        }
        defects.add(defect);
        setDefects(defects);
        return this;
    }

    // customstepresults

    @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
    @Getter(onMethod = @_({@JsonAnyGetter, @JsonSerialize(keyUsing = CustomFieldSerializer.class)}))
    @JsonIgnore
    private Map<String, Object> customFields;

    @JsonAnySetter
    public Result addCustomField(String key, Object value) {
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

    /**
     * Wrapper for list of {@code Result}s for internal use.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class List {

        @JsonView({TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
        private java.util.List<Result> results;

    }
}
