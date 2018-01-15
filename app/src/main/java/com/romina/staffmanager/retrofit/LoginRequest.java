package com.romina.staffmanager.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 06-07-2017
 */

public class LoginRequest implements Serializable {
    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
