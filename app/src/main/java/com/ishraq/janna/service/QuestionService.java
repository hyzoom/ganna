package com.ishraq.janna.service;

import android.content.Context;
import android.util.Log;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.model.Question;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Ahmed on 3/25/2016.
 */
public class QuestionService extends CommonService {

    public QuestionService(Context context) {
        super(context);
    }


    /**
     *
     * @return Question
     */
    public List<Question> getQuestions() {
        List<Question> questions = null;
        try {
            questions = questionDao.queryForAll();
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return questions;
    }



    /**
     * save Question to database
     * @param question
     * @return true if answer is added successfully, false if not
     */
    public Boolean saveQuestion(Question question) {
        Boolean result = false;

        try {
            Dao.CreateOrUpdateStatus status = questionDao.createOrUpdate(question);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return result;
    }

    public Boolean saveQuestions(List<Question> questions) {
        Boolean result = false;
        for (Question question : questions) {
            result = saveQuestion(question);
        }
        return result;
    }
}
