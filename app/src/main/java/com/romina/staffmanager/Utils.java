package com.romina.staffmanager;

import com.cloudinary.Cloudinary;
import com.romina.staffmanager.retrofit.Api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Abhilash on 06-07-2017
 */

public class Utils {
    public static final String tag="StaffManagerRomina";
    //private static final String BASE_URL="http://192.168.137.1:3000";
    private static final String BASE_URL="https://staff-manager-romina.herokuapp.com";
    private static Api service=null;
    public static Api getRetrofitService(){
        if(service==null){
            HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .readTimeout(30,TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            service=retrofit.create(Api.class);
        }
        return service;
    }

    private static Cloudinary cloudinary;

    public static Cloudinary getCloudinary(){
        if(cloudinary==null){
            cloudinary = new Cloudinary("cloudinary://754364979657771:6-cQ4VLqnAzqaZih_xDf6_3F8Pw@staff-manager");
        }
        return cloudinary;
    }

    public static final String SHARED_PREFERENCE="shared_preference";
    public static final String FIRST_NAME="first_name";
    public static final String LAST_NAME="last_name";
    public static final String USERNAME="username";
    public static final String POSITION="position";
    public static final String USER_TYPE="type";

    public static String getTimeStamp(){
        SimpleDateFormat s=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        return s.format(new Date());
    }
}
