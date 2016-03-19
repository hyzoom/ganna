package com.ishraq.janna.service;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ishraq.janna.JannaApp;
import com.ishraq.janna.database.DatabaseHelper;
import com.ishraq.janna.model.Booking;
import com.ishraq.janna.model.Clinic;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.EventSponsor;
import com.ishraq.janna.model.Guest;
import com.ishraq.janna.model.Instructor;
import com.ishraq.janna.model.Lecture;
import com.ishraq.janna.model.LectureGuest;
import com.ishraq.janna.model.LectureInstructor;
import com.ishraq.janna.model.News;
import com.ishraq.janna.model.Question;
import com.ishraq.janna.model.Rule;
import com.ishraq.janna.model.Session;
import com.ishraq.janna.model.Settings;
import com.ishraq.janna.model.Specialization;
import com.ishraq.janna.model.Sponsor;
import com.ishraq.janna.model.Survey;
import com.ishraq.janna.model.SurveyAnswer;
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

    public Dao<Guest, Integer> gstDao;
    public Dao<LectureGuest, Integer> lectureGuestDao;

    public Dao<Question, Integer> questionDao;
    public Dao<Settings, Integer> settingsDao;
    public Dao<User, Integer> userDao;
    public Dao<Session, Integer> sessionDao;

    public Dao<Booking, Integer> bookingDao;
    public Dao<Clinic, Integer> clinicDao;
    public Dao<Specialization, Integer> specializationDao;
    public Dao<News, Integer> newsDao;
    public Dao<Survey, Integer> surveyDao;
    public Dao<SurveyAnswer, Integer> answerDao;


    public CommonService(Context context) {
        eventDao = DatabaseHelper.getHelper(context).getEventDao();
        ruleDao = DatabaseHelper.getHelper(context).getRuleDao();
        sponsorDao = DatabaseHelper.getHelper(context).getSponsorDao();
        eventSponsorDao = DatabaseHelper.getHelper(context).getEventSponsorDao();
        lectureDao = DatabaseHelper.getHelper(context).getLectureDao();
        instructorDao = DatabaseHelper.getHelper(context).getInstructorDao();
        lectureInstructorDao = DatabaseHelper.getHelper(context).getLectureInstructorDao();
        gstDao = DatabaseHelper.getHelper(context).getGstDao();
        lectureGuestDao = DatabaseHelper.getHelper(context).getLectureGuestDao();
        questionDao = DatabaseHelper.getHelper(context).getQuestionDao();
        settingsDao = DatabaseHelper.getHelper(context).getSettingsDao();
        userDao = DatabaseHelper.getHelper(context).getUserDao();
        sessionDao = DatabaseHelper.getHelper(context).getSessionDao();
        bookingDao = DatabaseHelper.getHelper(context).getBookingDao();
        clinicDao = DatabaseHelper.getHelper(context).getClinicDao();
        specializationDao = DatabaseHelper.getHelper(context).getSpecializationDao();
        newsDao = DatabaseHelper.getHelper(context).getNewsDao();
        surveyDao = DatabaseHelper.getHelper(context).getSurveyDao();
        answerDao = DatabaseHelper.getHelper(context).getSurveyAnswersDao();

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



    /**
     * display image with Glide into imageView
     * @param imageUrl
     * @param imageView
     */
    public void displayImage(String imageUrl, ImageView imageView) {
        Glide.with(JannaApp.getContext()).load(imageUrl).thumbnail(0.01f)
                .crossFade().into(imageView);
    }
}

