package com.romina.staffmanager.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 11-07-2017
 */

public class CreateStaffRequest implements Serializable {
    private String first_name;
    private String last_name;
    private String username;
    private String position;
    private String type;
    private String password;

    public CreateStaffRequest(String first_name, String last_name, String username, String position, String type, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.position = position;
        this.type = type;
        this.password = password;
    }
}
