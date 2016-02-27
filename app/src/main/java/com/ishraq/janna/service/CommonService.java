package com.ishraq.janna.service;

import android.content.Context;
import android.util.Log;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.database.DatabaseHelper;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.EventGuest;
import com.ishraq.janna.model.EventImage;
import com.ishraq.janna.model.EventLecturer;
import com.ishraq.janna.model.Lecturer;
import com.ishraq.janna.model.News;
import com.ishraq.janna.model.NewsGuest;
import com.ishraq.janna.model.NewsImage;
import com.ishraq.janna.model.Question;
import com.ishraq.janna.model.Settings;
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
    public Dao<EventImage, Integer> eventImageDao;
    public Dao<EventLecturer, Integer> eventLecturerDao;
    public Dao<EventGuest, Integer> eventGuestDao;
    public Dao<Lecturer, Integer> lecturerDao;
    public Dao<News, Integer> newsDao;
    public Dao<NewsImage, Integer> newsImageDao;
    public Dao<NewsGuest, Integer> newsGuestDao;
    public Dao<Question, Integer> questionDao;
    public Dao<Settings, Integer> settingsDao;
    public Dao<User, Integer> userDao;

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public CommonService(Context context) {
        eventDao = DatabaseHelper.getHelper(context).getEventDao();
        eventImageDao = DatabaseHelper.getHelper(context).getEventImageDao();
        eventLecturerDao = DatabaseHelper.getHelper(context).getEventLecturerDao();
        eventGuestDao = DatabaseHelper.getHelper(context).getEventGuestDao();
        lecturerDao = DatabaseHelper.getHelper(context).getLecturerDao();
        newsDao = DatabaseHelper.getHelper(context).getNewsDao();
        newsImageDao = DatabaseHelper.getHelper(context).getNewsImageDao();
        newsGuestDao = DatabaseHelper.getHelper(context).getNewsGuestDao();
        questionDao = DatabaseHelper.getHelper(context).getQuestionDao();
        settingsDao = DatabaseHelper.getHelper(context).getSettingsDao();
        userDao = DatabaseHelper.getHelper(context).getUserDao();
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

