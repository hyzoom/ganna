package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created by Ahmed on 3/19/2016.
 */
@DatabaseTable(tableName = "survey")
public class Survey {

    @DatabaseField(id = true, columnName = "id")
    private Integer SurveyCode;

    @DatabaseField(columnName = "name")
    private String SurveyNameAra;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "event_id")
    private Event EventCode;


    @ForeignCollectionField(eager = true)
    private Collection<SurveyAnswer> answers;


    //////////////////////////////////////////////////////////////////////////////////////////


    public Integer getSurveyCode() {
        return SurveyCode;
    }

    public void setSurveyCode(Integer surveyCode) {
        SurveyCode = surveyCode;
    }

    public String getSurveyNameAra() {
        return SurveyNameAra;
    }

    public void setSurveyNameAra(String surveyNameAra) {
        SurveyNameAra = surveyNameAra;
    }

    public Event getEvent() {
        return EventCode;
    }

    public void setEvent(Event EventCode) {
        this.EventCode = EventCode;
    }

    public Collection<SurveyAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(Collection<SurveyAnswer> answers) {
        this.answers = answers;
    }
}
