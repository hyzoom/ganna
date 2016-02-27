package com.ishraq.janna.webservice;

import com.ishraq.janna.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ahmed on 7/5/15.
 */
public interface UserWebService {

    @GET("JsonInsertUsers.aspx")
    Call<List<User>> registerUser(@Query("userName") String name,
                              @Query("userPass") String pass,
                              @Query("userType") Integer type,
                              @Query("mobile") String mobile,
                              @Query("email") String email);

    @GET("JsonInsertUsers.aspx")
    Call<List<User>> getUser(@Query("userId") Integer userId);

    @GET("JsonCheckUsersExistance.aspx")
    Call<List<User.ExistUser>> loginUser(@Query("userPass") String password,
                               @Query("mobile") String mobile);
}
