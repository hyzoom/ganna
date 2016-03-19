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

}
