package com.cymbocha.apis.testrail.model;

import com.cymbocha.apis.testrail.internal.IntToBooleanDeserializer;
import com.cymbocha.apis.testrail.internal.StringToMapDeserializer;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TestRail field.
 */
@Data
@Accessors(chain = true)
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
     * Type of field.
     */
    @RequiredArgsConstructor
    public enum Type {
        UNKNOWN(Config.Options.class), STRING(Config.StringOptions.class), INTEGER(Config.IntegerOptions.class), TEXT(Config.TextOptions.class), URL(Config.UrlOptions.class), CHECKBOX(Config.CheckboxOptions.class), DROPDOWN(Config.DropdownOptions.class), USER(Config.UserOptions.class), DATE(Config.DateOptions.class), MILESTONE(Config.MilestoneOptions.class), STEPS(Config.StepsOptions.class), STEP_RESULTS(Config.StepResultsOptions.class), MULTI_SELECT(Config.MultiSelectOptions.class);

        @Getter
        private final Class<? extends Config.Options> optionsClass;

        public static Type getType(int typeId) {
            return typeId >= 0 && typeId < Type.values().length ? Type.values()[typeId] : UNKNOWN;
        }

    }

    /**
     * Configuration for a {@code Field}.
     */
    @Data
    @Accessors(chain = true)
    public static class Config {

        private String id;
        private Context context;
        private Options options;

        /**
         * Options for a {@code Field} configuration.
         */
        @Data
        @Accessors(chain = true)
        public static class Options {

            @JsonProperty
            @Getter(onMethod = @_({@JsonIgnore}))
            private boolean isRequired;
            @Getter(onMethod = @_({@JsonAnyGetter}))
            private Map<String, Object> unknownFields;

            @JsonAnySetter
            public Options addCustomField(String key, Object value) {
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
        @Accessors(chain = true)
        @ToString(callSuper = true)
        public static class StringOptions extends Options {
            private String defaultValue;
        }

        /**
         * Options for a {@code Field} of type {@link Type#INTEGER}.
         */
        @Data
        @Accessors(chain = true)
        @ToString(callSuper = true)
        public static class IntegerOptions extends Options {
            private BigInteger defaultValue;
        }

        /**
         * Options for a {@code Field} of type {@link Type#TEXT}.
         */
        @Data
        @Accessors(chain = true)
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
        @Accessors(chain = true)
        @ToString(callSuper = true)
        public static class UrlOptions extends Options {
            private String defaultValue;
        }

        /**
         * Options for a {@code Field} of type {@link Type#CHECKBOX}.
         */
        @Data
        @Accessors(chain = true)
        @ToString(callSuper = true)
        public static class CheckboxOptions extends Options {
            @JsonDeserialize(using = IntToBooleanDeserializer.class)
            private boolean defaultValue;
        }

        /**
         * Options for a {@code Field} of type {@link Type#DROPDOWN}.
         */
        @Data
        @Accessors(chain = true)
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
        @Accessors(chain = true)
        @ToString(callSuper = true)
        public static class UserOptions extends Options {
            private int defaultValue;
        }

        /**
         * Options for a {@code Field} of type {@link Type#DATE}.
         */
        @Data
        @Accessors(chain = true)
        @ToString(callSuper = true)
        public static class DateOptions extends Options {
        }

        /**
         * Options for a {@code Field} of type {@link Type#MILESTONE}.
         */
        @Data
        @Accessors(chain = true)
        @ToString(callSuper = true)
        public static class MilestoneOptions extends Options {
        }

        /**
         * Options for a {@code Field} of type {@link Type#STEPS}.
         */
        @Data
        @Accessors(chain = true)
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
        @Accessors(chain = true)
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
        @Accessors(chain = true)
        @ToString(callSuper = true)
        public static class MultiSelectOptions extends Options {
            @JsonDeserialize(using = StringToMapDeserializer.class)
            private Map<String, String> items;
        }


        @Data
        @Accessors(chain = true)
        public class Context {
            @JsonProperty
            @Getter(onMethod = @_({@JsonIgnore}))
            private boolean isGlobal;
            private List<Integer> projectIds;
        }

    }

}
