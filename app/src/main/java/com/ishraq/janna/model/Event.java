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

    @DatabaseField(id = true, columnName = "id")
    private Integer EventCode;

    @DatabaseField(columnName = "name_ar")
    private String EventNameAra;

    @DatabaseField(columnName = "name_en")
    private String EventNameEng;

    @DatabaseField(columnName = "start_date")
    private String EventStartDate;

    @DatabaseField(columnName = "end_date")
    private String EventEndDate;

    @DatabaseField(columnName = "structure")
    private String EventStructure;

    @DatabaseField(columnName = "address")
    private String EventAddress;

    @DatabaseField(columnName = "location")
    private String EventLocation;

    @DatabaseField(columnName = "notes")
    private String Notes;

    @ForeignCollectionField(eager = true)
    private Collection<Rule> Rules;

    @ForeignCollectionField
    private Collection<EventSponsor> eventSponsors;

    @ForeignCollectionField(eager = true)
    private Collection<Session> sess;

    private Collection<Sponsor> Sponsers;

    ////////////////////////////////////////////// /////////////////////////////////////////////

    public Integer getEventCode() {
        return EventCode;
    }

    public void setEventCode(Integer eventCode) {
        EventCode = eventCode;
    }

    public String getEventNameAra() {
        return EventNameAra;
    }

    public void setEventNameAra(String eventNameAra) {
        EventNameAra = eventNameAra;
    }

    public String getEventNameEng() {
        return EventNameEng;
    }

    public void setEventNameEng(String eventNameEng) {
        EventNameEng = eventNameEng;
    }

    public String getEventStartDate() {
        return EventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        EventStartDate = eventStartDate;
    }

    public String getEventEndDate() {
        return EventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        EventEndDate = eventEndDate;
    }

    public String getEventStructure() {
        return EventStructure;
    }

    public void setEventStructure(String eventStructure) {
        EventStructure = eventStructure;
    }

    public String getEventAddress() {
        return EventAddress;
    }

    public void setEventAddress(String eventAddress) {
        EventAddress = eventAddress;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public Collection<Session> getSess() {
        return sess;
    }

    public void setSess(Collection<Session> sess) {
        this.sess = sess;
    }

    public String getEventLocation() {
        return EventLocation;
    }

    public void setEventLocation(String eventLocation) {
        EventLocation = eventLocation;
    }

    public Collection<Rule> getRules() {
        return Rules;
    }

    public void setRules(Collection<Rule> rules) {
        Rules = rules;
    }

    public Collection<EventSponsor> getEventSponsors() {
        return eventSponsors;
    }

    public void setEventSponsors(Collection<EventSponsor> eventSponsors) {
        this.eventSponsors = eventSponsors;
    }

    public Collection<Sponsor> getSponsers() {
        return Sponsers;
    }

    public void setSponsers(Collection<Sponsor> sponsers) {
        Sponsers = sponsers;
    }


//    @DatabaseField(id = true)
//    private Integer id;
//
//    @DatabaseField
//    private String name;
//
//    @DatabaseField
//    private String details;
//
//    @DatabaseField
//    private String date;
//
//    @DatabaseField
//    private String address;
//
//    @DatabaseField
//    private double longitude;
//
//    @DatabaseField
//    private double latitude;
//
//    @DatabaseField
//    private String cover;
//
//    @DatabaseField
//    private boolean liked;
//
//    @DatabaseField
//    private String certificate;


//    @ForeignCollectionField(eager = true)
//    private Collection<EventImage> eventImages;
//
//    @ForeignCollectionField(eager = false)
//    private Collection<EventLecturer> lecturers;
//
//    @ForeignCollectionField(eager = false)
//    private Collection<EventGuest> guest;
//
//    @ForeignCollectionField(eager = false)
//    private Collection<News> newses;
//
//    @ForeignCollectionField(eager = false)
//    private Collection<Question> questions;

}
