package com.cymbocha.apis.testrail;

import com.cymbocha.apis.testrail.model.Project;
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

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Projects {

        public Get get(@NonNull Project project) {
            return new Get(project);
        }

        public class Get extends Request<Project> {
            private static final String REST_PATH = "get_project/";

            private Get(@NonNull Project project) {
                super(config, Method.GET, REST_PATH + project.getId(), Project.class);
            }
        }

        public List list() {
            return new List();
        }

        public class List extends Request<java.util.List<Project>> {
            private static final String REST_PATH = "get_projects";

            private List() {
                super(config, Method.GET, REST_PATH, List.<java.util.List<Project>>responseType());
            }
        }

        public Add add(@NonNull Project project) {
            return new Add(project);
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

        public Update update(@NonNull Project project) {
            return new Update(project);
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

        public Delete delete(@NonNull Project project) {
            return new Delete(project);
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
}
