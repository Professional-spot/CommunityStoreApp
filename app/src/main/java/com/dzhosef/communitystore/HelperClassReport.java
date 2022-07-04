package com.dzhosef.communitystore;


public class HelperClassReport {
    String uid_reporter, user_id_reported,task;


    public HelperClassReport( String uid_reporter,String user_id_reported, String task){

        this.uid_reporter =uid_reporter;
        this.user_id_reported =user_id_reported;
        this.task=task;

    }


    public String getUid() {
        return uid_reporter;
    }
    public void setUid(String uid) {
        this.uid_reporter = uid;
    }

    public String getUserid() {
        return user_id_reported;
    }
    public void setUserid(String userid) {
        this.user_id_reported = userid;
    }

    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
    }



}
