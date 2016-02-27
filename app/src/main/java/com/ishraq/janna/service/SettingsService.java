package com.ishraq.janna.service;

import android.content.Context;
import android.util.Log;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.model.Settings;
import com.ishraq.janna.model.User;

import java.sql.SQLException;

/**
 * Created by Ahmed on 2/16/2016.
 */
public class SettingsService extends CommonService{

    public SettingsService(Context context) {
        super(context);
    }
    /**
     * user logged out
     */
    public Boolean setUserLoggedOut() {
        Settings settings = getSettings();

        settings.setLoggedInUser(null);
        return updateSettings(settings);
    }

    /**
     *
     * @param settings
     * @return true if settings are updated successfully, false if not
     */
    public Boolean updateSettings(Settings settings) {
        Boolean result = false;
        try {
            if(settingsDao.update(settings) == 1) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return result;
    }

    /**
     * set logged in user
     * @param loggedInUser
     */
    public Boolean setLoggedInUser(User loggedInUser) {

        Settings settings = getSettings();
        if (settings == null) {
            settings = new Settings();
            settings.setId(1);
        }
        settings.setLoggedInUser(loggedInUser);
        return updateSettings(settings);
    }


}
