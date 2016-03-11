package com.ishraq.janna.service;

import android.content.Context;
import android.util.Log;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.EventSponsor;
import com.ishraq.janna.model.Guest;
import com.ishraq.janna.model.Instructor;
import com.ishraq.janna.model.Lecture;
import com.ishraq.janna.model.LectureGuest;
import com.ishraq.janna.model.LectureInstructor;
import com.ishraq.janna.model.Rule;
import com.ishraq.janna.model.Session;
import com.ishraq.janna.model.Sponsor;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 2/27/2016.
 */
public class EventService extends CommonService {
    public EventService(Context context) {
        super(context);
    }

    /**
     * save rule to database
     * @param rule
     * @return true if session is added successfully, false if not
     */
    public Boolean saveRule(Rule rule) {
        Boolean result = false;
        try {
            Dao.CreateOrUpdateStatus status = ruleDao.createOrUpdate(rule);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return result;
    }


    //////////////////////////////////////// Sponsor //////////////////////////////////////

    /**
     *
     * @return EventSponsor
     */
    public List<EventSponsor> getEventSponsors(Event event) {
        List<EventSponsor> eventSponsors = null;
        try {
            QueryBuilder<EventSponsor, Integer> queryBuilder = eventSponsorDao.queryBuilder();

            queryBuilder.where().eq("event_id", event.getEventCode());
            queryBuilder.where().eq("sponsor_id", event.getEventCode());
            eventSponsors = queryBuilder.query();

        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return eventSponsors;
    }

    /**
     * save sponsor to database
     * @param sponsor
     * @return true if sponsor is added successfully, false if not
     */
    public Boolean saveSponsor(Sponsor sponsor) {
        Boolean result = false;
        try {
            Dao.CreateOrUpdateStatus status = sponsorDao.createOrUpdate(sponsor);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return result;
    }

    /**
     * save eventSponsor to database
     * @param eventSponsor
     * @return true if eventSponsor is added successfully, false if not
     */
    public Boolean saveEventSponsor(EventSponsor eventSponsor) {
        Boolean result = false;
        try {
            Dao.CreateOrUpdateStatus status = eventSponsorDao.createOrUpdate(eventSponsor);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return result;
    }

    /**
     * save event sponsor to database
     * @param event
     * @param sponsors
     * @return true if eventSponsor is added successfully, false if not
     */
    public Boolean saveEventSponsors(Event event, List<Sponsor> sponsors) {
        // Remove all saved event sponsor if exist
        Event tempEvent = getEvent(event.getEventCode());
        if (tempEvent != null) {
            try {
                eventSponsorDao.delete(tempEvent.getEventSponsors());
            } catch (SQLException e) {
                Log.e(JannaApp.LOG_TAG, e.getMessage());
            }
        }

        Boolean result = false;
        for (Sponsor sponsor : sponsors) {
            EventSponsor eventSponsor = new EventSponsor();
            eventSponsor.setEvent(event);
            eventSponsor.setSponsor(sponsor);
            result = saveEventSponsor(eventSponsor);
        }
        return result;
    }






    ///////////////////////////////////////// Instructor /////////////////////////////////////


    /**
     * save instructor to database
     * @param instructor
     * @return true if instructor is added successfully, false if not
     */
    public Boolean saveInstructor(Instructor instructor) {
        Boolean result = false;
        try {
            Dao.CreateOrUpdateStatus status = instructorDao.createOrUpdate(instructor);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return result;
    }

    /**
     * save lectureInstructor to database
     * @param lectureInstructor
     * @return true if lectureInstructor is added successfully, false if not
     */
    public Boolean saveLectureInstructor(LectureInstructor lectureInstructor) {
        Boolean result = false;
        try {
            Dao.CreateOrUpdateStatus status = lectureInstructorDao.createOrUpdate(lectureInstructor);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return result;
    }

    /**
     * save event sponsor to database
     * @param lecture
     * @param instructors
     * @return true if lectureInstructor is added successfully, false if not
     */
    public Boolean saveLectureInstructors(Lecture lecture, List<Instructor> instructors) {
        // Remove all saved lecture instructors if exist
        Lecture tempLecture = getLecture(lecture.getEventsLectureCode());
        if (tempLecture != null) {
            try {
                lectureInstructorDao.delete(tempLecture.getLectureInstructors());
            } catch (SQLException e) {
                Log.e(JannaApp.LOG_TAG, e.getMessage());
            }
        }

        Boolean result = true;
        for (Instructor instructor : instructors) {
            LectureInstructor lectureInstructor = new LectureInstructor();
            lectureInstructor.setLecture(lecture);
            lectureInstructor.setInstructor(instructor);
            result = saveLectureInstructor(lectureInstructor);
        }
        return result;
    }





    /////////////////////////////////////// Guest ///////////////////////////////////////////////

    /**
     * save guest to database
     * @param guest
     * @return true if guest is added successfully, false if not
     */
    public Boolean saveGuest(Guest guest) {
        Boolean result = false;
        try {
            Dao.CreateOrUpdateStatus status = gstDao.createOrUpdate(guest);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return result;
    }


    /**
     * save lectureGuest to database
     * @param lectureGuest
     * @return true if lectureGuest is added successfully, false if not
     */
    public Boolean saveLectureGuest(LectureGuest lectureGuest) {
        Boolean result = false;
        try {
            Dao.CreateOrUpdateStatus status = lectureGuestDao.createOrUpdate(lectureGuest);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return result;
    }

    /**
     * save event sponsor to database
     * @param lecture
     * @param guests
     * @return true if lectureGuests is added successfully, false if not
     */
    public Boolean saveLectureGuests(Lecture lecture, List<Guest> guests) {
        // Remove all saved lecture guests if exist
        Lecture tempLecture = getLecture(lecture.getEventsLectureCode());
        if (tempLecture != null) {
            try {
                lectureGuestDao.delete(tempLecture.getLectureGuests());
            } catch (SQLException e) {
                Log.e(JannaApp.LOG_TAG, e.getMessage());
            }
        }

        Boolean result = true;
        for (Guest guest : guests) {
            LectureGuest lectureGuest = new LectureGuest();
            lectureGuest.setLecture(lecture);
            lectureGuest.setGuest(guest);
            result = saveLectureGuest(lectureGuest);
        }
        return result;
    }






    /////////////////////////////////////// Lecture /////////////////////////////////////////////

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


    /**
     * save lecture to database
     * @param lecture
     * @return true if lecture is added successfully, false if not
     */
    public Boolean saveLecture(Lecture lecture) {
        Boolean result = false;

        // save instructors
        if (lecture.getInst() != null && lecture.getInst().size() > 0) {
            List<Instructor> instructors = new ArrayList<Instructor>(lecture.getInst());
            for (Instructor instructor : instructors) {
                result = saveInstructor(instructor);
            }
            if (result == false)
                return result;
        }

        // save guests
        if (lecture.getGst() != null && lecture.getGst().size() > 0) {
            List<Guest> guests = new ArrayList<Guest>(lecture.getGst());
            for (Guest guest : guests) {
                result = saveGuest(guest);
            }
            if (result == false)
                return result;
        }

        try {
            Dao.CreateOrUpdateStatus status = lectureDao.createOrUpdate(lecture);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }

        // save lecture instructors
        if (lecture.getInst() != null && lecture.getInst().size() > 0) {
            List<Instructor> instructors = new ArrayList<Instructor>(lecture.getInst());
            result = saveLectureInstructors(lecture, instructors);
        }

        // save lecture guests
        if (lecture.getGst() != null && lecture.getGst().size() > 0) {
            List<Guest> guests = new ArrayList<Guest>(lecture.getGst());
            result = saveLectureGuests(lecture, guests);
        }


        return result;
    }





    /////////////////////////////////////////// Session //////////////////////////////////////


    /**
     * save session to database
     * @param session
     * @return true if session is added successfully, false if not
     */
    public Boolean saveSession(Session session) {
        Boolean result = false;

        // save lectures
        if (session.getLect() != null && session.getLect().size() > 0) {
            List<Lecture> lectures = new ArrayList<Lecture>(session.getLect());
            for (Lecture lecture : lectures) {
                result = saveLecture(lecture);
            }
            if (result == false)
                return result;
        }

        try {
            Dao.CreateOrUpdateStatus status = sessionDao.createOrUpdate(session);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }

        // save lectures
        if (session.getLect() != null && session.getLect().size() > 0) {
            List<Lecture> lectures = new ArrayList<Lecture>(session.getLect());
            for (Lecture lecture : lectures) {
                lecture.setSession(session);
                result = saveLecture(lecture);
            }
        }

        return result;
    }

    public Boolean saveSessions(List<Session> sessions) {
        Boolean result = false;
        for (Session session : sessions) {
            result = saveSession(session);
        }
        return result;
    }




    ///////////////////////////////////////// Event ////////////////////////////////////////

    /**
     *
     * @param eventId
     * @return Recipe
     */
    public Event getEvent(Integer eventId) {
        Event event = null;
        try {
            List<Event> events = eventDao.queryForEq("id", eventId);
            if (events.size() > 0) {
                event = events.get(0);
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return event;
    }

    /**
     *
     * @return Events
     */
    public List<Event> getEvents() {
        List<Event> events = null;
        try {
            events = eventDao.queryForAll();
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }
        return events;
    }

    /**
     * save event to database
     * @param event
     * @return true if event is added successfully, false if not
     */
    public Boolean saveEvent(Event event) {
        Boolean result = false;

        // save rules
        if (event.getRules() != null && event.getRules().size() > 0) {
            List<Rule> rules = new ArrayList<Rule>(event.getRules());
            for (Rule rule : rules) {
                result = saveRule(rule);
            }

            if (result == false)
                return result;
        }

        // save sponsors
        if (event.getSponsers() != null && event.getSponsers().size() > 0) {
            List<Sponsor> sponsors = new ArrayList<Sponsor>(event.getSponsers());
            for (Sponsor sponsor : sponsors) {
                result = saveSponsor(sponsor);
            }
            if (result == false)
                return result;
        }

        // save sessions
        if (event.getSess() != null && event.getSess().size() > 0) {
            List<Session> sessions = new ArrayList<Session>(event.getSess());
            for (Session session : sessions) {
                result = saveSession(session);
            }
            if (result == false)
                return result;
        }

        try {
            Dao.CreateOrUpdateStatus status = eventDao.createOrUpdate(event);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
        }

        if (result == false)
            return result;

        // save rules
        if (event.getRules() != null && event.getRules().size() > 0) {
            List<Rule> rules = new ArrayList<Rule>(event.getRules());
            for (Rule rule : rules) {
                rule.setEvent(event);
                result = saveRule(rule);
            }
        }
        // save sessions
        if (event.getSess() != null && event.getSess().size() > 0) {
            List<Session> sessions = new ArrayList<Session>(event.getSess());
            for (Session session : sessions) {
                session.setEvent(event);
                result = saveSession(session);
            }
        }

        // save event sponsors
        if (event.getSponsers() != null && event.getSponsers().size() > 0) {
            List<Sponsor> sponsors = new ArrayList<Sponsor>(event.getSponsers());
            result = saveEventSponsors(event, sponsors);
        }

        return result;
    }

    public Boolean saveEvents(List<Event> events) {
        Boolean result = false;
        for (Event event : events) {
            result = saveEvent(event);
        }
        return result;
    }


}
