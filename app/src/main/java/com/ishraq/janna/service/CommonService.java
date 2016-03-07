package com.ishraq.janna.service;

import android.content.Context;
import android.util.Log;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.database.DatabaseHelper;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.EventSponsor;
import com.ishraq.janna.model.Gst;
import com.ishraq.janna.model.Instructor;
import com.ishraq.janna.model.Lecture;
import com.ishraq.janna.model.LectureInstructor;
import com.ishraq.janna.model.Question;
import com.ishraq.janna.model.Rule;
import com.ishraq.janna.model.Session;
import com.ishraq.janna.model.Settings;
import com.ishraq.janna.model.Sponsor;
import com.ishraq.janna.model.User;
import com.j256.ormlite.dao.Dao;

import org.json.JSONObject;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Ahmed on 2/16/2016.
 */
public class CommonService {
    public Dao<Event, Integer> eventDao;

    public Dao<Rule, Integer> ruleDao;
    public Dao<Sponsor, Integer> sponsorDao;
    public Dao<EventSponsor, Integer> eventSponsorDao;
    public Dao<Lecture, Integer> lectureDao;
    public Dao<Instructor, Integer> instructorDao;
    public Dao<LectureInstructor, Integer> lectureInstructorDao;
    public Dao<Gst, Integer> gstDao;

    public Dao<Question, Integer> questionDao;
    public Dao<Settings, Integer> settingsDao;
    public Dao<User, Integer> userDao;
    public Dao<Session, Integer> sessionDao;

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public CommonService(Context context) {
        eventDao = DatabaseHelper.getHelper(context).getEventDao();
        ruleDao = DatabaseHelper.getHelper(context).getRuleDao();
        sponsorDao = DatabaseHelper.getHelper(context).getSponsorDao();
        eventSponsorDao = DatabaseHelper.getHelper(context).getEventSponsorDao();
        lectureDao = DatabaseHelper.getHelper(context).getLectureDao();
        instructorDao = DatabaseHelper.getHelper(context).getInstructorDao();
        lectureInstructorDao = DatabaseHelper.getHelper(context).getLectureInstructorDao();
        gstDao = DatabaseHelper.getHelper(context).getGstDao();
        questionDao = DatabaseHelper.getHelper(context).getQuestionDao();
        settingsDao = DatabaseHelper.getHelper(context).getSettingsDao();
        userDao = DatabaseHelper.getHelper(context).getUserDao();
        sessionDao = DatabaseHelper.getHelper(context).getSessionDao();
    }


    /**
     * @return Settings
     */
    public Settings getSettings() {
        Settings settings = null;
        List<Settings> list;
        try {
            list = settingsDao.queryForAll();
            if (list.size() == 1) {
                settings = list.get(0);
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return settings;
    }
}

