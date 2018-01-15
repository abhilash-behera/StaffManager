package com.romina.staffmanager.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 11-07-2017
 */

public class UsernameAvailablityRequest implements Serializable {
    private String username;

    public UsernameAvailablityRequest(String username) {
        this.username = username;
    }
}
