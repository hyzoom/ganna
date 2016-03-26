package com.ishraq.janna.service;

import android.content.Context;
import android.util.Log;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.Survey;
import com.ishraq.janna.model.SurveyAnswer;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 3/19/2016.
 */
public class SurveyService extends CommonService {
    public SurveyService(Context context) {
        super(context);
    }


    /**
     *
     * @return SurveyAnswer
     */
    public List<SurveyAnswer> getSurveyAnswers(Integer surveyId) {
        List<SurveyAnswer> surveyAnswers = null;
        try {
            surveyAnswers = answerDao.queryForEq("survey_id", surveyId);
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return surveyAnswers;
    }

    /**
     *
     * @return Surveys
     */
    public List<Survey> getSurveys() {
        List<Survey> surveys = null;
        try {
            surveys = surveyDao.queryForAll();

            for (int i = 0; i < surveys.size(); i++) {
                surveys.get(i).setAnswers(getSurveyAnswers(surveys.get(i).getSurveyCode()));
            }

        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return surveys;
    }


    /**
     * save answer to database
     * @param answer
     * @return true if answer is added successfully, false if not
     */
    public Boolean saveSurveyAnswer(SurveyAnswer answer) {
        Boolean result = false;

        try {
            Dao.CreateOrUpdateStatus status = answerDao.createOrUpdate(answer);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return result;
    }

    public Boolean saveSurveyAnswers(List<SurveyAnswer> answers) {
        Boolean result = false;
        for (SurveyAnswer surveyAnswer : answers) {
            result = saveSurveyAnswer(surveyAnswer);
        }
        return result;
    }


    /**
     * save survey to database
     * @param survey
     * @return true if survey is added successfully, false if not
     */
    public Boolean saveSurvey(Survey survey) {
        Boolean result = false;

        // Save answers
        if (survey.getAnswers() != null && survey.getAnswers().size() > 0) {
            result = saveSurveyAnswers(new ArrayList<SurveyAnswer>(survey.getAnswers()));

            if (result == false)
                return result;
        }

        try {
            Dao.CreateOrUpdateStatus status = surveyDao.createOrUpdate(survey);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }

        return result;
    }


    public Boolean saveSurveys(List<Survey> surveys) {
        Boolean result = false;
        for (Survey survey : surveys) {
            result = saveSurvey(survey);
        }
        return result;
    }

}
