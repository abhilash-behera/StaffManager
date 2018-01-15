package com.romina.staffmanager.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 11-07-2017.
 */

public class CreateStaffResponse implements Serializable{
    private boolean status;
    private String data;

    public boolean isStatus() {
        return status;
    }

    public String getData() {
        return data;
    }
}
