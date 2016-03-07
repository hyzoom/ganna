package com.ishraq.janna.service;

import android.content.Context;
import android.util.Log;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.Session;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 2/27/2016.
 */
public class EventService extends CommonService {
    public EventService(Context context) {
        super(context);
    }

//    /**
//     * save lectures to database
//     * @param lecture
//     * @return true if session is added successfully, false if not
//     */
//    public Boolean saveLecture(Lecturer lecturer) {
//        Boolean result = false;
//        try {
//            Dao.CreateOrUpdateStatus status = sessionDao.createOrUpdate(session);
//            if (status.isCreated() || status.isUpdated()) {
//                result = true;
//            }
//        } catch (SQLException e) {
//            Log.e(JannaApp.LOG_TAG, e.getMessage());
//        }
//        return result;
//    }


    /**
     * save session to database
     * @param session
     * @return true if session is added successfully, false if not
     */
    public Boolean saveSession(Session session) {
        Boolean result = false;
        try {
            Dao.CreateOrUpdateStatus status = sessionDao.createOrUpdate(session);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return result;
    }

    public Boolean saveSessions(List<Session> sessions) {
        Boolean result = false;
        for (Session session : sessions) {
            result = saveSession(session);
        }
        return result;
    }


    /**
     * save event to database
     * @param event
     * @return true if event is added successfully, false if not
     */
    public Boolean saveEvent(Event event) {
        Boolean result = false;
        try {
            Dao.CreateOrUpdateStatus status = eventDao.createOrUpdate(event);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }

        if (result == false)
            return result;

        // save sessions
        if (event.getSess() != null) {
            List<Session> sessions = new ArrayList<Session>(event.getSess());
            for (Session session : sessions) {
                session.setEvent(event);
                result = saveSession(session);
            }
        }


        return result;
    }

    public Boolean saveEvents(List<Event> events) {
        Boolean result = false;
        for (Event event : events) {
            result = saveEvent(event);
        }
        return result;
    }


    /**
     *
     * @return Events
     */
    public List<Event> getEvents() {
        List<Event> events = null;
        try {
            events = eventDao.queryForAll();
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return events;
    }
}
