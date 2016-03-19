package com.ishraq.janna.service;

import android.content.Context;
import android.util.Log;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.model.News;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Ahmed on 3/19/2016.
 */
public class NewsService extends CommonService {
    public NewsService(Context context) {
        super(context);
    }


    /**
     *
     * @param newsId
     * @return News
     */
    public News getNews(Integer newsId) {
        News news = null;
        try {
            List<News> newses = newsDao.queryForEq("id", newsId);
            if (newses.size() > 0) {
                news = newses.get(0);
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return news;
    }

    /**
     *
     * @return Newses
     */
    public List<News> getNewses() {
        List<News> newses = null;
        try {
            newses = newsDao.queryForAll();
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return newses;
    }


    /**
     * save news to database
     * @param news
     * @return true if news is added successfully, false if not
     */
    public Boolean saveNews(News news) {
        Boolean result = false;
        try {
            Dao.CreateOrUpdateStatus status = newsDao.createOrUpdate(news);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return result;
    }

    public Boolean saveNewses(List<News> newses) {
        Boolean result = false;
        for (News news : newses) {
            result = saveNews(news);
        }
        return result;
    }

}
