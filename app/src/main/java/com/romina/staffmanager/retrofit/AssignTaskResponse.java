package com.romina.staffmanager.retrofit;

import java.io.Serializable;

/**
 * Created by Abhilash on 02-08-2017
 */

public class AssignTaskResponse implements Serializable{
    private boolean success;
    private String data;

    public boolean isSuccess() {
        return success;
    }

    public String getData() {
        return data;
    }
}
