package com.ishraq.janna.webservice;

import com.ishraq.janna.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Ahmed on 7/5/15.
 */
public interface UserWebService {

    @GET("/user/{user_id}")
    Call<User> getUser(@Path("user_id") Integer userId);

//    @GET("/user/authenticate/{fb_user_id}/{fb_access_token}")
//    Call<User> logInUserWithFacebook(@Path("fb_user_id") String fbUserId, @Path("fb_access_token") String fbAccessToken);

//    @GET("JsonInsertUsers.aspx?username=ahmed&userPass={Ahmed}&phone={phone}&email={email}&address={address}&userType={type}&firstName={f_name}&LastName={l_name}&mobile={mobile}")
    @GET("JsonInsertUsers.aspx?username={name}&userPass={pass}&phone={phone}&email={email}&address={address}&userType={type}&mobile={mobile}")
    Call<User> logInUser(@Path("fb_access_token") String fbAccessToken);


    @GET("JsonInsertUsers.aspx?username={name}&userPass={pass}&phone={phone}&email={email}&address={address}&userType={type}&mobile={mobile}")
    Call<User> registerUser(@Path("fb_access_token") String fbAccessToken);

//    @GET("user/logout")
//    Call<Result> logoutUser();

}
