package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created by Ahmed on 3/7/2016.
 */
@DatabaseTable(tableName = "lecture")
public class Lecture {
    @DatabaseField(id = true, columnName = "id")
    private Integer EventsLectureCode;

    @DatabaseField
    private String EventsLectureNameAra;

    @DatabaseField
    private String EventsLectureNameLat;

    @DatabaseField
    private String EventsLectureDetails;

    @ForeignCollectionField(eager = true)
    private Collection<LectureInstructor> inst;

//    @ForeignCollectionField(eager = true)
//    private Collection<Gst> Gst;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "session_id")
    private Session session;


    public Integer getEventsLectureCode() {
        return EventsLectureCode;
    }

    public void setEventsLectureCode(Integer eventsLectureCode) {
        EventsLectureCode = eventsLectureCode;
    }

    public String getEventsLectureNameAra() {
        return EventsLectureNameAra;
    }

    public void setEventsLectureNameAra(String eventsLectureNameAra) {
        EventsLectureNameAra = eventsLectureNameAra;
    }

    public String getEventsLectureNameLat() {
        return EventsLectureNameLat;
    }

    public void setEventsLectureNameLat(String eventsLectureNameLat) {
        EventsLectureNameLat = eventsLectureNameLat;
    }

    public String getEventsLectureDetails() {
        return EventsLectureDetails;
    }

    public void setEventsLectureDetails(String eventsLectureDetails) {
        EventsLectureDetails = eventsLectureDetails;
    }

    public Collection<LectureInstructor> getInst() {
        return inst;
    }

    public void setInst(Collection<LectureInstructor> inst) {
        this.inst = inst;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
