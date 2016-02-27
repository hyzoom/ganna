package com.ishraq.janna.service;

import android.content.Context;
import android.util.Log;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.model.User;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by Ahmed on 2/27/2016.
 */
public class UserService extends CommonService{
    public UserService(Context context) {
        super(context);
    }

    /**
     * save user to database and create new record for profileImage
     * @param user
     * @return true if user is added successfully, false if not
     */
    public Boolean saveUser(User user) {
        Boolean result = false;
        try {
            Dao.CreateOrUpdateStatus status = userDao.createOrUpdate(user);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return result;
    }

    /**
     * get user with id
     * @param userId
     * @return
     */
    public User getUser(Integer userId) {
        User user = null;
        try {
            user = userDao.queryForId(userId);
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return user;
    }
}
