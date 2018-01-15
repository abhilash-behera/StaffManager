package com.romina.staffmanager.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 13-07-2017
 */

public class Staff implements Serializable {
    private String first_name;
    private String last_name;
    private String position;
    private int pending_tasks;
    private String username;

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPosition() {
        return position;
    }

    public int getPending_tasks() {
        return pending_tasks;
    }

    public String getUsername(){
        return username;
    }
}
