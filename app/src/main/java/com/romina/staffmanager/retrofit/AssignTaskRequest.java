package com.romina.staffmanager.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 02-08-2017
 */

public class AssignTaskRequest implements Serializable {
    private String username;
    private String created_on;
    private String timestamp;

    public AssignTaskRequest(String username, String created_on, String timestamp) {
        this.username = username;
        this.created_on = created_on;
        this.timestamp = timestamp;
    }

}
