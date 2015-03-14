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

package com.codepine.api.testrail.model;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.internal.IntToBooleanDeserializer;
import com.codepine.api.testrail.internal.StringToMapDeserializer;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TestRail field.
 */
@Data
public class Field {

    private int id;
    private String label;
    private String name;
    private String description;
    private String systemName;
    private int typeId;
    private Type type;
    private int displayOrder;
    private List<Config> configs;

    /**
     * TestRail type of field.
     * <p>
     * Map of TestRail field types to their corresponding Java types:
     * <pre>
     *      STRING -- java.lang.String
     *      INTEGER -- java.lang.Integer
     *      TEXT -- java.lang.String
     *      URL -- java.lang.String
     *      CHECKBOX -- java.lang.Boolean
     *      DROPDOWN -- java.lang.String
     *      USER -- java.lang.Integer
     *      DATE -- java.lang.String
     *      MILESTONE -- java.lang.Integer
     *      STEPS -- java.util.List<{@link Step}>
     *      STEP_RESULTS -- java.util.List<{@link StepResult}>
     *      MULTI_SELECT -- java.util.List<String>
     * </pre>
     * </p>
     */
    @RequiredArgsConstructor
    public static enum Type {
        UNKNOWN(Config.Options.class, new TypeReference<Object>() {
        }),
        STRING(Config.StringOptions.class, new TypeReference<String>() {
        }),
        INTEGER(Config.IntegerOptions.class, new TypeReference<Integer>() {
        }),
        TEXT(Config.TextOptions.class, new TypeReference<String>() {
        }),
        URL(Config.UrlOptions.class, new TypeReference<String>() {
        }),
        CHECKBOX(Config.CheckboxOptions.class, new TypeReference<Boolean>() {
        }),
        DROPDOWN(Config.DropdownOptions.class, new TypeReference<String>() {
        }),
        USER(Config.UserOptions.class, new TypeReference<Integer>() {
        }),
        DATE(Config.DateOptions.class, new TypeReference<String>() {
        }),
        MILESTONE(Config.MilestoneOptions.class, new TypeReference<Integer>() {
        }),
        STEPS(Config.StepsOptions.class, new TypeReference<List<Step>>() {
        }),
        STEP_RESULTS(Config.StepResultsOptions.class, new TypeReference<List<StepResult>>() {
        }),
        MULTI_SELECT(Config.MultiSelectOptions.class, new TypeReference<List<String>>() {
        });

        @Getter
        private final Class<? extends Config.Options> optionsClass;
        @Getter
        private final TypeReference<?> typeReference;

        public static Type getType(int typeId) {
            return typeId >= 0 && typeId < Type.values().length ? Type.values()[typeId] : UNKNOWN;
        }

    }

    /**
     * Configuration for a {@code Field}.
     */
    @Data
    public static class Config {

        private String id;
        private Context context;
        private Options options;

        /**
         * Options for a {@code Field} configuration.
         */
        @Data
        public static class Options {

            @JsonProperty
            @Getter(onMethod = @_({@JsonIgnore}))
            private boolean isRequired;
            @Getter(value = AccessLevel.PRIVATE, onMethod = @_({@JsonAnyGetter}))
            @Setter(value = AccessLevel.NONE)
            private Map<String, Object> unknownFields;

            @JsonAnySetter
            private Options addUnknownField(String key, Object value) {
                if (unknownFields == null) {
                    unknownFields = new HashMap<>();
                }
                unknownFields.put(key, value);
                return this;
            }

        }

        /**
         * Options for a {@code Field} of type {@link Type#STRING}.
         */
        @Data
        @EqualsAndHashCode(callSuper = true)
        @ToString(callSuper = true)
        public static class StringOptions extends Options {
            private String defaultValue;
        }

        /**
         * Options for a {@code Field} of type {@link Type#INTEGER}.
         */
        @Data
        @EqualsAndHashCode(callSuper = true)
        @ToString(callSuper = true)
        public static class IntegerOptions extends Options {
            private BigInteger defaultValue;
        }

        /**
         * Options for a {@code Field} of type {@link Type#TEXT}.
         */
        @Data
        @EqualsAndHashCode(callSuper = true)
        @ToString(callSuper = true)
        public static class TextOptions extends Options {
            private String defaultValue;
            private String format;
            private int rows;
        }

        /**
         * Options for a {@code Field} of type {@link Type#URL}.
         */
        @Data
        @EqualsAndHashCode(callSuper = true)
        @ToString(callSuper = true)
        public static class UrlOptions extends Options {
            private String defaultValue;
        }

        /**
         * Options for a {@code Field} of type {@link Type#CHECKBOX}.
         */
        @Data
        @EqualsAndHashCode(callSuper = true)
        @ToString(callSuper = true)
        public static class CheckboxOptions extends Options {
            @JsonDeserialize(using = IntToBooleanDeserializer.class)
            private boolean defaultValue;
        }

        /**
         * Options for a {@code Field} of type {@link Type#DROPDOWN}.
         */
        @Data
        @EqualsAndHashCode(callSuper = true)
        @ToString(callSuper = true)
        public static class DropdownOptions extends Options {
            private String defaultValue;
            @JsonDeserialize(using = StringToMapDeserializer.class)
            private Map<String, String> items;
        }

        /**
         * Options for a {@code Field} of type {@link Type#USER}.
         */
        @Data
        @EqualsAndHashCode(callSuper = true)
        @ToString(callSuper = true)
        public static class UserOptions extends Options {
            private int defaultValue;
        }

        /**
         * Options for a {@code Field} of type {@link Type#DATE}.
         */
        @Data
        @EqualsAndHashCode(callSuper = true)
        @ToString(callSuper = true)
        public static class DateOptions extends Options {
        }

        /**
         * Options for a {@code Field} of type {@link Type#MILESTONE}.
         */
        @Data
        @EqualsAndHashCode(callSuper = true)
        @ToString(callSuper = true)
        public static class MilestoneOptions extends Options {
        }

        /**
         * Options for a {@code Field} of type {@link Type#STEPS}.
         */
        @Data
        @EqualsAndHashCode(callSuper = true)
        @ToString(callSuper = true)
        public static class StepsOptions extends Options {
            private String format;
            private boolean hasExpected;
            private int rows;
        }

        /**
         * Options for a {@code Field} of type {@link Type#STEP_RESULTS}.
         */
        @Data
        @EqualsAndHashCode(callSuper = true)
        @ToString(callSuper = true)
        public static class StepResultsOptions extends Options {
            private String format;
            private boolean hasExpected;
            private boolean hasActual;
        }

        /**
         * Options for a {@code Field} of type {@link Type#MULTI_SELECT}.
         */
        @Data
        @EqualsAndHashCode(callSuper = true)
        @ToString(callSuper = true)
        public static class MultiSelectOptions extends Options {
            @JsonDeserialize(using = StringToMapDeserializer.class)
            private Map<String, String> items;
        }


        @Data
        public static class Context {
            @JsonProperty
            @Getter(onMethod = @_({@JsonIgnore}))
            private boolean isGlobal;
            private List<Integer> projectIds;
        }

    }

    /**
     * Step; a custom field type.
     */
    @Data
    public static class Step {

        @JsonView({TestRail.Cases.Add.class, TestRail.Cases.Update.class})
        private String content;
        @JsonView({TestRail.Cases.Add.class, TestRail.Cases.Update.class})
        private String expected;

    }

    /**
     * Step result; a custom field type.
     */
    @Data
    public static class StepResult {

        @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
        private String content;
        @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
        private String expected;
        @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
        private String actual;
        @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
        private Integer statusId;

    }

}
