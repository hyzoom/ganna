package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created by Ahmed on 3/5/2016.
 */
@DatabaseTable(tableName = "session")
public class Session {

    @DatabaseField(id = true, columnName = "id")
    private Integer EventsSessionCode;

    @DatabaseField(columnName = "name_ar")
    private String EventsSessionNameAra;

    @DatabaseField(columnName = "name_lat")
    private String EventsSessionNameLat;

    @DatabaseField(columnName = "date")
    private String EventsSessionDate;

    @DatabaseField(columnName = "details")
    private String EventsSessionDetails;

    @DatabaseField(columnName = "information")
    private String EventsSessionInformations;

    @ForeignCollectionField(eager = true)
    private Collection<Lecture> lect;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "event_id")
    private Event event;

    //////////////////////////////////////////////////////////////////////////////////////////

    public Integer getEventsSessionCode() {
        return EventsSessionCode;
    }

    public void setEventsSessionCode(Integer eventsSessionCode) {
        EventsSessionCode = eventsSessionCode;
    }

    public String getEventsSessionNameAra() {
        return EventsSessionNameAra;
    }

    public void setEventsSessionNameAra(String eventsSessionNameAra) {
        EventsSessionNameAra = eventsSessionNameAra;
    }

    public String getEventsSessionDate() {
        return EventsSessionDate;
    }

    public void setEventsSessionDate(String eventsSessionDate) {
        EventsSessionDate = eventsSessionDate;
    }

    public String getEventsSessionDetails() {
        return EventsSessionDetails;
    }

    public void setEventsSessionDetails(String eventsSessionDetails) {
        EventsSessionDetails = eventsSessionDetails;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getEventsSessionNameLat() {
        return EventsSessionNameLat;
    }

    public void setEventsSessionNameLat(String eventsSessionNameLat) {
        EventsSessionNameLat = eventsSessionNameLat;
    }

    public String getEventsSessionInformations() {
        return EventsSessionInformations;
    }

    public void setEventsSessionInformations(String eventsSessionInformations) {
        EventsSessionInformations = eventsSessionInformations;
    }

    public Collection<Lecture> getLect() {
        return lect;
    }

    public void setLect(Collection<Lecture> lect) {
        this.lect = lect;
    }
}
