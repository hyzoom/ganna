package com.ishraq.janna.webservice;

import com.ishraq.janna.model.Session;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ahmed on 3/11/2016.
 */
public interface SessionWebService {

    @GET("JsonViewSessions.aspx")
    Call<List<Session>> getSession(@Query("id") Integer eventId,
                                   @Query("sessionId") Integer sessionId);
}
