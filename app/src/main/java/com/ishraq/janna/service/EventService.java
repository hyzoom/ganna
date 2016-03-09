package com.ishraq.janna.service;

import android.content.Context;
import android.util.Log;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.model.Event;
import com.ishraq.janna.model.EventSponsor;
import com.ishraq.janna.model.Instructor;
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

//    /**
//     * save lectures to database
//     * @param lecture
//     * @return true if session is added successfully, false if not
//     */
//    public Boolean saveLecture(Lecturer lecturer) {
//        Boolean result = false;
//        try {
//            Dao.CreateOrUpdateStatus status = sessionDao.createOrUpdate(session);
//            if (status.isCreated() || status.isUpdated()) {
//                result = true;
//            }
//        } catch (SQLException e) {
//            Log.e(JannaApp.LOG_TAG, e.getMessage());
//        }
//        return result;
//    }


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
     * save session to database
     * @param session
     * @return true if session is added successfully, false if not
     */
    public Boolean saveSession(Session session) {
        Boolean result = false;


        try {
            Dao.CreateOrUpdateStatus status = sessionDao.createOrUpdate(session);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(JannaApp.LOG_TAG, e.getMessage());
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

    /**
     * save event to database
     * @param event
     * @return true if event is added successfully, false if not
     */
    public Boolean saveEvent(Event event) {
        Boolean result = false;

        // save rules
        if (event.getRules() != null) {
            List<Rule> rules = new ArrayList<Rule>(event.getRules());
            for (Rule rule : rules) {
                result = saveRule(rule);
            }

            if (result == false)
                return result;
        }

        // save sponsors
        if (event.getSponsers() != null) {
            List<Sponsor> sponsors = new ArrayList<Sponsor>(event.getSponsers());
            for (Sponsor sponsor : sponsors) {
                result = saveSponsor(sponsor);
            }
            if (result == false)
                return result;
        }

        // save sessions
        if (event.getSess() != null) {
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

        // save sessions
        if (event.getSess() != null) {
            List<Session> sessions = new ArrayList<Session>(event.getSess());
            for (Session session : sessions) {
                session.setEvent(event);
                result = saveSession(session);
            }
        }

        // save event sponsors
        if (event.getSponsers() != null) {
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


}
