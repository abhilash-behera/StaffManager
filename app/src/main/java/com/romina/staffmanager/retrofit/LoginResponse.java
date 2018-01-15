package com.romina.staffmanager.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 06-07-2017
 */

public class LoginResponse implements Serializable {
    public boolean success;
    public datum data;
    public class datum{
        public String first_name;
        public String last_name;
        public String username;
        public String type;
        public String position;
    }
}

