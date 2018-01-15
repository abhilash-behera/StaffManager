package com.romina.staffmanager.retrofit;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Abhilash on 13-07-2017
 */

public class StaffListResponse implements Serializable{
    private boolean success;
    private List<Staff> data;

    public boolean isSuccess() {
        return success;
    }

    public List<Staff> getData() {
        return data;
    }
}
