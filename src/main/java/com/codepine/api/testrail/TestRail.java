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

package com.codepine.api.testrail;

import com.codepine.api.testrail.internal.BooleanToIntSerializer;
import com.codepine.api.testrail.internal.ListToCsvSerializer;
import com.codepine.api.testrail.model.*;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Client for Test Rail API. Configure and use it to create requests for the API.
 *
 * @see <a href="http://docs.gurock.com/testrail-api2/start">TestRail API v2 Documentation</a>
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(fluent = true)
public class TestRail {

    @Getter(value = AccessLevel.MODULE)
    @Accessors(fluent = false)
    private final TestRailConfig config;

    /**
     * Get a builder to build an instance of {@code TestRail}.
     *
     * @param endPoint the URL end point where your TestRail is hosted, for e.g. https://example.com/testrail
     * @param username the username of the TestRail user on behalf of which the communication with TestRail will happen
     * @param password the password of the same TestRail user
     * @return a builder to build {@code TestRail} instance
     */
    public static Builder builder(@NonNull final String endPoint, @NonNull final String username, @NonNull final String password) {
        return new Builder(endPoint, username, password);
    }

    /**
     * An accessor for creating requests for "Projects".
     *
     * @return a request factory
     */
    public Projects projects() {
        return new Projects();
    }

    /**
     * An accessor for creating requests for "Cases".
     *
     * @return a request factory
     */
    public Cases cases() {
        return new Cases();
    }

    /**
     * An accessor for creating requests for "Case Fields".
     *
     * @return a request factory
     */
    public CaseFields caseFields() {
        return new CaseFields();
    }

    /**
     * An accessor for creating requests for "Case Types".
     *
     * @return a request factory
     */
    public CaseTypes caseTypes() {
        return new CaseTypes();
    }

    /**
     * An accessor for creating requests for "Configurations".
     *
     * @return a request factory
     */
    public Configurations configurations() {
        return new Configurations();
    }

    /**
     * An accessor for creating requests for "Sections".
     *
     * @return a request factory
     */
    public Sections sections() {
        return new Sections();
    }

    /**
     * An accessor for creating requests for "Suites".
     *
     * @return a request factory
     */
    public Suites suites() {
        return new Suites();
    }

    /**
     * An accessor for creating requests for "Milestones".
     *
     * @return a request factory
     */
    public Milestones milestones() {
        return new Milestones();
    }

    /**
     * An accessor for creating requests for "Priorities".
     *
     * @return a request factory
     */
    public Priorities priorities() {
        return new Priorities();
    }

    /**
     * An accessor for creating requests for "Result Fields".
     *
     * @return a request factory
     */
    public ResultFields resultFields() {
        return new ResultFields();
    }

    /**
     * An accessor for creating requests for "Tests".
     *
     * @return a request factory
     */
    public Tests tests() {
        return new Tests();
    }

    /**
     * An accessor for creating requests for "Users".
     *
     * @return a request factory
     */
    public Users users() {
        return new Users();
    }

    /**
     * An accessor for creating requests for "Statuses".
     *
     * @return a request factory
     */
    public Statuses statuses() {
        return new Statuses();
    }

    /**
     * An accessor for creating requests for "Runs".
     *
     * @return a request factory
     */
    public Runs runs() {
        return new Runs();
    }

    /**
     * An accessor for creating requests for "Plans".
     *
     * @return a request factory
     */
    public Plans plans() {
        return new Plans();
    }

    /**
     * An accessor for creating requests for "Results".
     *
     * @return a request factory
     */
    public Results results() {
        return new Results();
    }

    /**
     * Builder for {@code TestRail}.
     */
    public static class Builder {

        private static final String DEFAULT_BASE_API_PATH = "index.php?/api/v2/";

        private final String endPoint;
        private final String username;
        private final String password;
        private String apiPath;
        private String applicationName;

        /**
         * @param endPoint the URL end point where your TestRail is hosted, for e.g. https://example.com/testrail
         * @param username the username of the TestRail user on behalf of which the communication with TestRail will happen
         * @param password the password of the same TestRail user
         */
        private Builder(final String endPoint, final String username, final String password) {
            String sanitizedEndPoint = endPoint.trim();
            if (!sanitizedEndPoint.endsWith("/")) {
                sanitizedEndPoint = sanitizedEndPoint + "/";
            }
            this.endPoint = sanitizedEndPoint;
            this.username = username;
            this.password = password;
            apiPath = DEFAULT_BASE_API_PATH;
        }

        /**
         * Set the URL path of your TestRail API. Useful to override the default API path of standard TestRail deployments.
         *
         * @param apiPath the URL path of your TestRail API
         * @return this for chaining
         * @throws NullPointerException if apiPath is null
         */
        public Builder apiPath(@NonNull final String apiPath) {
            String sanitizedApiPath = apiPath.trim();
            if (sanitizedApiPath.startsWith("/")) {
                sanitizedApiPath = sanitizedApiPath.substring(1);
            }
            if (!sanitizedApiPath.endsWith("/")) {
                sanitizedApiPath = sanitizedApiPath + "/";
            }
            this.apiPath = sanitizedApiPath;
            return this;
        }

        /**
         * Set the name of the application which will communicate with TestRail.
         *
         * @param applicationName name of the application
         * @return this for chaining
         * @throws NullPointerException if applicationName is null
         */
        public Builder applicationName(@NonNull final String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        /**
         * Build an instance of {@code TestRail}.
         *
         * @return a new instance
         */
        public TestRail build() {
            return new TestRail(new TestRailConfig(endPoint + apiPath, username, password, applicationName));
        }
    }

    /**
     * Request factories for "Projects".
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Projects {

        /**
         * Returns an existing project.
         *
         * @param projectId the ID of the project
         * @return the request
         * @throws java.lang.IllegalArgumentException if projectId is not positive
         */
        public Get get(final int projectId) {
            checkArgument(projectId > 0, "projectId should be positive");
            return new Get(projectId);
        }

        /**
         * Returns the list of available projects.
         *
         * @return the request
         */
        public List list() {
            return new List();
        }

        /**
         * Creates a new project.
         *
         * @param project the project to be added
         * @return the request
         * @throws java.lang.NullPointerException if project is null
         */
        public Add add(@NonNull Project project) {
            return new Add(project);
        }

        /**
         * Updates an existing project. Partial updates are supported, i.e. you can set and update specific fields only.
         *
         * @param project the project to be updated
         * @return the request
         * @throws java.lang.NullPointerException if project is null
         */
        public Update update(@NonNull Project project) {
            return new Update(project);
        }

        /**
         * Deletes an existing project.
         *
         * @param projectId the ID of the project to be deleted
         * @return the request
         * @throws java.lang.IllegalArgumentException if projectId is not positive
         */
        public Delete delete(final int projectId) {
            checkArgument(projectId > 0, "projectId should be positive");
            return new Delete(projectId);
        }

        public class Get extends Request<Project> {
            private static final String REST_PATH = "get_project/";

            private Get(int projectId) {
                super(config, Method.GET, REST_PATH + projectId, Project.class);
            }
        }

        @Getter
        @Setter
        public class List extends Request<java.util.List<Project>> {
            private static final String REST_PATH = "get_projects";

            @JsonView(List.class)
            @JsonSerialize(using = BooleanToIntSerializer.class)
            private Boolean isCompleted;

            private List() {
                super(config, Method.GET, REST_PATH, new TypeReference<java.util.List<Project>>() {
                }, new TypeReference<Page<java.util.List<Project>>>(){});
            }
        }

        public class Add extends Request<Project> {
            private static final String REST_PATH = "add_project";

            private final Project project;

            private Add(@NonNull Project project) {
                super(config, Method.POST, REST_PATH, Project.class);
                this.project = project;
            }

            @Override
            protected Object getContent() {
                return project;
            }

        }

        public class Update extends Request<Project> {
            private static final String REST_PATH = "update_project/";

            private final Project project;

            private Update(@NonNull Project project) {
                super(config, Method.POST, REST_PATH + project.getId(), Project.class);
                this.project = project;
            }

            @Override
            protected Object getContent() {
                return project;
            }

        }

        public class Delete extends Request<Void> {
            private static final String REST_PATH = "delete_project/";

            private Delete(int projectId) {
                super(config, Method.POST, REST_PATH + projectId, Void.class);
            }

        }
    }

    /**
     * Request factories for "Cases".
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Cases {

        /**
         * Returns an existing test case.
         * <p>The custom case fields configured in TestRail can be fetched using {@link CaseFields#list()} request.
         * The reason for not fetching this during execution of this request is to allow you to cache the list on your end to prevent an extra call on every execution.</p>
         *
         * @param testCaseId the ID of the test case
         * @param caseFields the custom case fields configured in TestRail to get type information for custom fields in the test case returned
         * @return the request
         * @throws java.lang.IllegalArgumentException if testCaseId is not positive
         * @throws java.lang.NullPointerException     if caseFields is null
         */
        public Get get(final int testCaseId, @NonNull java.util.List<CaseField> caseFields) {
            checkArgument(testCaseId > 0, "testCaseId should be positive");
            return new Get(testCaseId, caseFields);
        }

        /**
         * Returns the list of available test cases.
         * <p>The custom case fields configured in TestRail can be fetched using {@link CaseFields#list()} request.
         * The reason for not fetching this during execution of this request is to allow you to cache the list on your end to prevent an extra call on every execution.</p>
         *
         * @param projectId  the ID of the project which is operating in a single suite mode
         * @param caseFields the custom case fields configured in TestRail to get type information for custom fields in the test cases returned
         * @return the request
         * @throws java.lang.IllegalArgumentException if projectId is not positive
         * @throws java.lang.NullPointerException     if caseFields is null
         */
        public List list(final int projectId, @NonNull java.util.List<CaseField> caseFields) {
            checkArgument(projectId > 0, "projectId should be positive");
            return new List(projectId, caseFields);
        }

        /**
         * Returns the list of available test cases.
         * <p>The custom case fields configured in TestRail can be fetched using {@link CaseFields#list()} request.
         * The reason for not fetching this during execution of this request is to allow you to cache the list on your end to prevent an extra call on every execution.</p>
         *
         * @param projectId  the ID of the project
         * @param suiteId    the ID of the suite
         * @param caseFields the custom case fields configured in TestRail to get type information for custom fields in the test cases returned
         * @return the request
         * @throws java.lang.IllegalArgumentException if any argument is not positive
         * @throws java.lang.NullPointerException     if caseFields is null
         */
        public List list(final int projectId, final int suiteId, @NonNull java.util.List<CaseField> caseFields) {
            checkArgument(projectId > 0, "projectId should be positive");
            checkArgument(suiteId > 0, "suiteId should be positive");
            return new List(projectId, suiteId, caseFields);
        }

        /**
         * Creates a new test case.
         * <p>The custom case fields configured in TestRail can be fetched using {@link CaseFields#list()} request.
         * The reason for not fetching this during execution of this request is to allow you to cache the list on your end to prevent an extra call on every execution.</p>
         *
         * @param sectionId  the ID of the section to add the test case to
         * @param testCase   the test case to be added
         * @param caseFields the custom case fields configured in TestRail to get type information for custom fields in the test case returned
         * @return the request
         * @throws java.lang.IllegalArgumentException if sectionId is not positive
         * @throws java.lang.NullPointerException     if any other argument is null
         */
        public Add add(final int sectionId, @NonNull Case testCase, @NonNull java.util.List<CaseField> caseFields) {
            checkArgument(sectionId > 0, "projectId should be positive");
            return new Add(sectionId, testCase, caseFields);
        }

        /**
         * Updates an existing test case. Partial updates are supported, i.e. you can set and update specific fields only.
         * <p>The custom case fields configured in TestRail can be fetched using {@link CaseFields#list()} request.
         * The reason for not fetching this during execution of this request is to allow you to cache the list on your end to prevent an extra call on every execution.</p>
         *
         * @param testCase   the test case to be updated
         * @param caseFields the custom case fields configured in TestRail to get type information for custom fields in the test case returned
         * @return the request
         * @throws java.lang.NullPointerException if any argument is null
         */
        public Update update(@NonNull Case testCase, @NonNull java.util.List<CaseField> caseFields) {
            return new Update(testCase, caseFields);
        }

        /**
         * Deletes an existing test case.
         *
         * @param testCaseId the ID of the test case to be deleted
         * @return the request
         * @throws java.lang.IllegalArgumentException if testCaseId is not positive
         */
        public Delete delete(final int testCaseId) {
            checkArgument(testCaseId > 0, "testCaseId should be positive");
            return new Delete(testCaseId);
        }

        public class Get extends Request<Case> {
            private static final String REST_PATH = "get_case/";

            private final java.util.List<CaseField> caseFields;

            private Get(int testCaseId, java.util.List<CaseField> caseFields) {
                super(config, Method.GET, REST_PATH + testCaseId, Case.class);
                this.caseFields = caseFields;
            }

            @Override
            protected Object getSupplementForDeserialization() {
                return caseFields;
            }
        }

        @Getter
        @Setter
        @Accessors(fluent = true)
        public class List extends Request<java.util.List<Case>> {
            private static final String REST_PATH = "get_cases/%s&suite_id=%s";
            private final java.util.List<CaseField> caseFields;
            @JsonView(List.class)
            private Integer sectionId;
            @JsonView(List.class)
            private Date createdAfter;
            @JsonView(List.class)
            private Date createdBefore;
            @JsonView(List.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> createdBy;
            @JsonView(List.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> milestoneId;
            @JsonView(List.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> priorityId;
            @JsonView(List.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> typeId;
            @JsonView(List.class)
            private Date updatedAfter;
            @JsonView(List.class)
            private Date updatedBefore;
            @JsonView(List.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> updatedBy;

            @JsonView(List.class)
            private Integer templateId;

            @JsonView(List.class)
            private String refsFilter;

            private List(int projectId, java.util.List<CaseField> caseFields) {
                super(config, Method.GET, String.format(REST_PATH, projectId, ""), new TypeReference<java.util.List<Case>>() {
                }, new TypeReference<Page<java.util.List<Case>>>(){});
                this.caseFields = caseFields;
            }

            private List(int projectId, int suiteId, java.util.List<CaseField> caseFields) {
                super(config, Method.GET, String.format(REST_PATH, projectId, suiteId), new TypeReference<java.util.List<Case>>() {
                }, new TypeReference<Page<java.util.List<Case>>>(){});
                this.caseFields = caseFields;
            }

            @Override
            protected Object getSupplementForDeserialization() {
                return caseFields;
            }

        }

        public class Add extends Request<Case> {
            private static final String REST_PATH = "add_case/";

            private final Case testCase;
            private final java.util.List<CaseField> caseFields;

            private Add(int sectionId, Case testCase, java.util.List<CaseField> caseFields) {
                super(config, Method.POST, REST_PATH + sectionId, Case.class);
                this.testCase = testCase;
                this.caseFields = caseFields;
            }

            @Override
            protected Object getContent() {
                return testCase;
            }

            @Override
            protected Object getSupplementForDeserialization() {
                return caseFields;
            }

        }

        public class Update extends Request<Case> {
            private static final String REST_PATH = "update_case/";

            private final Case testCase;
            private final java.util.List<CaseField> caseFields;

            private Update(Case testCase, java.util.List<CaseField> caseFields) {
                super(config, Method.POST, REST_PATH + testCase.getId(), Case.class);
                this.testCase = testCase;
                this.caseFields = caseFields;
            }

            @Override
            protected Object getContent() {
                return testCase;
            }

            @Override
            protected Object getSupplementForDeserialization() {
                return caseFields;
            }

        }

        public class Delete extends Request<Void> {
            private static final String REST_PATH = "delete_case/";

            private Delete(int testCaseId) {
                super(config, Method.POST, REST_PATH + testCaseId, Void.class);
            }
        }
    }

    /**
     * Request factories for "Case Fields".
     */
    @NoArgsConstructor
    public class CaseFields {

        /**
         * Returns a list of available test case custom fields.
         *
         * @return the request
         */
        public List list() {
            return new List();
        }

        public class List extends Request<java.util.List<CaseField>> {
            private static final String REST_PATH = "get_case_fields";

            private List() {
                super(config, Method.GET, REST_PATH, new TypeReference<java.util.List<CaseField>>() {
                }, new TypeReference<Page<java.util.List<CaseField>>>(){});
            }
        }
    }

    /**
     * Request factories for "Case Types".
     */
    @NoArgsConstructor
    public class CaseTypes {

        /**
         * Returns a list of available case types.
         *
         * @return the request
         */
        public List list() {
            return new List();
        }

        public class List extends Request<java.util.List<CaseType>> {
            private static final String REST_PATH = "get_case_types";

            private List() {
                super(config, Method.GET, REST_PATH, new TypeReference<java.util.List<CaseType>>() {
                }, new TypeReference<Page<java.util.List<CaseType>>>(){});
            }
        }

    }

    /**
     * Request factories for "Configurations".
     */
    @NoArgsConstructor
    public class Configurations {

        /**
         * Returns a list of available configurations, grouped by configuration groups.
         *
         * @param projectId the ID of the project to get the configurations for
         * @return the request
         * @throws java.lang.IllegalArgumentException if projectId is not positive
         */
        public List list(final int projectId) {
            checkArgument(projectId > 0, "projectId should be positive");
            return new List(projectId);
        }

        public class List extends Request<java.util.List<Configuration>> {
            private static final String REST_PATH = "get_configs/";

            private List(int projectId) {
                super(config, Method.GET, REST_PATH + projectId, new TypeReference<java.util.List<Configuration>>() {
                }, new TypeReference<Page<java.util.List<Configuration>>>(){});
            }

        }

    }

    /**
     * Request factories for "Milestones".
     */
    @NoArgsConstructor
    public class Milestones {

        /**
         * Returns an existing milestone.
         *
         * @param milestoneId the ID of the milestone
         * @return the request
         * @throws java.lang.IllegalArgumentException if milestoneId is not positive
         */
        public Get get(final int milestoneId) {
            checkArgument(milestoneId > 0, "milestoneId should be positive");
            return new Get(milestoneId);
        }

        /**
         * Returns the list of milestones for a project.
         *
         * @param projectId the ID of the project to get the milestones for
         * @return the request
         * @throws java.lang.IllegalArgumentException if projectId is not positive
         */
        public List list(final int projectId) {
            checkArgument(projectId > 0, "projectId should be positive");
            return new List(projectId);
        }

        /**
         * Creates a new milestone.
         *
         * @param projectId the ID of the project to add the milestone to
         * @param milestone the milestone to be added
         * @return the request
         * @throws java.lang.IllegalArgumentException if projectId is not positive
         * @throws java.lang.NullPointerException     if milestone is null
         */
        public Add add(final int projectId, @NonNull Milestone milestone) {
            checkArgument(projectId > 0, "projectId should be positive");
            return new Add(projectId, milestone);
        }

        /**
         * Updates an existing milestone. Partial updates are supported, i.e. you can set and update specific fields only.
         *
         * @param milestone the milestone to be updated
         * @return the request
         * @throws java.lang.NullPointerException if milestone is null
         */
        public Update update(@NonNull Milestone milestone) {
            return new Update(milestone);
        }

        /**
         * Deletes an existing milestone.
         *
         * @param milestoneId the ID of the milestone to be deleted
         * @return the request
         * @throws java.lang.IllegalArgumentException if milestoneId is not positive
         */
        public Delete delete(final int milestoneId) {
            checkArgument(milestoneId > 0, "milestoneId should be positive");
            return new Delete(milestoneId);
        }

        public class Get extends Request<Milestone> {
            private static final String REST_PATH = "get_milestone/";

            private Get(int milestoneId) {
                super(config, Method.GET, REST_PATH + milestoneId, Milestone.class);
            }
        }

        @Getter
        @Setter
        @Accessors(fluent = true)
        public class List extends Request<java.util.List<Milestone>> {
            private static final String REST_PATH = "get_milestones/";

            @JsonView(List.class)
            @JsonSerialize(using = BooleanToIntSerializer.class)
            private Boolean isCompleted;

            private List(int projectId) {
                super(config, Method.GET, REST_PATH + projectId, new TypeReference<java.util.List<Milestone>>() {
                }, new TypeReference<Page<java.util.List<Milestone>>>(){});
            }
        }

        public class Add extends Request<Milestone> {
            private static final String REST_PATH = "add_milestone/";

            private final Milestone milestone;

            private Add(int projectId, Milestone milestone) {
                super(config, Method.POST, REST_PATH + projectId, Milestone.class);
                this.milestone = milestone;
            }

            @Override
            protected Object getContent() {
                return milestone;
            }
        }

        public class Update extends Request<Milestone> {
            private static final String REST_PATH = "update_milestone/";

            private final Milestone milestone;

            private Update(Milestone milestone) {
                super(config, Method.POST, REST_PATH + milestone.getId(), Milestone.class);
                this.milestone = milestone;
            }

            @Override
            protected Object getContent() {
                return milestone;
            }
        }

        public class Delete extends Request<Void> {
            private static final String REST_PATH = "delete_milestone/";

            private Delete(int milestoneId) {
                super(config, Method.POST, REST_PATH + milestoneId, Void.class);
            }
        }

    }

    /**
     * Request factories for "Priorities".
     */
    @NoArgsConstructor
    public class Priorities {

        /**
         * Returns a list of available priorities.
         *
         * @return the request
         */
        public List list() {
            return new List();
        }

        public class List extends Request<java.util.List<Priority>> {
            private static final String REST_PATH = "get_priorities";

            private List() {
                super(config, Method.GET, REST_PATH, new TypeReference<java.util.List<Priority>>() {
                }, new TypeReference<Page<java.util.List<Priority>>>(){});
            }
        }

    }

    /**
     * Request factories for "Plans".
     */
    @NoArgsConstructor
    public class Plans {

        /**
         * Returns an existing test plan.
         *
         * @param planId the ID of the test plan
         * @return the request
         * @throws java.lang.IllegalArgumentException if planId is not positive
         */
        public Get get(final int planId) {
            checkArgument(planId > 0, "planId should be positive");
            return new Get(planId);
        }

        /**
         * Returns a list of test plans for a project.
         *
         * @param projectId the ID of the project to get the plans for
         * @return the request
         * @throws java.lang.IllegalArgumentException if projectId is not positive
         */
        public List list(final int projectId) {
            checkArgument(projectId > 0, "projectId should be positive");
            return new List(projectId);
        }

        /**
         * Creates a new test plan.
         *
         * @param projectId the ID of the project to add the plans to
         * @param plan      the test plan to be added
         * @return the request
         * @throws java.lang.IllegalArgumentException if projectId is not positive
         * @throws java.lang.NullPointerException     if plan is null
         */
        public Add add(final int projectId, @NonNull Plan plan) {
            checkArgument(projectId > 0, "projectId should be positive");
            return new Add(projectId, plan);
        }

        /**
         * Updates an existing test plan. Partial updates are supported, i.e. you can set and update specific fields only.
         *
         * @param plan the test plan to be updated
         * @return the request
         * @throws java.lang.NullPointerException if plan is null
         */
        public Update update(@NonNull Plan plan) {
            return new Update(plan);
        }

        /**
         * Closes an existing test plan and archives its test runs & results.
         *
         * @param planId the ID of the test plan to be closed
         * @return the request
         * @throws java.lang.IllegalArgumentException if planId is not positive
         */
        public Close close(final int planId) {
            checkArgument(planId > 0, "planId should be positive");
            return new Close(planId);
        }

        /**
         * Deletes an existing test plan.
         *
         * @param planId the ID of the test plan to be deleted
         * @return the request
         * @throws java.lang.IllegalArgumentException if planId is not positive
         */
        public Delete delete(final int planId) {
            checkArgument(planId > 0, "planId should be positive");
            return new Delete(planId);
        }

        /**
         * Adds one or more new test runs to a test plan.
         *
         * @param planId the ID of the test plan to add the entry to
         * @param entry  the plan entry to be added
         * @return the request
         * @throws java.lang.IllegalArgumentException if planId is not positive
         * @throws NullPointerException               if any argument is null
         */
        public AddEntry addEntry(final int planId, @NonNull Plan.Entry entry) {
            checkArgument(planId > 0, "planId should be positive");
            return new AddEntry(planId, entry);
        }

        /**
         * Updates one or more existing test runs in a plan. Partial updates are supported, i.e. you can set and update specific fields only.
         *
         * @param planId the ID of the test plan to update the entry in
         * @param entry  the plan entry to be updated
         * @return the request
         * @throws java.lang.IllegalArgumentException if planId is not positive
         * @throws java.lang.NullPointerException     if any argument is null
         */
        public UpdateEntry updateEntry(final int planId, @NonNull Plan.Entry entry) {
            checkArgument(planId > 0, "planId should be positive");
            return new UpdateEntry(planId, entry);
        }

        /**
         * Deletes one or more existing test runs from a plan.
         *
         * @param planId  the ID of the test plan to delete entry from
         * @param entryId the ID of the plan entry to be deleted
         * @return the request
         * @throws java.lang.IllegalArgumentException if any argument is not positive
         */
        public DeleteEntry deleteEntry(final int planId, @NonNull final String entryId) {
            checkArgument(planId > 0, "planId should be positive");
            return new DeleteEntry(planId, entryId);
        }

        public class Get extends Request<Plan> {
            private static final String REST_PATH = "get_plan/";

            private Get(int planId) {
                super(config, Method.GET, REST_PATH + planId, Plan.class);
            }
        }

        @Getter
        @Setter
        @Accessors(fluent = true)
        public class List extends Request<java.util.List<Plan>> {
            private static final String REST_PATH = "get_plans/";

            @JsonView(List.class)
            private Date createdAfter;

            @JsonView(List.class)
            private Date createdBefore;

            @JsonView(List.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> createdBy;

            @JsonView(List.class)
            @JsonSerialize(using = BooleanToIntSerializer.class)
            private Boolean isCompleted;

            @JsonView(List.class)
            private Integer limit;

            @JsonView(List.class)
            private Integer offset;

            @JsonView(List.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> milestoneId;

            private List(int projectId) {
                super(config, Method.GET, REST_PATH + projectId, new TypeReference<java.util.List<Plan>>() {
                }, new TypeReference<Page<java.util.List<Plan>>>(){});
            }
        }

        public class Add extends Request<Plan> {
            private static final String REST_PATH = "add_plan/";

            private final Plan plan;

            private Add(int projectId, Plan plan) {
                super(config, Method.POST, REST_PATH + projectId, Plan.class);
                this.plan = plan;
            }

            @Override
            protected Object getContent() {
                return plan;
            }
        }

        public class AddEntry extends Request<Plan.Entry> {
            private static final String REST_PATH = "add_plan_entry/";

            private final Plan.Entry entry;

            private AddEntry(int planId, Plan.Entry entry) {
                super(config, Method.POST, REST_PATH + planId, Plan.Entry.class);
                this.entry = entry;
            }

            @Override
            protected Object getContent() {
                return entry;
            }
        }

        public class Update extends Request<Plan> {
            private static final String REST_PATH = "update_plan/";

            private final Plan plan;

            private Update(Plan plan) {
                super(config, Method.POST, REST_PATH + plan.getId(), Plan.class);
                this.plan = plan;
            }

            @Override
            protected Object getContent() {
                return plan;
            }
        }

        public class UpdateEntry extends Request<Plan.Entry> {
            private static final String REST_PATH = "update_plan_entry/%s/%s";

            private final Plan.Entry entry;

            private UpdateEntry(int planId, Plan.Entry entry) {
                super(config, Method.POST, String.format(REST_PATH, planId, entry.getId()), Plan.Entry.class);
                this.entry = entry;
            }

            @Override
            protected Object getContent() {
                return entry;
            }
        }

        public class Close extends Request<Plan> {
            private static final String REST_PATH = "close_plan/";

            private Close(int planId) {
                super(config, Method.POST, REST_PATH + planId, Plan.class);
            }
        }

        public class Delete extends Request<Void> {
            private static final String REST_PATH = "delete_plan/";

            private Delete(int planId) {
                super(config, Method.POST, REST_PATH + planId, Void.class);
            }
        }

        public class DeleteEntry extends Request<Void> {
            private static final String REST_PATH = "delete_plan_entry/%s/%s";

            private DeleteEntry(int planId, String entryId) {
                super(config, Method.POST, String.format(REST_PATH, planId, entryId), Void.class);
            }
        }
    }

    /**
     * Request factories for "Results".
     */
    @NoArgsConstructor
    public class Results {

        /**
         * Returns a list of test results for a test.
         * <p>The custom result fields configured in TestRail can be fetched using {@link ResultFields#list()} request.
         * The reason for not fetching this during execution of this request is to allow you to cache the list on your end to prevent an extra call on every execution.</p>
         *
         * @param testId       the ID of the test to get the results for
         * @param resultFields the custom result fields configured in TestRail to get type information for custom fields in the results returned
         * @return the request
         * @throws java.lang.IllegalArgumentException if testId is not positive
         * @throws java.lang.NullPointerException     if resultFields is null
         */
        public List list(final int testId, @NonNull java.util.List<ResultField> resultFields) {
            checkArgument(testId > 0, "testId should be positive");
            return new List(testId, resultFields);
        }

        /**
         * Returns a list of test results for a test run and case combination.
         * <p>The custom result fields configured in TestRail can be fetched using {@link ResultFields#list()} request.
         * The reason for not fetching this during execution of this request is to allow you to cache the list on your end to prevent an extra call on every execution.</p>
         *
         * @param runId        the ID of the test run
         * @param testCaseId   the ID of the test case
         * @param resultFields the custom result fields configured in TestRail to get type information for custom fields in the results returned
         * @return the request
         * @throws java.lang.IllegalArgumentException if any argument is not positive
         * @throws java.lang.NullPointerException     if resultFields is null
         */
        public ListForCase listForCase(final int runId, final int testCaseId, @NonNull java.util.List<ResultField> resultFields) {
            checkArgument(runId > 0, "runId should be positive");
            checkArgument(testCaseId > 0, "testCaseId should be positive");
            return new ListForCase(runId, testCaseId, resultFields);
        }

        /**
         * Returns a list of test results for a test run.
         * <p>The custom result fields configured in TestRail can be fetched using {@link ResultFields#list()} request.
         * The reason for not fetching this during execution of this request is to allow you to cache the list on your end to prevent an extra call on every execution.</p>
         *
         * @param runId        the ID of the test run to get the results for
         * @param resultFields the custom result fields configured in TestRail to get type information for custom fields in the results returned
         * @return the request
         * @throws java.lang.IllegalArgumentException if runId is not positive
         * @throws java.lang.NullPointerException     if resultFields is null
         */
        public ListForRun listForRun(final int runId, @NonNull java.util.List<ResultField> resultFields) {
            checkArgument(runId > 0, "runId should be positive");
            return new ListForRun(runId, resultFields);
        }

        /**
         * Adds a new test result, comment or assigns a test.
         * <p>The custom result fields configured in TestRail can be fetched using {@link ResultFields#list()} request.
         * The reason for not fetching this during execution of this request is to allow you to cache the list on your end to prevent an extra call on every execution.</p>
         *
         * @param testId       the ID of the test whose result is to be added
         * @param result       the test result to be added
         * @param resultFields the custom result fields configured in TestRail to get type information for custom fields in the result returned
         * @return the request
         * @throws java.lang.IllegalArgumentException if testId is not positive
         * @throws java.lang.NullPointerException     if any other argument is null
         */
        public Add add(final int testId, @NonNull Result result, @NonNull java.util.List<ResultField> resultFields) {
            checkArgument(testId > 0, "testId should be positive");
            return new Add(testId, result, resultFields);
        }

        /**
         * Adds a new test result, comment or assigns a test (for a test run and case combination).
         * <p>The custom result fields configured in TestRail can be fetched using {@link ResultFields#list()} request.
         * The reason for not fetching this during execution of this request is to allow you to cache the list on your end to prevent an extra call on every execution.</p>
         *
         * @param runId        the ID of the test run
         * @param testCaseId   the ID of the test case
         * @param result       the test result to be added
         * @param resultFields the custom result fields configured in TestRail to get type information for custom fields in the result returned
         * @return the request
         * @throws java.lang.IllegalArgumentException if runId or testCaseId is not positive
         * @throws java.lang.NullPointerException     if any other argument is null
         */
        public AddForCase addForCase(final int runId, final int testCaseId, @NonNull Result result, @NonNull java.util.List<ResultField> resultFields) {
            checkArgument(runId > 0, "runId should be positive");
            checkArgument(testCaseId > 0, "testCaseId should be positive");
            return new AddForCase(runId, testCaseId, result, resultFields);
        }

        /**
         * Adds one or more new test results, comments or assigns one or more tests.
         * <p>The custom result fields configured in TestRail can be fetched using {@link ResultFields#list()} request.
         * The reason for not fetching this during execution of this request is to allow you to cache the list on your end to prevent an extra call on every execution.</p>
         *
         * @param runId        the ID of the test run to add the results to
         * @param results      the test results to be added
         * @param resultFields the custom result fields configured in TestRail to get type information for custom fields in the results returned
         * @return the request
         * @throws java.lang.IllegalArgumentException if runId is not positive or results is empty
         * @throws java.lang.NullPointerException     if results or resultFields are null
         */
        public AddList add(final int runId, @NonNull java.util.List<Result> results, @NonNull java.util.List<ResultField> resultFields) {
            checkArgument(runId > 0, "runId should be positive");
            checkArgument(!results.isEmpty(), "results cannot be empty");
            return new AddList(runId, results, resultFields);
        }

        /**
         * Adds one or more new test results, comments or assigns one or more tests (using the case IDs).
         * <p>The custom result fields configured in TestRail can be fetched using {@link ResultFields#list()} request.
         * The reason for not fetching this during execution of this request is to allow you to cache the list on your end to prevent an extra call on every execution.</p>
         *
         * @param runId        the ID of the test run to add the results to
         * @param results      the test results to be added
         * @param resultFields the custom result fields configured in TestRail to get type information for custom fields in the results returned
         * @return the request
         * @throws java.lang.IllegalArgumentException if runId is not positive or results is empty
         * @throws java.lang.NullPointerException     if results or resultFields are null
         */
        public AddListForCases addForCases(final int runId, @NonNull java.util.List<Result> results, @NonNull java.util.List<ResultField> resultFields) {
            checkArgument(runId > 0, "runId should be positive");
            checkArgument(!results.isEmpty(), "results cannot be empty");
            return new AddListForCases(runId, results, resultFields);
        }

        @Getter
        @Setter
        @Accessors(fluent = true)
        public class List extends Request<java.util.List<Result>> {
            private static final String REST_PATH = "get_results/";
            private final java.util.List<ResultField> resultFields;
            @JsonView(List.class)
            private Integer limit;
            @JsonView(List.class)
            private Integer offset;
            @JsonView(List.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> statusId;

            private List(int testId, java.util.List<ResultField> resultFields) {
                super(config, Method.GET, REST_PATH + testId, new TypeReference<java.util.List<Result>>() {
                }, new TypeReference<Page<java.util.List<Result>>>(){});
                this.resultFields = resultFields;
            }

            @Override
            protected Object getSupplementForDeserialization() {
                return resultFields;
            }
        }

        @Getter
        @Setter
        @Accessors(fluent = true)
        public class ListForRun extends Request<java.util.List<Result>> {
            private static final String REST_PATH = "get_results_for_run/";
            private final java.util.List<ResultField> resultFields;
            @JsonView(ListForRun.class)
            private Date createdAfter;
            @JsonView(ListForRun.class)
            private Date createdBefore;
            @JsonView(ListForRun.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> createdBy;
            @JsonView(ListForRun.class)
            private Integer limit;
            @JsonView(ListForRun.class)
            private Integer offset;
            @JsonView(ListForRun.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> statusId;

            private ListForRun(int runId, java.util.List<ResultField> resultFields) {
                super(config, Method.GET, REST_PATH + runId, new TypeReference<java.util.List<Result>>() {
                }, new TypeReference<Page<java.util.List<Result>>>(){});
                this.resultFields = resultFields;
            }

            @Override
            protected Object getSupplementForDeserialization() {
                return resultFields;
            }
        }

        @Getter
        @Setter
        @Accessors(fluent = true)
        public class ListForCase extends Request<java.util.List<Result>> {
            private static final String REST_PATH = "get_results_for_case/";
            private final java.util.List<ResultField> resultFields;
            @JsonView(ListForCase.class)
            private Integer limit;
            @JsonView(ListForCase.class)
            private Integer offset;
            @JsonView(ListForCase.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> statusId;

            private ListForCase(int runId, int testCaseId, java.util.List<ResultField> resultFields) {
                super(config, Method.GET, REST_PATH + runId + "/" + testCaseId, new TypeReference<java.util.List<Result>>() {
                }, new TypeReference<Page<java.util.List<Result>>>(){});
                this.resultFields = resultFields;
            }

            @Override
            protected Object getSupplementForDeserialization() {
                return resultFields;
            }
        }

        public class Add extends Request<Result> {
            private static final String REST_PATH = "add_result/";

            private final Result result;
            private final java.util.List<ResultField> resultFields;

            private Add(int testId, Result result, java.util.List<ResultField> resultFields) {
                super(config, Method.POST, REST_PATH + testId, Result.class);
                this.result = result;
                this.resultFields = resultFields;
            }

            @Override
            protected Object getContent() {
                return result;
            }

            @Override
            protected Object getSupplementForDeserialization() {
                return resultFields;
            }
        }

        public class AddForCase extends Request<Result> {
            private static final String REST_PATH = "add_result_for_case/";

            private final Result result;
            private final java.util.List<ResultField> resultFields;

            private AddForCase(int runId, int testCaseId, Result result, java.util.List<ResultField> resultFields) {
                super(config, Method.POST, REST_PATH + runId + "/" + testCaseId, Result.class);
                this.result = result;
                this.resultFields = resultFields;
            }

            @Override
            protected Object getContent() {
                return result;
            }

            @Override
            protected Object getSupplementForDeserialization() {
                return resultFields;
            }
        }

        public class AddList extends Request<java.util.List<Result>> {
            private static final String REST_PATH = "add_results/";

            private final Result.List results;
            private final java.util.List<ResultField> resultFields;

            private AddList(final int runId, java.util.List<Result> results, java.util.List<ResultField> resultFields) {
                super(config, Method.POST, REST_PATH + runId, new TypeReference<java.util.List<Result>>() {
                });
                this.results = new Result.List(results);
                this.resultFields = resultFields;
            }

            @Override
            protected Object getContent() {
                return results;
            }

            @Override
            protected Object getSupplementForDeserialization() {
                return resultFields;
            }
        }

        public class AddListForCases extends Request<java.util.List<Result>> {
            private static final String REST_PATH = "add_results_for_cases/";

            private final Result.List results;
            private final java.util.List<ResultField> resultFields;

            private AddListForCases(int runId, java.util.List<Result> results, java.util.List<ResultField> resultFields) {
                super(config, Method.POST, REST_PATH + runId, new TypeReference<java.util.List<Result>>() {
                });
                this.results = new Result.List(results);
                this.resultFields = resultFields;
            }

            @Override
            protected Object getContent() {
                return results;
            }

            @Override
            protected Object getSupplementForDeserialization() {
                return resultFields;
            }
        }

    }


    /**
     * Request factories for "Result Fields".
     */
    @NoArgsConstructor
    public class ResultFields {

        /**
         * Returns a list of available test result custom fields.
         *
         * @return the request
         */
        public List list() {
            return new List();
        }

        public class List extends Request<java.util.List<ResultField>> {
            private static final String REST_PATH = "get_result_fields";

            private List() {
                super(config, Method.GET, REST_PATH, new TypeReference<java.util.List<ResultField>>() {
                }, new TypeReference<Page<java.util.List<ResultField>>>(){});
            }
        }
    }

    /**
     * Request factories for "Runs".
     */
    @NoArgsConstructor
    public class Runs {

        /**
         * Returns an existing test run.
         *
         * @param runId the ID of the test run
         * @return the request
         * @throws java.lang.IllegalArgumentException if runId is not positive
         */
        public Get get(final int runId) {
            checkArgument(runId > 0, "runId should be positive");
            return new Get(runId);
        }

        /**
         * Returns a list of test runs for a project. Only returns those test runs that are not part of a test plan.
         *
         * @param projectId the ID of the project to get the runs for
         * @return the request
         * @throws java.lang.IllegalArgumentException if projectId is not positive
         */
        public List list(final int projectId) {
            checkArgument(projectId > 0, "projectId should be positive");
            return new List(projectId);
        }

        /**
         * Creates a new test run.
         *
         * @param projectId the ID of the project to add the run to
         * @param run       the test run to be added
         * @return the request
         * @throws java.lang.IllegalArgumentException if projectId is not positive
         * @throws java.lang.NullPointerException     if run is null
         */
        public Add add(final int projectId, @NonNull Run run) {
            checkArgument(projectId > 0, "projectId should be positive");
            return new Add(projectId, run);
        }

        /**
         * Updates an existing test run. Partial updates are supported, i.e. you can set and update specific fields only.
         *
         * @param run the test run to be updated
         * @return the request
         * @throws java.lang.NullPointerException if run is null
         */
        public Update update(@NonNull Run run) {
            return new Update(run);
        }

        /**
         * Closes an existing test run and archives its tests & results.
         *
         * @param runId the ID of the test run to be closed
         * @return the request
         * @throws java.lang.IllegalArgumentException if runId is not positive
         */
        public Close close(final int runId) {
            checkArgument(runId > 0, "runId should be positive");
            return new Close(runId);
        }

        /**
         * Deletes an existing test run.
         *
         * @param runId the ID of the test run to be deleted
         * @return the request
         * @throws java.lang.IllegalArgumentException if runId is not positive
         */
        public Delete delete(final int runId) {
            checkArgument(runId > 0, "runId should be positive");
            return new Delete(runId);
        }

        public class Get extends Request<Run> {
            private static final String REST_PATH = "get_run/";

            private Get(int runId) {
                super(config, Method.GET, REST_PATH + runId, Run.class);
            }
        }

        @Getter
        @Setter
        @Accessors(fluent = true)
        public class List extends Request<java.util.List<Run>> {
            private static final String REST_PATH = "get_runs/";

            @JsonView(List.class)
            private Date createdAfter;

            @JsonView(List.class)
            private Date createdBefore;

            @JsonView(List.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> createdBy;

            @JsonView(List.class)
            @JsonSerialize(using = BooleanToIntSerializer.class)
            private Boolean isCompleted;

            @JsonView(List.class)
            private Integer limit;

            @JsonView(List.class)
            private Integer offset;

            @JsonView(List.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> milestoneId;

            @JsonView(List.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> suiteId;

            @JsonView(List.class)
            private String refsFilter;

            private List(int projectId) {
                super(config, Method.GET, REST_PATH + projectId, new TypeReference<java.util.List<Run>>() {
                }, new TypeReference<Page<java.util.List<Run>>>(){});
            }
        }

        public class Add extends Request<Run> {
            private static final String REST_PATH = "add_run/";

            private final Run run;

            private Add(int projectId, Run run) {
                super(config, Method.POST, REST_PATH + projectId, Run.class);
                this.run = run;
            }

            @Override
            protected Object getContent() {
                return run;
            }
        }

        public class Update extends Request<Run> {
            private static final String REST_PATH = "update_run/";

            private final Run run;

            private Update(Run run) {
                super(config, Method.POST, REST_PATH + run.getId(), Run.class);
                this.run = run;
            }

            @Override
            protected Object getContent() {
                return run;
            }
        }

        public class Close extends Request<Run> {
            private static final String REST_PATH = "close_run/";

            private Close(int runId) {
                super(config, Method.POST, REST_PATH + runId, Run.class);
            }
        }

        public class Delete extends Request<Void> {
            private static final String REST_PATH = "delete_run/";

            private Delete(int runId) {
                super(config, Method.POST, REST_PATH + runId, Void.class);
            }
        }

    }

    /**
     * Request factories for "Sections".
     */
    @NoArgsConstructor
    public class Sections {

        /**
         * Returns an existing section.
         *
         * @param sectionId the ID of the section
         * @return the request
         * @throws java.lang.IllegalArgumentException if sectionId is not positive
         */
        public Get get(final int sectionId) {
            checkArgument(sectionId > 0, "sectionId should be positive");
            return new Get(sectionId);
        }

        /**
         * Returns a list of sections for a project.
         *
         * @param projectId the ID of the project which is operating in a single suite mode
         * @return the request
         * @throws java.lang.IllegalArgumentException if projectId is not positive
         */
        public List list(final int projectId) {
            checkArgument(projectId > 0, "projectId should be positive");
            return new List(projectId);
        }

        /**
         * Returns a list of sections for a project and test suite.
         *
         * @param projectId the ID of the project
         * @param suiteId   the ID of the suite
         * @return the request
         * @throws java.lang.IllegalArgumentException if any argument is not positive
         */
        public List list(final int projectId, final int suiteId) {
            checkArgument(projectId > 0, "projectId should be positive");
            checkArgument(suiteId > 0, "suiteId should be positive");
            return new List(projectId, suiteId);
        }

        /**
         * Creates a new section.
         *
         * @param projectId the ID of the project to add the section to
         * @param section   the section to be added
         * @return the request
         * @throws java.lang.NullPointerException if section is null
         */
        public Add add(final int projectId, @NonNull Section section) {
            checkArgument(projectId > 0, "projectId should be positive");
            return new Add(projectId, section);
        }

        /**
         * Updates an existing section. Partial updates are supported, i.e. you can set and update specific fields only.
         *
         * @param section the section to be updated
         * @return the request
         * @throws java.lang.NullPointerException if section is null
         */
        public Update update(@NonNull Section section) {
            return new Update(section);
        }

        /**
         * Deletes an existing section.
         *
         * @param sectionId the ID of the section to be deleted
         * @return the request
         * @throws java.lang.IllegalArgumentException if sectionId is not positive
         */
        public Delete delete(final int sectionId) {
            checkArgument(sectionId > 0, "sectionId should be positive");
            return new Delete(sectionId);
        }

        public class Get extends Request<Section> {
            private static final String REST_PATH = "get_section/";

            private Get(int sectionId) {
                super(config, Method.GET, REST_PATH + sectionId, Section.class);
            }

        }

        public class List extends Request<java.util.List<Section>> {
            private static final String REST_PATH = "get_sections/%s&suite_id=%s";

            private List(int projectId) {
                super(config, Method.GET, String.format(REST_PATH, projectId, ""), new TypeReference<java.util.List<Section>>() {
                }, new TypeReference<Page<java.util.List<Section>>>(){});
            }

            private List(int projectId, int suiteId) {
                super(config, Method.GET, String.format(REST_PATH, projectId, suiteId), new TypeReference<java.util.List<Section>>() {
                }, new TypeReference<Page<java.util.List<Section>>>(){});
            }
        }

        public class Add extends Request<Section> {
            private static final String REST_PATH = "add_section/";

            private final Section section;

            private Add(int projectId, Section section) {
                super(config, Method.POST, REST_PATH + projectId, Section.class);
                this.section = section;
            }

            @Override
            protected Object getContent() {
                return section;
            }
        }

        public class Update extends Request<Section> {
            private static final String REST_PATH = "update_section/";

            private final Section section;

            private Update(Section section) {
                super(config, Method.POST, REST_PATH + section.getId(), Section.class);
                this.section = section;
            }

            @Override
            protected Object getContent() {
                return section;
            }
        }

        public class Delete extends Request<Void> {
            private static final String REST_PATH = "delete_section/";

            private Delete(int sectionId) {
                super(config, Method.POST, REST_PATH + sectionId, Void.class);
            }
        }
    }

    /**
     * Request factories for "Statuses".
     */
    @NoArgsConstructor
    public class Statuses {

        /**
         * Returns a list of available test statuses.
         *
         * @return the request
         */
        public List list() {
            return new List();
        }

        public class List extends Request<java.util.List<Status>> {
            private static final String REST_PATH = "get_statuses";

            private List() {
                super(config, Method.GET, REST_PATH, new TypeReference<java.util.List<Status>>() {
                }, new TypeReference<Page<java.util.List<Status>>>(){});
            }
        }

    }

    /**
     * Request factories for "Suites".
     */
    @NoArgsConstructor
    public class Suites {

        /**
         * Returns an existing test suite.
         *
         * @param suiteId the ID of the test suite
         * @return the request
         * @throws java.lang.IllegalArgumentException if suiteId is not positive
         */
        public Get get(final int suiteId) {
            checkArgument(suiteId > 0, "suiteId should be positive");
            return new Get(suiteId);
        }

        /**
         * Returns a list of test suites for a project.
         *
         * @param projectId the ID of the project to get the test suites for
         * @return the request
         * @throws java.lang.IllegalArgumentException if projectId is not positive
         */
        public List list(final int projectId) {
            checkArgument(projectId > 0, "projectId should be positive");
            return new List(projectId);
        }

        /**
         * Creates a new test suite.
         *
         * @param projectId the ID of the project to add the test suite to
         * @param suite     the test suite to be added
         * @return the request
         * @throws java.lang.IllegalArgumentException if projectId is not positive
         * @throws java.lang.NullPointerException     if suite is null
         */
        public Add add(final int projectId, @NonNull Suite suite) {
            checkArgument(projectId > 0, "projectId should be positive");
            return new Add(projectId, suite);
        }

        /**
         * Updates an existing test suite. Partial updates are supported, i.e. you can set and update specific fields only.
         *
         * @param suite the test suite to be updated
         * @return the request
         * @throws java.lang.NullPointerException if suite is null
         */
        public Update update(@NonNull Suite suite) {
            return new Update(suite);
        }

        /**
         * Deletes an existing test suite.
         *
         * @param suiteId the ID of the test suite to be deleted
         * @return the request
         * @throws java.lang.IllegalArgumentException if suiteId is not positive
         */
        public Delete delete(final int suiteId) {
            checkArgument(suiteId > 0, "suiteId should be positive");
            return new Delete(suiteId);
        }

        public class Get extends Request<Suite> {
            private static final String REST_PATH = "get_suite/";

            private Get(int suiteId) {
                super(config, Method.GET, REST_PATH + suiteId, Suite.class);
            }
        }

        public class List extends Request<java.util.List<Suite>> {
            private static final String REST_PATH = "get_suites/";

            private List(int projectId) {
                super(config, Method.GET, REST_PATH + projectId, new TypeReference<java.util.List<Suite>>() {
                }, new TypeReference<Page<java.util.List<Suite>>>(){});
            }
        }

        public class Add extends Request<Suite> {
            private static final String REST_PATH = "add_suite/";

            private final Suite suite;

            private Add(final int projectId, Suite suite) {
                super(config, Method.POST, REST_PATH + projectId, Suite.class);
                this.suite = suite;
            }

            @Override
            protected Object getContent() {
                return suite;
            }
        }

        public class Update extends Request<Suite> {
            private static final String REST_PATH = "update_suite/";

            private final Suite suite;

            private Update(Suite suite) {
                super(config, Method.POST, REST_PATH + suite.getId(), Suite.class);
                this.suite = suite;
            }

            @Override
            protected Object getContent() {
                return suite;
            }
        }

        public class Delete extends Request<Void> {
            private static final String REST_PATH = "delete_suite/";

            private Delete(int suiteId) {
                super(config, Method.POST, REST_PATH + suiteId, Void.class);
            }
        }

    }

    /**
     * Request factories for "Tests".
     */
    @NoArgsConstructor
    public class Tests {

        /**
         * Returns an existing test.
         *
         * @param testId the ID of the test
         * @return the request
         * @throws java.lang.IllegalArgumentException if testId is not positive
         */
        public Get get(final int testId) {
            checkArgument(testId > 0, "testId should be positive");
            return new Get(testId);
        }

        /**
         * Returns a list of tests for a test run.
         *
         * @param runId the ID of the test run to get the tests for
         * @return the request
         * @throws java.lang.IllegalArgumentException if runId is not positive
         */
        public List list(final int runId) {
            checkArgument(runId > 0, "runId should be positive");
            return new List(runId);
        }

        public class Get extends Request<Test> {
            private static final String REST_PATH = "get_test/";

            private Get(int testId) {
                super(config, Method.GET, REST_PATH + testId, Test.class);
            }
        }

        @Getter
        @Setter
        @Accessors(fluent = true)
        public class List extends Request<java.util.List<Test>> {
            private static final String REST_PATH = "get_tests/";

            @JsonView(List.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> statusId;

            private List(int runId) {
                super(config, Method.GET, REST_PATH + runId, new TypeReference<java.util.List<Test>>() {
                }, new TypeReference<Page<java.util.List<Test>>>(){});
            }
        }

    }

    /**
     * Request factories for "Users".
     */
    @NoArgsConstructor
    public class Users {

        /**
         * Returns an existing user.
         *
         * @param userId the ID of the user
         * @return the request
         * @throws java.lang.IllegalArgumentException if userId is not positive
         */
        public Get get(final int userId) {
            checkArgument(userId > 0, "userId should be positive");
            return new Get(userId);
        }

        /**
         * Returns an existing user by his/her email address.
         *
         * @param email the email address to get the user for
         * @return the request
         * @throws java.lang.NullPointerException     if email is null
         * @throws java.lang.IllegalArgumentException if email is empty
         */
        public GetByEmail getByEmail(@NonNull String email) {
            email = email.trim();
            checkArgument(!email.isEmpty(), "email cannot be empty");
            return new GetByEmail(email);
        }

        /**
         * Returns a list of users.
         *
         * @return the request
         */
        public List list() {
            return new List();
        }

        public class Get extends Request<User> {
            private static final String REST_PATH = "get_user/";

            private Get(int userId) {
                super(config, Method.GET, REST_PATH + userId, User.class);
            }
        }

        public class GetByEmail extends Request<User> {
            private static final String REST_PATH = "get_user_by_email&email=";

            private GetByEmail(String email) {
                super(config, Method.GET, REST_PATH + email, User.class);
            }
        }

        public class List extends Request<java.util.List<User>> {
            private static final String REST_PATH = "get_users";

            private List() {
                super(config, Method.GET, REST_PATH, new TypeReference<java.util.List<User>>() {
                }, new TypeReference<Page<java.util.List<User>>>(){});
            }
        }

    }
}