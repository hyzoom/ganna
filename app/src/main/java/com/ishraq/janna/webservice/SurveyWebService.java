package com.ishraq.janna.webservice;

import com.ishraq.janna.model.Survey;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ahmed on 3/19/2016.
 */
public interface SurveyWebService {

    @GET("JsonViewSurvey.aspx?id=1")
    Call<List<Survey>> getAllSurveyQuestions();
//    Call<List<Survey>> getAllSurveyQuestions(@Query("id") Integer eventId);

    @GET("JsonViewSurvey.aspx")
    Call<List<Survey>> sendAllSurveyQuestions(@Query("event_id") Integer eventId,
                                              @Query("user_id") Integer userId,
                                              @Query("question_id") Integer questionId,
                                              @Query("result_id") Integer resultId);

}
