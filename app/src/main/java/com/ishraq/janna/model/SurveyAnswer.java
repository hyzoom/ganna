package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ahmed on 3/19/2016.
 */
@DatabaseTable(tableName = "answer")
public class SurveyAnswer {
    @DatabaseField(id = true, columnName = "id")
    private Integer AnswerCode;

    @DatabaseField(columnName = "name")
    private String AnswerName;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "survey_id")
    private Survey SurveyCode;

    /////////////////////////////////////////////////////////////////////////////////////////

    public Integer getAnswerCode() {
        return AnswerCode;
    }

    public void setAnswerCode(Integer answerCode) {
        AnswerCode = answerCode;
    }

    public String getAnswerName() {
        return AnswerName;
    }

    public void setAnswerName(String answerName) {
        AnswerName = answerName;
    }

    public Survey getSurveyCode() {
        return SurveyCode;
    }

    public void setSurveyCode(Survey surveyCode) {
        SurveyCode = surveyCode;
    }
}
