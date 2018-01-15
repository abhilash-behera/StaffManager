package com.romina.staffmanager.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 22-07-2017
 */

public class Task implements Serializable{
    private String task_name;
    private String video_url;
    private String task_duration;
    private String task_status;
    private String assigned_to;
    private String notification_duration;
    private String created_on;
    private String assigned_time="";

    public String getAssigned_time() {
        return assigned_time;
    }

    public Task(String task_name, String video_url, String task_duration, String task_status, String assigned_to, String notification_duration, String created_on) {
        this.task_name = task_name;
        this.video_url = video_url;
        this.task_duration = task_duration;
        this.task_status = task_status;
        this.assigned_to = assigned_to;
        this.notification_duration = notification_duration;
        this.created_on = created_on;
    }

    public String getTask_name() {

        return task_name;
    }

    public String getVideo_url() {
        return video_url;
    }

    public String getTask_duration() {
        return task_duration;
    }

    public String getTask_status() {
        return task_status;
    }

    public String getAssigned_to() {
        return assigned_to;
    }

    public String getNotification_duration() {
        return notification_duration;
    }

    public String getCreated_on() {
        return created_on;
    }
}
