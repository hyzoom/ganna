package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ahmed on 3/18/2016.
 */
@DatabaseTable(tableName = "news")
public class News {
    @DatabaseField(id = true, columnName = "id")
    private Integer EventsNewsCode;

    @DatabaseField(columnName = "name_ar")
    private String EventsNewsNameAra;

    @DatabaseField(columnName = "name_lat")
    private String EventsNewsNameLat;

    @DatabaseField(columnName = "date")
    private String EventsNewsDate;

    @DatabaseField(columnName = "details")
    private String EventsNewsDetails;

    @DatabaseField(columnName = "image")
    private String EventsNewsImage;

    public Integer getEventsNewsCode() {
        return EventsNewsCode;
    }

    public void setEventsNewsCode(Integer eventsNewsCode) {
        EventsNewsCode = eventsNewsCode;
    }

    public String getEventsNewsNameAra() {
        return EventsNewsNameAra;
    }

    public void setEventsNewsNameAra(String eventsNewsNameAra) {
        EventsNewsNameAra = eventsNewsNameAra;
    }

    public String getEventsNewsNameLat() {
        return EventsNewsNameLat;
    }

    public void setEventsNewsNameLat(String eventsNewsNameLat) {
        EventsNewsNameLat = eventsNewsNameLat;
    }

    public String getEventsNewsDate() {
        return EventsNewsDate;
    }

    public void setEventsNewsDate(String eventsNewsDate) {
        EventsNewsDate = eventsNewsDate;
    }

    public String getEventsNewsDetails() {
        return EventsNewsDetails;
    }

    public void setEventsNewsDetails(String eventsNewsDetails) {
        EventsNewsDetails = eventsNewsDetails;
    }

    public String getImage() {
        return EventsNewsImage;
    }

    public void setImage(String EventsNewsImage) {
        this.EventsNewsImage = EventsNewsImage;
    }
}
