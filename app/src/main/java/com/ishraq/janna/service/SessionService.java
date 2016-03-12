package com.ishraq.janna.service;

import android.content.Context;
import android.util.Log;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.Session;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Ahmed on 3/11/2016.
 */
public class SessionService extends CommonService {
    public SessionService(Context context) {
        super(context);
    }


    /**
     *
     * @param sessionId
     * @return Session
     */
    public Session getSession(Integer sessionId) {
        Session session = null;
        try {
            List<Session> sessions = sessionDao.queryForEq("id", sessionId);
            if (sessions.size() > 0) {
                session = sessions.get(0);
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return session;
    }


}
