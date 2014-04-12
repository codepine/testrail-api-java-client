package com.cymbocha.apis.testrail;

import com.cymbocha.apis.testrail.model.*;
import lombok.*;

/**
 * @author kms
 */
@AllArgsConstructor
public class TestRail {
    
    private final TestRailConfig config;

    public Projects projects() {
        return new Projects();
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
                super(config, Method.GET, REST_PATH, List.<java.util.List<Project>>responseType());
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
                return new Object() {
                    @Delegate(types = {View.Project.Add.class})
                    private Project delegate = project;
                };
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
                return new Object() {
                    @Delegate(types = {View.Project.Update.class})
                    private Project delegate = project;
                };
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

    @NoArgsConstructor
    public class CaseFields {

        public List list() {
            return new List();
        }

        public class List extends Request<java.util.List<CaseField>> {
            private static final String REST_PATH = "get_case_fields";

            private List() {
                super(config, Method.GET, REST_PATH, List.<java.util.List<CaseField>>responseType());
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
                super(config, Method.GET, REST_PATH, List.<java.util.List<CaseType>>responseType());
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
                super(config, Method.GET, REST_PATH + project.getId(), List.<java.util.List<Configuration>>responseType());
            }

        }

    }
}