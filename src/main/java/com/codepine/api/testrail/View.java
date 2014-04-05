package com.cymbocha.apis.testrail;

/**
 * Created by kunal546 on 4/5/14.
 */
class View {

    static class Project {
        interface Add {
            String getName();
            String getAnnouncement();
            boolean isShowAnnouncement();
        }
        interface Update {
            String getName();
            String getAnnouncement();
            boolean isShowAnnouncement();
            boolean isCompleted();
        }
    }
}
