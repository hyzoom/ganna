package com.ishraq.janna.webservice;

import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.Session;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ahmed on 2/27/2016.
 */
public interface EventWebService {

    @GET("JsonViewEvents.aspx")
    Call<List<Event>> getAllEvents();

    @GET("JsonViewEvents.aspx")
    Call<List<Event>> getEvent(@Query("id") Integer eventId);

    @GET("JsonViewSessions.aspx")
    Call<List<Session>> getSession(@Query("id") Integer eventId,
                                   @Query("sessionId") Integer sessionId);
}
