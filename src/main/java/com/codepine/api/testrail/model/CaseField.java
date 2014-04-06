package com.cymbocha.apis.testrail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * TestRail case field.
 */
@Data
@Accessors(chain = true)
public class CaseField {

    private int id;
    private int typeId;
    private String name;
    private String systemName;
    private String label;
    private String description;
    private List<Config> configs;
    private int displayOrder;

    @Data
    @Accessors(chain=true)
    public class Config {

        @Data
        @Accessors(chain=true)
        public class Context {
            @JsonProperty
            @Getter(onMethod = @_({@JsonIgnore}))
            private boolean isGlobal;
            private List<Integer> projectIds;
        }

        @Data
        @Accessors(chain=true)
        public class Options {
            @JsonProperty
            @Getter(onMethod = @_({@JsonIgnore}))
            private boolean isRequired;
            private String defaultValue;
            private String format;
            private int rows;
        }

        private int id;
        private Context context;
        private Options options;
    }
}
