package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ahmed on 3/19/2016.
 */
@DatabaseTable(tableName = "answer")
public class SurveyAnswer {
    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField(columnName = "answer_id")
    private Integer AnswerCode;

    @DatabaseField(columnName = "name")
    private String AnswerName;

    @DatabaseField(columnName = "survey_id")
    private Integer SurveyCode;

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

    public Integer getSurveyCode() {
        return SurveyCode;
    }

    public void setSurveyCode(Integer surveyCode) {
        SurveyCode = surveyCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
