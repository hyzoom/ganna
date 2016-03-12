package com.ishraq.janna.service;

import android.content.Context;
import android.util.Log;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.model.Lecture;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Ahmed on 3/11/2016.
 */
public class LectureService extends CommonService {
    public LectureService(Context context) {
        super(context);
    }


    /**
     *
     * @param lectureId
     * @return Lecture
     */
    public Lecture getLecture(Integer lectureId) {
        Lecture lecture = null;
        try {
            List<Lecture> lectures = lectureDao.queryForEq("id", lectureId);
            if (lectures.size() > 0) {
                lecture = lectures.get(0);
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return lecture;
    }
}
