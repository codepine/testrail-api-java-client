package com.cymbocha.apis.testrail.model;

import com.cymbocha.apis.testrail.TestRail;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * TestRail section.
 */
@Data
@Accessors(chain = true)
public class Section {

    private int id;

    @JsonView({TestRail.Sections.Add.class, TestRail.Sections.Update.class})
    private String name;

    @JsonView(TestRail.Sections.Add.class)
    private int suiteId;

    @JsonView(TestRail.Sections.Add.class)
    private int parentId;

    private int depth;

    private int displayOrder;

}
