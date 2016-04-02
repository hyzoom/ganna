package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ahmed on 3/7/2016.
 */
@DatabaseTable(tableName = "guest")
public class Guest {
    @DatabaseField(id = true, columnName = "id")
    private Integer GuestsCode;

    @DatabaseField(columnName = "name_ar")
    private String GuestsCodeNameAra;

    @DatabaseField(columnName = "name_en")
    private String GuestsCodeNameLat;

    @DatabaseField(columnName = "image")
    private String GuestsImageUrl;

    @DatabaseField(columnName = "notes")
    private String Notes;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "lecture_id")
    private Lecture lecture;




    public Integer getGuestsCode() {
        return GuestsCode;
    }

    public void setGuestsCode(Integer guestsCode) {
        GuestsCode = guestsCode;
    }

    public String getGuestsCodeNameAra() {
        return GuestsCodeNameAra;
    }

    public void setGuestsCodeNameAra(String guestsCodeNameAra) {
        GuestsCodeNameAra = guestsCodeNameAra;
    }

    public String getGuestsCodeNameLat() {
        return GuestsCodeNameLat;
    }

    public void setGuestsCodeNameLat(String guestsCodeNameLat) {
        GuestsCodeNameLat = guestsCodeNameLat;
    }

    public String getGuestsImageUrl() {
        return GuestsImageUrl;
    }

    public void setGuestsImageUrl(String guestsImageUrl) {
        GuestsImageUrl = guestsImageUrl;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}
