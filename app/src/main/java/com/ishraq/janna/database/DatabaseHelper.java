package com.ishraq.janna.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.EventGuest;
import com.ishraq.janna.model.EventImage;
import com.ishraq.janna.model.EventLecturer;
import com.ishraq.janna.model.Lecturer;
import com.ishraq.janna.model.News;
import com.ishraq.janna.model.NewsGuest;
import com.ishraq.janna.model.NewsImage;
import com.ishraq.janna.model.Question;
import com.ishraq.janna.model.Session;
import com.ishraq.janna.model.Settings;
import com.ishraq.janna.model.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Ahmed on 2/16/2016.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "janna.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    private Dao<Event, Integer> eventDao = null;
    private Dao<EventLecturer, Integer> eventLecturerDao = null;
    private Dao<EventGuest, Integer> eventGuestDao = null;
    private Dao<EventImage, Integer> eventImageDao = null;
    private Dao<Lecturer, Integer> lecturerDao = null;
    private Dao<News, Integer> newsDao = null;
    private Dao<NewsImage, Integer> newsImageDao = null;
    private Dao<NewsGuest, Integer> newsGuestDao = null;
    private Dao<Question, Integer> questionDao = null;
    private Dao<Settings, Integer> settingsDao = null;
    private Dao<User, Integer> userDao = null;
    private Dao<Session, Integer> sessionDao = null;

    // we do this so there is only one helper
    private static DatabaseHelper helper = null;
    private static final AtomicInteger usageCounter = new AtomicInteger(0);


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Get the helper, possibly constructing it if necessary. For each call to this method, there should be 1 and only 1
     * call to {@link #close()}.
     */
    public static synchronized DatabaseHelper getHelper(Context context) {
        if (helper == null) {
            helper = new DatabaseHelper(context);
        }
        usageCounter.incrementAndGet();
        return helper;
    }


    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(JannaApp.LOG_TAG, "onCreate");
            TableUtils.createTable(connectionSource, Event.class);
            TableUtils.createTable(connectionSource, EventImage.class);
            TableUtils.createTable(connectionSource, EventLecturer.class);
            TableUtils.createTable(connectionSource, EventGuest.class);
            TableUtils.createTable(connectionSource, Lecturer.class);
            TableUtils.createTable(connectionSource, News.class);
            TableUtils.createTable(connectionSource, NewsImage.class);
            TableUtils.createTable(connectionSource, NewsGuest.class);
            TableUtils.createTable(connectionSource, Question.class);
            TableUtils.createTable(connectionSource, Settings.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Session.class);

            initData();
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.i(JannaApp.LOG_TAG, "onUpgrade");
    }


    private void initData(){
        Log.d(JannaApp.LOG_TAG, "data initiating");

        Settings settings = new Settings();
        settings.setId(1);
        try {
            settingsDao.create(settings);
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, "Data init2 ERROR");
        }
    }

    public Dao<Event, Integer> getEventDao() {
        if (eventDao == null) {
            try {
                eventDao = getDao(Event.class);
            } catch (SQLException e) {
            }
        }
        return eventDao;
    }

    public Dao<EventImage, Integer> getEventImageDao() {
        if (eventImageDao == null) {
            try {
                eventImageDao = getDao(EventImage.class);
            } catch (SQLException e) {
            }
        }
        return eventImageDao;
    }

    public Dao<EventLecturer, Integer> getEventLecturerDao() {
        if (eventLecturerDao == null) {
            try {
                eventLecturerDao = getDao(EventLecturer.class);
            } catch (SQLException e) {
            }
        }
        return eventLecturerDao;
    }

    public Dao<EventGuest, Integer> getEventGuestDao() {
        if (eventGuestDao == null) {
            try {
                eventGuestDao = getDao(EventGuest.class);
            } catch (SQLException e) {
            }
        }
        return eventGuestDao;
    }

    public Dao<Lecturer, Integer> getLecturerDao() {
        if (lecturerDao == null) {
            try {
                lecturerDao = getDao(Lecturer.class);
            } catch (SQLException e) {
            }
        }
        return lecturerDao;
    }

    public Dao<News, Integer> getNewsDao() {
        if (newsDao == null) {
            try {
                newsDao = getDao(News.class);
            } catch (SQLException e) {
            }
        }
        return newsDao;
    }

    public Dao<NewsImage, Integer> getNewsImageDao() {
        if (newsImageDao == null) {
            try {
                newsImageDao = getDao(NewsImage.class);
            } catch (SQLException e) {
            }
        }
        return newsImageDao;
    }

    public Dao<NewsGuest, Integer> getNewsGuestDao() {
        if (newsGuestDao == null) {
            try {
                newsGuestDao = getDao(NewsGuest.class);
            } catch (SQLException e) {
            }
        }
        return newsGuestDao;
    }

    public Dao<Question, Integer> getQuestionDao() {
        if (questionDao == null) {
            try {
                questionDao = getDao(Question.class);
            } catch (SQLException e) {
            }
        }
        return questionDao;
    }

    public Dao<Settings, Integer> getSettingsDao() {
        if (settingsDao == null) {
            try {
                settingsDao = getDao(Settings.class);
            } catch (SQLException e) {
            }
        }
        return settingsDao;
    }

    public Dao<User, Integer> getUserDao() {
        if (userDao == null) {
            try {
                userDao = getDao(User.class);
            } catch (SQLException e) {
            }
        }
        return userDao;
    }

    public Dao<Session, Integer> getSessionDao() {
        if (sessionDao == null) {
            try {
                sessionDao = getDao(Session.class);
            } catch (SQLException e) {
            }
        }
        return sessionDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();

        eventDao = null;
        eventImageDao = null;
        eventLecturerDao = null;
        lecturerDao = null;
        newsDao = null;
        newsImageDao = null;
        newsGuestDao = null;
        questionDao = null;
        settingsDao = null;
        userDao = null;
        sessionDao = null;
    }
}
