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

    // Lecturer
    @DatabaseField
    private String InstructorName;

    @DatabaseField
    private String EventsLectureDate;

    @DatabaseField
    private String lecturesImageUrl;

    @ForeignCollectionField(eager = true, columnName = "lecture_instructor")
    private Collection<LectureInstructor> lectureInstructors;

    @ForeignCollectionField(eager = true, columnName = "lecture_guest")
    private Collection<LectureGuest> lectureGuests;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "session_id")
    private Session session;


    private Collection<Instructor> inst;

    @ForeignCollectionField(eager = true, columnName = "guest")
    private Collection<Guest> Gst;


    ///////////////////////////////////////////////////////////////////////////////////////////

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

    public Collection<LectureInstructor> getLectureInstructors() {
        return lectureInstructors;
    }

    public void setLectureInstructors(Collection<LectureInstructor> lectureInstructors) {
        this.lectureInstructors = lectureInstructors;
    }

    public Collection<LectureGuest> getLectureGuests() {
        return lectureGuests;
    }

    public void setLectureGuests(Collection<LectureGuest> lectureGuests) {
        this.lectureGuests = lectureGuests;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Collection<Instructor> getInst() {
        return inst;
    }

    public void setInst(Collection<Instructor> inst) {
        this.inst = inst;
    }

    public Collection<Guest> getGst() {
        return Gst;
    }

    public void setGst(Collection<Guest> gst) {
        Gst = gst;
    }

    public String getImage() {
        return lecturesImageUrl;
    }

    public void setImage(String lecturesImageUrl) {
        this.lecturesImageUrl = lecturesImageUrl;
    }

    public String getInstructorName() {
        return InstructorName;
    }

    public void setInstructorName(String instructorName) {
        InstructorName = instructorName;
    }

    public String getEventsLectureDate() {
        return EventsLectureDate;
    }

    public void setEventsLectureDate(String eventsLectureDate) {
        EventsLectureDate = eventsLectureDate;
    }
}
