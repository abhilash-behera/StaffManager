package com.romina.staffmanager.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Abhilash on 06-07-2017
 */

public interface Api {
    @Headers("Content-type: application/json")
    @POST("/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @Headers("Content-type:application/json")
    @POST("/createStaff")
    Call<CreateStaffResponse> createStaff(@Body CreateStaffRequest createStaffRequest);

    @Headers("Content-type:application/json")
    @POST("/usernameAvailability")
    Call<UsernameAvailabilityResponse> checkUsernameAvailability(@Body UsernameAvailablityRequest usernameAvailablityRequest);

    @Headers("Content-type:application/json")
    @GET("/staffList")
    Call<StaffListResponse> getStaffList();

    @Headers("Content-type:application/json")
    @POST("/createTask")
    Call<CreateTaskResponse> createTask(@Body Task task);

    @Headers("Content-type:application/json")
    @GET("/adminTasksList")
    Call<AdminTasksListResponse> getAdminTasksList();

    @Headers("Content-type:application/json")
    @POST("/assignTask")
    Call<AssignTaskResponse> assignTask(@Body AssignTaskRequest assignTaskRequest);
}
