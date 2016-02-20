package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created by Ahmed on 2/16/2016.
 */
@DatabaseTable(tableName = "event")
public class Event {
    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String details;

    @DatabaseField
    private String date;

    @DatabaseField
    private String address;

    @DatabaseField
    private double longitude;

    @DatabaseField
    private double latitude;

    @DatabaseField
    private String cover;

    @DatabaseField
    private boolean liked;

    @DatabaseField
    private String certificate;


    @ForeignCollectionField(eager = true)
    private Collection<EventImage> eventImages;

    @ForeignCollectionField(eager = false)
    private Collection<EventLecturer> lecturers;

    @ForeignCollectionField(eager = false)
    private Collection<EventGuest> guest;

    @ForeignCollectionField(eager = false)
    private Collection<News> newses;

    @ForeignCollectionField(eager = false)
    private Collection<Question> questions;



    ///////////////////////////////////////////////////////////////////////////////////


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public Collection<EventImage> getEventImages() {
        return eventImages;
    }

    public void setEventImages(Collection<EventImage> eventImages) {
        this.eventImages = eventImages;
    }

    public Collection<EventLecturer> getLecturers() {
        return lecturers;
    }

    public void setLecturers(Collection<EventLecturer> lecturers) {
        this.lecturers = lecturers;
    }

    public Collection<EventGuest> getGuest() {
        return guest;
    }

    public void setGuest(Collection<EventGuest> guest) {
        this.guest = guest;
    }

    public Collection<News> getNewses() {
        return newses;
    }

    public void setNewses(Collection<News> newses) {
        this.newses = newses;
    }

    public Collection<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Collection<Question> questions) {
        this.questions = questions;
    }

}
