package com.ishraq.janna.webservice;

import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.News;
import com.ishraq.janna.model.Question;
import com.ishraq.janna.model.Session;
import com.ishraq.janna.model.User;

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

    @GET("JsonInsertQuestions.aspx")
    Call<List<Event.QuestionResult>> sendEventQuestion(@Query("eventid") Integer eventId,
                                        @Query("userid") Integer userId,
                                        @Query("question") String question);


    @GET("JsonViewQuestions.aspx")
    Call<List<Question>> getEventQuestions(@Query("id") Integer eventId);


    @GET("JsonViewDoctors.aspx")
    Call<List<User>> getAllAttendants();


    @GET("JsonViewNews.aspx")
    Call<List<News>> getAllNewses(@Query("id") Integer eventId);
}
