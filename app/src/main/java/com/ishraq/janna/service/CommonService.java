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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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

    public static JSONObject makeHttpRequest(String url, String method,
                                             List<NameValuePair> params) {
        // Making HTTP request
        try {

            // check for request method
            if (method == "POST") {
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } else if (method == "GET") {
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.e("JSON Parser", "json" + sb);
//            json = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

}

