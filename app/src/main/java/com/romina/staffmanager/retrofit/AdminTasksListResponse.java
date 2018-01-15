package com.romina.staffmanager.retrofit;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Abhilash on 22-07-2017
 */

public class AdminTasksListResponse implements Serializable {
    private boolean success;
    private List<Task> data;

    public boolean isSuccess() {
        return success;
    }

    public List<Task> getData() {
        return data;
    }
}
