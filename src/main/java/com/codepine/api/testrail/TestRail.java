package com.cymbocha.apis.testrail;

import com.cymbocha.apis.testrail.model.Project;
import com.google.common.base.Preconditions;
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

        public Get get(int id) {
            return new Get(id);
        }

        public class Get extends Request<Project> {
            private static final String REST_PATH = "get_project/";

            private Get(int id) {
                super(config, Method.GET, REST_PATH + id, Project.class);
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

        public Update update(Project project) {
            project = Preconditions.checkNotNull(project, "project cannot be null");
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
                abstract class ContentView {
                    abstract String getName();
                    abstract String getAnnouncement();
                    abstract boolean isShowCompleted();
                    abstract boolean isCompleted();
                }

                return new Object() {
                    @Delegate(types = {ContentView.class})
                    Project delegate = project;
                };
            }

        }
    }
}
