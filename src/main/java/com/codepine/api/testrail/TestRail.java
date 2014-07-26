package com.cymbocha.apis.testrail;

import com.cymbocha.apis.testrail.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author kms
 */
@AllArgsConstructor
public class TestRail {

    private final TestRailConfig config;

    public Projects projects() {
        return new Projects();
    }

    public Cases cases() {
        return new Cases();
    }

    public CaseFields caseFields() {
        return new CaseFields();
    }

    public CaseTypes caseTypes() {
        return new CaseTypes();
    }

    public Configurations configurations() {
        return new Configurations();
    }

    public Sections sections() {
        return new Sections();
    }

    public Suites suites() {
        return new Suites();
    }

    public Milestones milestones() {
        return new Milestones();
    }

    public Priorities priorities() {
        return new Priorities();
    }

    public ResultFields resultFields() {
        return new ResultFields();
    }

    public Tests tests() {
        return new Tests();
    }

    public Users users() {
        return new Users();
    }

    public Statuses statuses() {
        return new Statuses();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Projects {

        public Get get(@NonNull Project project) {
            return new Get(project);
        }

        public List list() {
            return new List();
        }

        public Add add(@NonNull Project project) {
            return new Add(project);
        }

        public Update update(@NonNull Project project) {
            return new Update(project);
        }

        public Delete delete(@NonNull Project project) {
            return new Delete(project);
        }

        public class Get extends Request<Project> {
            private static final String REST_PATH = "get_project/";

            private Get(@NonNull Project project) {
                super(config, Method.GET, REST_PATH + project.getId(), Project.class);
            }
        }

        public class List extends Request<java.util.List<Project>> {
            private static final String REST_PATH = "get_projects";

            private List() {
                super(config, Method.GET, REST_PATH, new TypeReference<java.util.List<Project>>() {
                });
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

            private final Project project;

            private Delete(@NonNull Project project) {
                super(config, Method.POST, REST_PATH + project.getId(), Void.class);
                this.project = project;
            }

        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Cases {

        public Get get(@NonNull Case testCase) {
            return new Get(testCase);
        }

        public List list(@NonNull Project project, @NonNull Suite suite) {
            return list(project, suite, null);
        }

        public List list(@NonNull Project project, @NonNull Suite suite, Section section) {
            return new List(project, suite, section);
        }

        public Add add(@NonNull Case testCase) {
            return new Add(testCase);
        }

        public Update update(@NonNull Case testCase) {
            return new Update(testCase);
        }

        public Delete delete(@NonNull Case testCase) {
            return new Delete(testCase);
        }

        public class Get extends Request<Case> {
            private static final String REST_PATH = "get_case/";

            private Get(Case testCase) {
                super(config, Method.GET, REST_PATH + testCase.getId(), Case.class);
            }
        }

        public class List extends Request<java.util.List<Case>> {
            private static final String REST_PATH = "get_cases/%s&suite_id=%s&section_id=%s";

            private List(Project project, Suite suite, Section section) {
                super(config, Method.GET, String.format(REST_PATH, project.getId(), suite.getId(), section == null ? "" : section.getId()), new TypeReference<java.util.List<Case>>() {
                });
            }
        }

        public class Add extends Request<Case> {
            private static final String REST_PATH = "add_case/";

            private final Case testCase;

            private Add(Case testCase) {
                super(config, Method.POST, REST_PATH + testCase.getSectionId(), Case.class);
                this.testCase = testCase;
            }

            @Override
            protected Object getContent() {
                return testCase;
            }

        }

        public class Update extends Request<Case> {
            private static final String REST_PATH = "update_case/";

            private final Case testCase;

            private Update(Case testCase) {
                super(config, Method.POST, REST_PATH + testCase.getId(), Case.class);
                this.testCase = testCase;
            }

            @Override
            protected Object getContent() {
                return testCase;
            }

        }

        public class Delete extends Request<Void> {
            private static final String REST_PATH = "delete_case/";

            private Delete(Case testCase) {
                super(config, Method.POST, REST_PATH + testCase.getId(), Void.class);
            }
        }
    }

    @NoArgsConstructor
    public class CaseFields {

        public List list() {
            return new List();
        }

        public class List extends Request<java.util.List<CaseField>> {
            private static final String REST_PATH = "get_case_fields";

            private List() {
                super(config, Method.GET, REST_PATH, new TypeReference<java.util.List<CaseField>>() {
                });
            }
        }
    }

    @NoArgsConstructor
    public class CaseTypes {

        public List list() {
            return new List();
        }

        public class List extends Request<java.util.List<CaseType>> {
            private static final String REST_PATH = "get_case_types";

            private List() {
                super(config, Method.GET, REST_PATH, new TypeReference<java.util.List<CaseType>>() {
                });
            }
        }

    }

    @NoArgsConstructor
    public class Configurations {

        public List list(@NonNull Project project) {
            return new List(project);
        }

        public class List extends Request<java.util.List<Configuration>> {
            private static final String REST_PATH = "get_configs/";

            private List(Project project) {
                super(config, Method.GET, REST_PATH + project.getId(), new TypeReference<java.util.List<Configuration>>() {
                });
            }

        }

    }

    @NoArgsConstructor
    public class Milestones {

        public Get get(@NonNull Milestone milestone) {
            return new Get(milestone);
        }

        public List list(@NonNull Project project) {
            return new List(project);
        }

        public Add add(@NonNull Milestone milestone) {
            return new Add(milestone);
        }

        public Update update(@NonNull Milestone milestone) {
            return new Update(milestone);
        }

        public Delete delete(@NonNull Milestone milestone) {
            return new Delete(milestone);
        }

        public class Get extends Request<Milestone> {
            private static final String REST_PATH = "get_milestone/";

            private Get(Milestone milestone) {
                super(config, Method.GET, REST_PATH + milestone.getId(), Milestone.class);
            }
        }

        public class List extends Request<java.util.List<Milestone>> {
            private static final String REST_PATH = "get_milestones/";

            private List(Project project) {
                super(config, Method.GET, REST_PATH + project.getId(), new TypeReference<java.util.List<Milestone>>() {
                });
            }
        }

        public class Add extends Request<Milestone> {
            private static final String REST_PATH = "add_milestone/";

            private final Milestone milestone;

            private Add(Milestone milestone) {
                super(config, Method.POST, REST_PATH + milestone.getProjectId(), Milestone.class);
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

            private Delete(Milestone milestone) {
                super(config, Method.POST, REST_PATH + milestone.getId(), Void.class);
            }
        }

    }

    @NoArgsConstructor
    public class Priorities {

        public List list() {
            return new List();
        }

        public class List extends Request<java.util.List<Priority>> {
            private static final String REST_PATH = "get_priorities";

            private List() {
                super(config, Method.GET, REST_PATH, new TypeReference<java.util.List<Priority>>() {
                });
            }
        }

    }

    @NoArgsConstructor
    public class ResultFields {

        public List list() {
            return new List();
        }

        public class List extends Request<java.util.List<ResultField>> {
            private static final String REST_PATH = "get_result_fields";

            private List() {
                super(config, Method.GET, REST_PATH, new TypeReference<java.util.List<ResultField>>() {
                });
            }
        }
    }

    @NoArgsConstructor
    public class Sections {

        public Get get(@NonNull Section section) {
            return new Get(section);
        }

        public List list(@NonNull Project project, Suite suite) {
            return new List(project, suite);
        }

        public Add add(@NonNull Project project, @NonNull Section section) {
            return new Add(project, section);
        }

        public Update update(@NonNull Section section) {
            return new Update(section);
        }

        public Delete delete(@NonNull Section section) {
            return new Delete(section);
        }

        public class Get extends Request<Section> {
            private static final String REST_PATH = "get_section/";

            private Get(Section section) {
                super(config, Method.GET, REST_PATH + section.getId(), Section.class);
            }

        }

        public class List extends Request<java.util.List<Section>> {
            private static final String REST_PATH = "get_sections/%s&suite_id=%s";

            private List(Project project, Suite suite) {
                super(config, Method.GET, String.format(REST_PATH, project.getId(), suite.getId()), new TypeReference<java.util.List<Section>>() {
                });
            }
        }

        public class Add extends Request<Section> {
            private static final String REST_PATH = "add_section/";

            private final Section section;

            private Add(Project project, Section section) {
                super(config, Method.POST, REST_PATH + project.getId(), Section.class);
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

            private Delete(Section section) {
                super(config, Method.POST, REST_PATH + section.getId(), Void.class);
            }
        }
    }

    @NoArgsConstructor
    public class Statuses {

        public List list() {
            return new List();
        }

        public class List extends Request<java.util.List<Status>> {
            private static final String REST_PATH = "get_statuses";

            private List() {
                super(config, Method.GET, REST_PATH, new TypeReference<java.util.List<Status>>() {
                });
            }
        }

    }

    @NoArgsConstructor
    public class Suites {

        public Get get(@NonNull Suite suite) {
            return new Get(suite);
        }

        public List list(@NonNull Project project) {
            return new List(project);
        }

        public Add add(@NonNull Suite suite) {
            return new Add(suite);
        }

        public Update update(@NonNull Suite suite) {
            return new Update(suite);
        }

        public Delete delete(@NonNull Suite suite) {
            return new Delete(suite);
        }

        public class Get extends Request<Suite> {
            private static final String REST_PATH = "get_suite/";

            private Get(Suite suite) {
                super(config, Method.GET, REST_PATH + suite.getId(), Suite.class);
            }
        }

        public class List extends Request<java.util.List<Suite>> {
            private static final String REST_PATH = "get_suites/";

            private List(Project project) {
                super(config, Method.GET, REST_PATH + project.getId(), new TypeReference<java.util.List<Suite>>() {
                });
            }
        }

        public class Add extends Request<Suite> {
            private static final String REST_PATH = "add_suite/";

            private final Suite suite;

            private Add(Suite suite) {
                super(config, Method.POST, REST_PATH + suite.getProjectId(), Suite.class);
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

            private Delete(Suite suite) {
                super(config, Method.POST, REST_PATH + suite.getId(), Void.class);
            }
        }

    }

    @NoArgsConstructor
    public class Tests {

        public Get get(@NonNull Test test) {
            return new Get(test);
        }

        public List list(@NonNull Run run) {
            return new List(run);
        }

        public class Get extends Request<Test> {
            private static final String REST_PATH = "get_test/";

            private Get(Test test) {
                super(config, Method.GET, REST_PATH + test.getId(), Test.class);
            }
        }

        public class List extends Request<java.util.List<Test>> {
            private static final String REST_PATH = "get_tests/";

            private List(Run run) {
                super(config, Method.GET, REST_PATH + run.getId(), new TypeReference<java.util.List<Test>>() {
                });
            }
        }

    }

    @NoArgsConstructor
    public class Users {

        public Get get(@NonNull User user) {
            return new Get(user);
        }

        public GetByEmail getByEmail(@NonNull User user) {
            return new GetByEmail(user);
        }

        public List list() {
            return new List();
        }

        public class Get extends Request<User> {
            private static final String REST_PATH = "get_user/";

            private Get(User user) {
                super(config, Method.GET, REST_PATH + user.getId(), User.class);
            }
        }

        public class GetByEmail extends Request<User> {
            private static final String REST_PATH = "get_user_by_email&email=";

            private GetByEmail(User user) {
                super(config, Method.GET, REST_PATH + user.getEmail(), User.class);
            }
        }

        public class List extends Request<java.util.List<User>> {
            private static final String REST_PATH = "get_users";

            private List() {
                super(config, Method.GET, REST_PATH, new TypeReference<java.util.List<User>>() {
                });
            }
        }

    }
}