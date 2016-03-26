package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.List;

/**
 * Created by Ahmed on 3/19/2016.
 */
@DatabaseTable(tableName = "survey")
public class Survey {

    @DatabaseField(id = true, columnName = "id")
    private Integer SurveyCode;

    @DatabaseField(columnName = "name")
    private String SurveyNameAra;

    @DatabaseField(columnName = "event_id")
    private Integer EventCode;

    private List<SurveyAnswer> answers;

    @DatabaseField(columnName = "answer_id")
    private Integer answerId;

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


    public Integer getEventCode() {
        return EventCode;
    }

    public void setEventCode(Integer eventCode) {
        EventCode = eventCode;
    }

    public List<SurveyAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<SurveyAnswer> answers) {
        this.answers = answers;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }
}
