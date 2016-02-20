package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created by Ahmed on 2/16/2016.
 */
@DatabaseTable(tableName = "news")
public class News {
    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String date;

    @DatabaseField
    private String details;

    @DatabaseField
    private String cover;

    @ForeignCollectionField(eager = true)
    private Collection<NewsImage> newsImages;

    @ForeignCollectionField(eager = true)
    private Collection<NewsGuest> guest;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "event_id")
    private Event event;


    ////////////////////////////////////////////////////////////////////////////////////////


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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Collection<NewsImage> getNewsImages() {
        return newsImages;
    }

    public void setNewsImages(Collection<NewsImage> newsImages) {
        this.newsImages = newsImages;
    }

    public Collection<NewsGuest> getGuest() {
        return guest;
    }

    public void setGuest(Collection<NewsGuest> guest) {
        this.guest = guest;
    }
}
