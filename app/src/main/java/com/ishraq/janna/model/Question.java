package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ahmed on 2/16/2016.
 */
@DatabaseTable(tableName = "question")
public class Question {
    @DatabaseField(id = true, columnName = "id")
    private Integer EventQuestionCode;

    @DatabaseField
    private String EventQuestion;

    @DatabaseField
    private String EventQuestionDate;

    @DatabaseField
    private String Notes;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "event_id")
    private Event EventCode;



    ///////////////////////////////////////////////////////////////


    public Integer getEventQuestionCode() {
        return EventQuestionCode;
    }

    public void setEventQuestionCode(Integer eventQuestionCode) {
        EventQuestionCode = eventQuestionCode;
    }

    public String getEventQuestion() {
        return EventQuestion;
    }

    public void setEventQuestion(String eventQuestion) {
        EventQuestion = eventQuestion;
    }


    public String getEventQuestionDate() {
        return EventQuestionDate;
    }

    public void setEventQuestionDate(String eventQuestionDate) {
        EventQuestionDate = eventQuestionDate;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public Event getEventCode() {
        return EventCode;
    }

    public void setEventCode(Event eventCode) {
        EventCode = eventCode;
    }
}
